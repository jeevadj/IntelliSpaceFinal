package com.example.sasi.intellispace;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class EventCardClickActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_card_click);

        imageView = (ImageView)findViewById(R.id.imageView3);

        String buildingname = getIntent().getExtras().getString("building").toString().toLowerCase();

        if(buildingname.equals("eb-1")){
            imageView.setImageResource(R.drawable.eb1);
        }else if(buildingname.equals("eb-2")){

        }


    }
}
