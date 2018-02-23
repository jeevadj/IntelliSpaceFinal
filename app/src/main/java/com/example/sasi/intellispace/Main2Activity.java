package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.example.sasi.intellispace.Adapters.BookingAdapter;
import com.example.sasi.intellispace.Adapters.UploadAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity
{
    ScrollView scrollview;
    TextView q1,q2,q3,q4,q5,q6,a1,a2,a3,a4,a5,a6;
    EditText r;
    Button finish;
    String input;
    int replyCount=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        q1=(TextView)findViewById(R.id.ques1);
        q2=(TextView)findViewById(R.id.ques2);
        q3=(TextView)findViewById(R.id.ques3);
        q4=(TextView)findViewById(R.id.ques4);
        q5=(TextView)findViewById(R.id.ques5);
        q6=(TextView)findViewById(R.id.ques6);

        a1=(TextView)findViewById(R.id.ans1);
        a2=(TextView)findViewById(R.id.ans2);
        a3=(TextView)findViewById(R.id.ans3);
        a4=(TextView)findViewById(R.id.ans4);
        a5=(TextView)findViewById(R.id.ans5);
        a6=(TextView)findViewById(R.id.ans6);

        r=(EditText)findViewById(R.id.reply);
        finish=(Button)findViewById(R.id.done);
        scrollview=(ScrollView)findViewById(R.id.scrollView);




        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyCount++;
                input=r.getText().toString();
                setreply();

                scrollview.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

                r.setText(null);
            }
        });
    }

    public void setreply()
    {
        if(replyCount==1)
        {
            a1.setText(input);
            a1.setVisibility(View.VISIBLE);
            q2.setVisibility(View.VISIBLE);
        }

        if(replyCount==2)
        {
            a2.setText(input);
            BookingAdapter.setB(input);
            a2.setVisibility(View.VISIBLE);
            q3.setVisibility(View.VISIBLE);
        }

        if(replyCount==3)
        {
            a3.setText(input.trim());
            if(input.toLowerCase().equals("audio"))
            {
                input="Audio-Room";

            }
            if(input.toLowerCase().equals("video"))
            {
                input="Video-Room";

            }
            if(input.toLowerCase().equals("meeting"))
            {
                input="Meeting-Room";
            }
            BookingAdapter.setRT(input);
            a3.setVisibility(View.VISIBLE);
            q4.setVisibility(View.VISIBLE);
        }

        if(replyCount==4)
        {
            a4.setText(input);
            BookingAdapter.setST(timeconvertion(input));
            a4.setVisibility(View.VISIBLE);
            q5.setVisibility(View.VISIBLE);
        }

        if(replyCount==5)
        {
            a5.setText(input);
            BookingAdapter.setET(timeconvertion(input));
            a5.setVisibility(View.VISIBLE);
            q6.setVisibility(View.VISIBLE);
        }

        if(replyCount==6)
        {
            a6.setText(input);
            a6.setVisibility(View.VISIBLE);
            finish.setText("Done");
        }
        if(replyCount>=7)
        {
            Intent i=new Intent(Main2Activity.this,rcActivity.class);
            Boolean f = getIntent().getExtras().getBoolean("flag",false);
            i.putExtra("flag",f);
            i.putExtra("caller","chatActivity");
            startActivity(i);
        }
    }
    public String timeconvertion(String time){
        String correctedtime = "00:00";
        String[] splited = time.split(":");
        if(splited.length == 2){
            splited[0]=splited[0].length()==1?"0"+splited[0]:splited[0];
            splited[1]=splited[1].length()==1?"0"+splited[1]:splited[1];

            correctedtime = splited[0]+":"+splited[1];

        }
        if(splited.length == 1){
            splited[0]=splited[0].length()==1?"0"+splited[0]:splited[0];

            correctedtime = splited[0]+":00";
        }

        return correctedtime;

    }
}

