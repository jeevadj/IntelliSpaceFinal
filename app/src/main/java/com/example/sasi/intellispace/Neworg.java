package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Neworg extends AppCompatActivity {
    EditText orgname;
    Button register;
    String org;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworg);
        orgname= (EditText) findViewById(R.id.orgname);
        register= (Button) findViewById(R.id.register);
        mRef= FirebaseDatabase.getInstance().getReference().child("Organisations");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               org=orgname.getText().toString();

                if (!TextUtils.isEmpty(org))
                {
                    OrgAdapter adapter=new OrgAdapter();

                    adapter.setOrgname(org);
                    mRef.child(org).setValue(adapter);
                    startActivity(new Intent(Neworg.this,OrgSignIn.class));
                }
                else
                {
                    Toast.makeText(Neworg.this, "Please Enter Your Org name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
