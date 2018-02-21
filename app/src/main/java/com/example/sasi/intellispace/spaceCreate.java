package com.example.sasi.intellispace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class spaceCreate extends AppCompatActivity
{
    TextView Bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_create);

        Bs=(TextView)findViewById(R.id.bookspace);

        Bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(spaceCreate.this,rcActivity.class);
                i.putExtra("caller","spaceCreate");
                startActivity(i);
            }
        });
    }
}
