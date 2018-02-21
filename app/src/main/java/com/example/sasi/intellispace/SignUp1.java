package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.example.sasi.intellispace.Adapters.UploadAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp1 extends AppCompatActivity {
    private DatabaseReference mRef;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String date="^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d{2}$";
    private EditText uname,uemail,udesig,udob,upass,uphone;
    private Button signup;
    String org;
    AmazonSimpleEmailService client;
    static String TO;
    CognitoCachingCredentialsProvider credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        org=getIntent().getExtras().getString("key");
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        credentials= new CognitoCachingCredentialsProvider(getApplicationContext(), "us-west-2:c0f1fa86-4263-451c-a754-69c10fb68253", Regions.US_WEST_2);
        client = new AmazonSimpleEmailServiceClient(credentials);
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        uname= (EditText) findViewById(R.id.uname);
        uemail= (EditText) findViewById(R.id.uemail);
        uphone= (EditText) findViewById(R.id.uphone);
        udesig= (EditText) findViewById(R.id.udesig);
        udob= (EditText) findViewById(R.id.DOB);
        signup= (Button) findViewById(R.id.signup);
        upass= (EditText) findViewById(R.id.pass);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String name=uname.getText().toString();
                 String email = uemail.getText().toString();

                 TO=email;

                String phone=uphone.getText().toString();
                String desig=udesig.getText().toString();
                String dob=udob.getText().toString();
                String pass=upass.getText().toString();
//                if (!email.matches(emailPattern)) {
//                    uemail.setError("Invalid Email");
//                }
                if(name.length()==0||(!name.matches("[a-zA-Z ]+"))){

                    uname.requestFocus();
                    uname.setError("Enter only alphabetical charactor");
                }

                else if(!email.matches(emailPattern)){
                    uemail.requestFocus();
                    uemail.setError("Enter valid e-mail id");
                }
                else if(!dob.matches(date)){
                    udob.requestFocus();
                    udob.setError("Enter valid date format");
                }
//                else{
//                    Toast.makeText(SignUp1.this, "Validation Success!",
//                            Toast.LENGTH_LONG).show();
//                }


               else if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(desig)&&!TextUtils.isEmpty(dob)&&!TextUtils.isEmpty(pass)) {

                    UploadAdapter adapter = new UploadAdapter();
                    adapter.setName(name);
                    adapter.setEmail(email);
                    adapter.setDesig(desig);
                    adapter.setDob(dob);
                    adapter.setPhone(phone);
                    adapter.setPassword(pass);

                    mRef.child(org).child(name).setValue(adapter);
                    new SES_Example().execute();
                    Intent i=new Intent(SignUp1.this, MainActivity.class);
                    i.putExtra("key",org);
                    finish();
                }
                else
                {
                    Toast.makeText(SignUp1.this, "Empty columns", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class SES_Example extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            client.verifyEmailAddress(new VerifyEmailAddressRequest()
                    .withEmailAddress(TO));
            return null;
        }
    }
}






