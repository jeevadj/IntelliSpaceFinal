package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasi.intellispace.Adapters.CreateSpaceAdapter;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateSpace extends AppCompatActivity {

    Button createspace,delete;
    EditText edt_Building;
    Spinner sp_floor;
    String build,Flr,Room;
    Spinner sp_rooms;
    private DatabaseReference mref;
    Firebase fb;
    Spinner NOR;
    TextView shortform;
    Date currentdate;
    Spinner spinner;
    String shortfR ;
    String RoomName,date,time;
    String[] Nor = {"1","2","3","4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);
        createspace=(Button)findViewById(R.id.createspace);
        edt_Building=(EditText)findViewById(R.id.Building);
        sp_floor=(Spinner) findViewById(R.id.floor);
        shortform =(TextView)findViewById(R.id.shortform);
        delete =(Button)findViewById(R.id.delete);
        NOR = (Spinner)findViewById(R.id.NOR);
        sp_rooms=(Spinner)findViewById(R.id.rooms);
        mref= FirebaseDatabase.getInstance().getReference();
        System.out.println("REference Url "+mref.getRef());
        spinner = (Spinner) findViewById(R.id.rooms);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinner2 = (Spinner) findViewById(R.id.floor);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.array_floor, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.No_of_Rooms,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NOR.setAdapter(adapter2);


        NOR.setVisibility(View.GONE);
        shortform.setVisibility(View.GONE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shortformAssign();
                shortform.setText(shortfR);
                if(!spinner.getSelectedItem().toString().equals("Room-Type")){
                    NOR.setVisibility(View.VISIBLE);
                    shortform.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        createspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                build=edt_Building.getText().toString();
                Flr=sp_floor.getSelectedItem().toString();
                Room=sp_rooms.getSelectedItem().toString();
                System.out.println("Checking Values: "+build+Flr+Room);
                currentdate = Calendar.getInstance().getTime();
                SimpleDateFormat form= new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeform = new SimpleDateFormat("HH:mm:ss");

                date= form.format(currentdate);
                time = timeform.format(currentdate);
                System.out.println("TIme : "+ date + time + shortfR);
                try {


                    if (!TextUtils.isEmpty(build) && (!TextUtils.isEmpty(Flr) && (!TextUtils.isEmpty(Room)))) {
//                        CreateSpaceAdapter space = new CreateSpaceAdapter();
//                        space.setBuilding(build);
//                        space.setFloor(Flr);
//                        space.setRooms(Room);
//
//                        //  mref.child("Building").child(build).child(Flr).child(Room).setValue(space);
                        new Create_Space_Task().execute();

                    } else {
                        Toast.makeText(CreateSpace.this, "Empty Columns", Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build=edt_Building.getText().toString();
                Flr=sp_floor.getSelectedItem().toString();
                Room=sp_rooms.getSelectedItem().toString();
                new Delete_Value_Task().execute();
            }
        });

    }
    public void shortformAssign(){
        String selectedtype = spinner.getSelectedItem().toString();
        if(selectedtype.equals("Audio-Room")){
            shortfR = "AR";
        }
        else if(selectedtype.equals("Video-Room")){
            shortfR = "VR";
        }
        else if(selectedtype.equals("Web Conferencing Room")){
            shortfR = "WC";
        }
        else if(selectedtype.equals("Meeting-Room")){
            shortfR= "MR";
        }
    }
    public class Create_Space_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            mref.child("Building").child(build).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    System.out.println("Bow "+dataSnapshot.getChildrenCount());
                    System.out.println("bow "+dataSnapshot.getChildrenCount()+dataSnapshot.getKey());
                    RoomName = shortfR+NOR.getSelectedItem().toString();
                    final CreateSpaceAdapter space = new CreateSpaceAdapter();
                    space.setRoomtype(spinner.getSelectedItem().toString());
                    final String selectedtype = spinner.getSelectedItem().toString();
                    mref.child("Building").child(build).child(selectedtype).child(Flr).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("Children  "+dataSnapshot.getChildrenCount());
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                if(RoomName.equals(child.getKey())){
                                    Toast.makeText(CreateSpace.this, "Room already Exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    mref.child("Building").child(build).child(selectedtype).child(Flr).child(RoomName).setValue(space);
                                    startActivity(new Intent(CreateSpace.this,CreateSpace.class));
                                    System.out.println("stick "+build+selectedtype+Flr+RoomName);
                                    finish();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
    public class Delete_Value_Task extends  AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {


            RoomName = shortfR+NOR.getSelectedItem().toString();
            mref.child("Building").child(build).child(Flr).child(RoomName).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    System.out.println("bowwwwwww");
                }
            });
            return null;
        }
    }
}
