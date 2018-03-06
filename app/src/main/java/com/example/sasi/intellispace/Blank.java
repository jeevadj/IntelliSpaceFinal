package com.example.sasi.intellispace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasi.intellispace.Adapters.BookingAdapter;
import com.example.sasi.intellispace.Adapters.ConfirmBookingAdapter;
import com.example.sasi.intellispace.Adapters.EventAdapter;
import com.example.sasi.intellispace.Adapters.Event_Card_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Blank extends AppCompatActivity {
  String name;
  String date;
  ArrayList<EventAdapter> itemlist;
    CalendarView cal;
    TextView selectdate;
    FloatingActionButton ib;
    boolean f;
    Event_Card_Adapter event_card_adapter;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        name=getIntent().getExtras().getString("name");
        setContentView(R.layout.activity_blank);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        cal= (CalendarView) findViewById(R.id.calendarView);
        selectdate = (TextView) findViewById(R.id.select_date);
        recyclerView = (RecyclerView) findViewById(R.id.event_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ib= (FloatingActionButton) findViewById(R.id.imageButton);

        Intent i=getIntent();
        f=i.getExtras().getBoolean("flag");
        Toast.makeText(this, "bow "+f, Toast.LENGTH_SHORT).show();
        Date dat = Calendar.getInstance().getTime();
        SimpleDateFormat form= new SimpleDateFormat("dd-MM-yyyy");
        date = form.format(dat);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month ,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                month=month+1;
                String s=String.valueOf(month);
                s=month<10?"0"+month:s;
                String day =String.valueOf(dayOfMonth);
                day=dayOfMonth<10?"0"+dayOfMonth:day;
                      date=day+"-"+s+"-"+year;
                Toast.makeText(getBaseContext(),"Selected Date is\n\n"
                                +date ,
                        Toast.LENGTH_LONG).show();
                selectdate.setText(date);

                SharedPreferences sharedPreferences = getSharedPreferences("dataset",MODE_PRIVATE);
                final String username = sharedPreferences.getString("Name",null);
                itemlist = new ArrayList<>();
                itemlist.clear();
                databaseReference.child("UsersBooking").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            for(final DataSnapshot building:dataSnapshot.getChildren()){
                                databaseReference.child("UsersBooking").child(username).child(building.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChildren()){
                                            for(final DataSnapshot floor : dataSnapshot.getChildren()){
                                                databaseReference.child("UsersBooking").child(username).child(building.getKey()).child(floor.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.hasChildren()){
                                                            for(final DataSnapshot Room : dataSnapshot.getChildren()){
                                                                databaseReference.child("UsersBooking").child(username).child(building.getKey()).child(floor.getKey()).child(Room.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if(dataSnapshot.hasChildren()){
                                                                            System.out.println("Building "+building.getKey()+"Floor "+floor.getKey()+"Room "+Room.getKey()+"Date "+date);
                                                                            databaseReference.child("UsersBooking").child(username).child(building.getKey()).child(floor.getKey()).child(Room.getKey()).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if(dataSnapshot.hasChildren()){
                                                                                        for (DataSnapshot child:dataSnapshot.getChildren()){
                                                                                            System.out.println("Bow"+child.getKey());
                                                                                            ConfirmBookingAdapter confirmBookingAdapter = child.getValue(ConfirmBookingAdapter.class);
                                                                                            if(!itemlist.contains(new EventAdapter(building.getKey(),floor.getKey(),confirmBookingAdapter.getDate(),confirmBookingAdapter.getStarttime(),confirmBookingAdapter.getEndtime(),Room.getKey()))){
                                                                                                itemlist.add(0,new EventAdapter(building.getKey(),floor.getKey(),confirmBookingAdapter.getDate(),confirmBookingAdapter.getStarttime(),confirmBookingAdapter.getEndtime(),Room.getKey()));
                                                                                            }

                                                                                        }
                                                                                        event_card_adapter = new Event_Card_Adapter(R.layout.eventscard,itemlist);
                                                                                        recyclerView.setAdapter(event_card_adapter);
                                                                                    }
                                                                                    else{
                                                                                         selectdate.setText("No Events");
                                                                                    }


                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }else{
                                                                            selectdate.setText("No Events");
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        }else{
                                                            selectdate.setText("No Events");
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }else{
                                            selectdate.setText("No Events");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }else{
                            selectdate.setText("No Events");
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //new Vacant_Room_Lo    ading_Task().execute();


            }


        });

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingAdapter.setBookdate(date);
                Toast.makeText(Blank.this,  date+"", Toast.LENGTH_SHORT).show();
                if(f==true) {
                    Intent intent =new Intent(Blank.this,Speechengine.class);
                    intent.putExtra("flag",f);
                    startActivity(intent);
                }else
                    {   Intent intent = new Intent(Blank.this,Main2Activity.class);
                        intent.putExtra("flag",f);
                        startActivity(intent);
                }
            }
        });

    }
}
