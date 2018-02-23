package com.example.sasi.intellispace;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sasi.intellispace.Adapters.BookingAdapter;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Speechengine extends AppCompatActivity implements  TextToSpeech.OnInitListener,TextToSpeech.OnUtteranceCompletedListener  {
    DatabaseReference db;
    private Context context;
    SpeechRecognizer speechRecognizer;
    Button speech;
    private TextToSpeech tvvs;
    RecognitionProgressView recognitionProgressView;
    Handler handler=new Handler();
    public int speakCV = 0;
    HashMap<String, String> map = new HashMap<>();
    public int FirstSpeak = 0;
    String building,Roomtype,starttime,invitees;
    String[] speakarray = { " Welcome to IntelliSpace Which Building are you looking for the meeting? ",
                            " Ok! Which Type of Meeting are you looking for! ",
                            "Please tell me your meeting time",


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechengine);
        db= FirebaseDatabase.getInstance().getReference().child("audio");
        context=Speechengine.this;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speech=(Button)findViewById(R.id.speech);

        map.put("Ram","ram98@gmail.com");
        map.get("Ram");
        tvvs=new TextToSpeech(Speechengine.this,Speechengine.this);
        recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
        int[] colors = {
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this,R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorPrimary)
        };
        int[] heights = {60, 76, 58, 80, 55};
        recognitionProgressView.setColors(colors);
        recognitionProgressView.setBarMaxHeightsInDp(heights);
        recognitionProgressView.play();
        recognitionProgressView.setSpeechRecognizer(speechRecognizer);
        recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstSpeak =0;
                speakCV=0;
               speak("HI");


            }
        });


    }
    public void speak(String speakwords){
        if(!tvvs.isSpeaking())
        {
            HashMap<String,String> params=new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"sampleText");
            if(FirstSpeak == 0){
                tvvs.speak(speakwords,TextToSpeech.QUEUE_ADD,params);
                FirstSpeak = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecognition();
                    }
                },1000);

            }
            else{
                tvvs.speak(speakarray[speakCV],TextToSpeech.QUEUE_ADD,params);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecognition();
                    }
                },3500);
            }
        }
        else
        {
            tvvs.stop();
        }

    }
    private void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        speechRecognizer.startListening(intent);

    }
    private void showResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        //Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();
        Iterator iterator=matches.iterator();

        if(!matches.isEmpty()){
            int a =0;
            Toast.makeText(context, matches.get(0)+"   "+speakCV, Toast.LENGTH_SHORT).show();
            if(speakCV<speakarray.length){
                speak(speakarray[speakCV]);
            }
            if(a<speakarray.length){
                if(speakCV==1){
                    building=matches.get(0);
                }else if(speakCV==2) {
                    Roomtype = matches.get(0);
                }else if(speakCV==3){
                    starttime=matches.get(0).replace(" ","");
//                    SpeakAdapter sp=new SpeakAdapter();
//                    sp.setBuild(building);
//                    sp.setRoom(Roomtype);
//                    sp.setTimee(starttime);
//                    db.setValue(sp);
                }
                speakCV++;
                a=speakCV;


            }
            System.out.println("bow "+matches.get(0));
            System.out.println("bowwwwww "+building+Roomtype+starttime);

            if(speakCV == 4){
                String[] splitted = starttime.split("2");

                if(Roomtype.toLowerCase().equals("audio"))
                {
                    Roomtype="Audio-Room";

                }
                if(Roomtype.toLowerCase().equals("video"))
                {
                    Roomtype="Video-Room";

                }
                if(Roomtype.toLowerCase().equals("meeting"))
                {
                    Roomtype="Meeting-Room";
                }
                BookingAdapter bookingAdapter = new BookingAdapter();
                bookingAdapter.setB(building);
                bookingAdapter.setET(timeconvertion(splitted[1]));
                bookingAdapter.setRT(Roomtype);
                bookingAdapter.setST(timeconvertion(splitted[0]));
                Intent intetn = new Intent(Speechengine.this,rcActivity.class);
                intetn.putExtra("caller","Speechengine");
                Boolean f = getIntent().getExtras().getBoolean("flag",false);
                intetn.putExtra("flag",f);
                startActivity(intetn);

            }




        }else{
            Toast.makeText(context, "Let us discuss more...", Toast.LENGTH_SHORT).show();
        }

//            System.out.println("Bow"+matches.get(0));
//            if(matches.get(0).toLowerCase().equals("bird")) {
//                if (!tvvs.isSpeaking()) {
//                    HashMap<String, String> params = new HashMap<String, String>();

//                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sampleText");
//                    tvvs.speak("Ok! Which Type of Meeting are you looking for! ", TextToSpeech.QUEUE_ADD, params);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            startRecognition();
//                        }
//                    },3500);
//
//
//                } else {
//                    tvvs.stop();
//                }
//            }
//            if(matches.get(0).toLowerCase().equals("audio room")||matches.get(0).equals("Video Room")||matches.get(0).equals("Meeting Room")){
//                    if(!tvvs.isSpeaking()){
//                        HashMap<String,String>params = new HashMap<String,String>();
//                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"sampleText");
//                        tvvs.speak("Who are all the participants for the meeting ", TextToSpeech.QUEUE_ADD, params);
//                    }
////                    else{
////                        HashMap<String,String>params = new HashMap<String,String>();
////                        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"sampleText");
////                        tvvs.speak("Ok! Which Type of Meeting are you looking for! ", TextToSpeech.QUEUE_ADD,params);
////                    }
//                else{
//                        tvvs.stop();
//                    }
//                }


    }
    @Override
    protected void onDestroy() {
        if(tvvs!=null)
        {
            tvvs.stop();
            tvvs.shutdown();

            tvvs=null;
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }

    public String timeconvertion(String time){
        String correctedtime = "00:00";
        String[] splited = time.split(":");
        if(splited.length == 2){
            splited[0]=splited[0].length()>0?"0"+splited[0]:splited[0];
            splited[1]=splited[1].length()>0?"0"+splited[0]:splited[0];

            correctedtime = splited[0]+":"+splited[1];

        }
        if(splited.length == 1){
            splited[0]=splited[0].length()>0?"0"+splited[0]:splited[0];

            correctedtime = splited[0]+":00";
        }

        return correctedtime;

    }

    @Override
    public void onInit(int i) {
        tvvs.setOnUtteranceCompletedListener(this);
    }

    @Override
    public void onUtteranceCompleted(String s) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
               // recognitionProgressView.play();


            }
        });
    }
}
