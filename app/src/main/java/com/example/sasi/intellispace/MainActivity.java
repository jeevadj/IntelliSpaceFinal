package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;


public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText pass;
    Button login;
    TextView usignup,fpass;
    private DatabaseReference mRef, mRef2,admin,admin2;
    String email,Pass,org;
    Switch ch;
    boolean f=false,fl=false;
    String t1,t2,t3,t4;
    static AmazonSimpleEmailService client;
    CognitoCachingCredentialsProvider credentials;
    static final String FROM = "intellispace.meeting@outlook.com";
    static final String SUBJECT = "Password Recovery for Intellispace";

    static String HTMLBODY = "";

    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        org=new String();
        credentials= new CognitoCachingCredentialsProvider(getApplicationContext(), "us-west-2:c0f1fa86-4263-451c-a754-69c10fb68253", Regions.US_WEST_2);
        client = new AmazonSimpleEmailServiceClient(credentials);
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        try {
            org=getIntent().getExtras().getString("key");
            username= (EditText) findViewById(R.id.username);
            pass= (EditText) findViewById(R.id.pass);
            login= (Button) findViewById(R.id.login);
            fpass=(TextView)findViewById(R.id.forgot);
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
            fpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email=username.getText().toString();
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(email)) {
                                mRef2 = mRef.child(email);
                                mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UploadAdapter adapter = dataSnapshot.getValue(UploadAdapter.class);
                                        t1=adapter.getPassword();
                                        t2=adapter.getEmail();
                                        HTMLBODY = "<h4>Password Recovery</h4>"
                                                + "<p>Username "+email

                                                + "<p>Password "+t1;

                                        new SES_Example().execute();

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
                                                        t3=adapter.getEmail();
                                                        t4=adapter.getPassword();
                                                        new List_Verified_Task().execute();

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
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Exception no such org value", Toast.LENGTH_SHORT).show();
        }

    }
    public  class SES_Example extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {


            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(t2))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);
            // Comment or remove the next line if you are not using a
            // configuration set
            // .withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);
            Looper.prepare();
            Toast.makeText(MainActivity.this, "password has been sent your email", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return null;
        }
    }
    public class List_Verified_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            List<String> result = client.listVerifiedEmailAddresses().getVerifiedEmailAddresses();
            System.out.println("bowww"+result+" "+t3);

            for(int i=0;i<result.size();i++)
            {
                if(t3.equals(result.get(i).toString())){
                    System.out.println("boww"+result.get(i).toString());
                    fl=true;
                }
            }
            if (t4.equals(Pass)&&fl==true) {
                Intent i = new Intent(MainActivity.this, Blank.class);
                i.putExtra("flag",f);
                startActivity(i);
                finish();
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, t3+" is not verified", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

}
