package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasi.intellispace.Adapters.UploadAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText pass;
    Button login;
    TextView usignup;
    private DatabaseReference mRef, mRef2,admin,admin2;
    String email,Pass,org;
    Switch ch;
    boolean f=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        org=new String();
        try {
            org=getIntent().getExtras().getString("key");
            username= (EditText) findViewById(R.id.username);
            pass= (EditText) findViewById(R.id.pass);
            login= (Button) findViewById(R.id.login);
            usignup= (TextView) findViewById(R.id.usignup);
            ch=(Switch)findViewById(R.id.switch1);
            mRef= FirebaseDatabase.getInstance().getReference().child("Users").child(org);
            admin= FirebaseDatabase.getInstance().getReference().child("Users").child(org).child("admin");
            ch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ch.isChecked()){
                        f=true;
                    }
                    if(!ch.isChecked()){
                        f=false;
                    }
                }
            });



            usignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(MainActivity.this, SignUp1.class);
                    i.putExtra("key",org);
                    startActivity(i);

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email=username.getText().toString();
                    Pass=pass.getText().toString();
//                    Toast.makeText(MainActivity.this, "bow "+f , Toast.LENGTH_SHORT).show();

                    if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(Pass)) {

                        admin.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(email)) {
                                    if(Pass.equals("admin"))
                                    {
                                        Intent i = new Intent(MainActivity.this, CreateSpace.class);

                                             //   i.putExtra("email", adapter.getEmail());
                                                startActivity(i);
                                                finish();
                                    }
//                                    admin2 = admin.child(email);
//                                    admin2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            UploadAdapter adapter = dataSnapshot.getValue(UploadAdapter.class);
//
//                                            if (adapter.getPassword().equals(Pass)) {
//                                                Intent i = new Intent(MainActivity.this, CreateSpace.class);
//                                                i.putExtra("name", adapter.getName());
//                                                i.putExtra("email", adapter.getEmail());
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
                                }
                                else
                                {
                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(email)) {
                                                mRef2 = mRef.child(email);
                                                mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        UploadAdapter adapter = dataSnapshot.getValue(UploadAdapter.class);

                                                        if (adapter.getPassword().equals(Pass)) {
                                                            Intent i = new Intent(MainActivity.this, Blank.class);
                                                            i.putExtra("name", adapter.getName());
                                                            i.putExtra("email", adapter.getEmail());
                                                            i.putExtra("flag",f);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Empty columns", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception no such org value", Toast.LENGTH_SHORT).show();
        }



    }
}
