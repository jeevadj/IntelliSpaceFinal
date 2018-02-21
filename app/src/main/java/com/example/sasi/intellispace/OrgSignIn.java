package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrgSignIn extends AppCompatActivity {
    EditText orgname;
    Button create,con;
    String Namo;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_sign_in);
        orgname= (EditText) findViewById(R.id.orgname);
        create= (Button) findViewById(R.id.create);
        con= (Button) findViewById(R.id.con);
        mRef= FirebaseDatabase.getInstance().getReference().child("Organisations");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrgSignIn.this, Neworg .class));
            }
        });



        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Namo=orgname.getText().toString();
                if (!TextUtils.isEmpty(Namo))
                {
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(Namo)) {
                              Intent i=new Intent(OrgSignIn.this,MainActivity.class);
                                i.putExtra("key",Namo);
                             startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(OrgSignIn.this, "Please Register Your Organisation", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                }



        });

    }
}
