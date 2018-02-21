package com.example.sasi.intellispace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Visiter extends AppCompatActivity {
    Button Smart;
    Button Visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiter);
        Smart = (Button)findViewById(R.id.Smart);
        Visitor=(Button)findViewById(R.id.Visitor);
        Smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Visiter.this, OrgSignIn .class));
            }
        });


        
    }

}
