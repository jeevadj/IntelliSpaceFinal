package com.example.sasi.intellispace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasi.intellispace.Adapters.BookingAdapter;
import com.example.sasi.intellispace.Adapters.CardAdapter;
import com.example.sasi.intellispace.Adapters.ConfirmBookingAdapter;
import com.example.sasi.intellispace.Adapters.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class rcActivity extends AppCompatActivity
{

    public ArrayList<CardAdapter> itemCardAdapter =new ArrayList<>();
    public ArrayList<ConfirmBookingAdapter> bookingAdapters;
    int a=0;
    public ArrayList<CardAdapter> cardAdapters = new ArrayList<>();
    public HashMap<String, ArrayList<ConfirmBookingAdapter>> hashMap;
    public ItemAdapter itemArrayAdapter;
    ArrayList<String> building_spinner;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Spinner BuildingSpinnner;
    DatabaseReference databaseReference;
    String SelectedBuilding;
    TextView emptyview;



    @Override
    protected void onResume() {
        super.onResume();
        itemArrayAdapter.setOnItemClickListener(new ItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Toast.makeText(rcActivity.this, "Card clicked "+ BookingAdapter.al.get(position).getBuilding(), Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(rcActivity.this)
                        .setTitle("Confirm Booking...")
                        .setMessage("Are You Confirm to book Room  "+BookingAdapter.al.get(position).getRoom() +" in "+BookingAdapter.al.get(position).getFloor()+" of "+BookingAdapter.al.get(position).getBuilding()+" on "+BookingAdapter.bookdate+ " from "+BookingAdapter.getST()+" to "+BookingAdapter.getET())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(rcActivity.this, "Confirmed..", Toast.LENGTH_SHORT).show();

                                ConfirmBookingAdapter adapter = new ConfirmBookingAdapter();
                                adapter.setDate(BookingAdapter.bookdate);
                                adapter.setStarttime(BookingAdapter.getST());
                                adapter.setEndtime(BookingAdapter.getET());

                                databaseReference.child("BookedTimings").child(BookingAdapter.al.get(position).getBuilding()).child(BookingAdapter.getRT()).child(BookingAdapter.al.get(position).getFloor()).child(BookingAdapter.al.get(position).getRoom()).child(BookingAdapter.bookdate).push().setValue(adapter);

                                startActivity(new Intent(rcActivity.this,Blank.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(rcActivity.this, "Cancelled..", Toast.LENGTH_SHORT).show();

                            }
                        });
                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        building_spinner = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        hashMap = new HashMap<>();

        layoutManager = new LinearLayoutManager(this);
        Runnable r1 =new Runnable() {
            @Override
            public void run() {
                check_room();
            }
        };

        Thread t1 = new Thread(r1);


        t1.start();

//        check_room();

        recyclerView.setLayoutManager(layoutManager);
        BuildingSpinnner = (Spinner)findViewById(R.id.buildingspinner);
        emptyview =(TextView)findViewById(R.id.textView2);
        String caller = getIntent().getExtras().get("caller").toString();
        System.out.println("bowwww"+caller);
        System.out.println("bowwwww"+BookingAdapter.B+BookingAdapter.ST+BookingAdapter.ET+BookingAdapter.RT);


        building_spinner.add("Select Building");
        databaseReference.child("Building").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    building_spinner.add(child.getKey());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setCards();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,building_spinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BuildingSpinnner.setAdapter(arrayAdapter);

        if(itemCardAdapter.isEmpty()){
            emptyview.setVisibility(View.VISIBLE);
        }





        //  new Spinner_Item_Task().execute();

        BuildingSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!BuildingSpinnner.getSelectedItem().toString().equals("Select Building")){
                    itemCardAdapter.clear();
                    BookingAdapter.al.clear();
                    SelectedBuilding = BuildingSpinnner.getSelectedItem().toString();
                    System.out.println("Selected Building : "+SelectedBuilding);
                    new Vacant_Room_Loading_Task().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void check_room(){
        final int[] a = {0};
        databaseReference.child("BookedTimings").child(BookingAdapter.B).child(BookingAdapter.RT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot child: dataSnapshot.getChildren()){
                    System.out.println("Floor"+child.getKey());
                    databaseReference.child("BookedTimings").child(BookingAdapter.B).child(BookingAdapter.RT).child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(final DataSnapshot child1:dataSnapshot.getChildren()){
                                System.out.println("Room Name : "+child1.getKey());
                                databaseReference.child("BookedTimings").child(BookingAdapter.B).child(BookingAdapter.RT).child(child.getKey()).child(child1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot child2: dataSnapshot.getChildren()){
                                            System.out.println("Date : "+child2.getKey());
                                            databaseReference.child("BookedTimings").child(BookingAdapter.B).child(BookingAdapter.RT).child(child.getKey()).child(child1.getKey()).child(child2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    bookingAdapters = new ArrayList<>();
                                                    for(DataSnapshot child3: dataSnapshot.getChildren()){
                                                        ConfirmBookingAdapter confirmBookingAdapter = child3.getValue(ConfirmBookingAdapter.class);

                                                        bookingAdapters.add(0,confirmBookingAdapter);
                                                    }

                                                    hashMap.put(child.getKey()+"@"+child1.getKey()+"@"+child2.getKey(),bookingAdapters);
                                                    System.out.println("hash"+hashMap);
                                                   if(a[0] ==0){
                                                       Vacant_Room();
                                                       a[0] =1;
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
        //new Vacant_Room_Lo    ading_Task().execute();


    }

    public void Vacant_Room(){
        System.out.println("building bow"+BookingAdapter.getB());
        itemCardAdapter.clear();
        BookingAdapter.al.clear();
        System.out.println("bowwwwwwwww"+BookingAdapter.B+BookingAdapter.RT+BookingAdapter.ST+BookingAdapter.ET);

//            databaseReference.child("Building").child(SelectedBuilding).child("Video-Room").addListenerForSingleValueEvent(new ValueEventListener() {
        databaseReference.child("Building").child(BookingAdapter.B).child(BookingAdapter.RT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Bowww building"+dataSnapshot.getChildrenCount()+ "   "+dataSnapshot.getKey() );


                for( DataSnapshot child: dataSnapshot.getChildren()){
                    final String floor = child.getKey();
                    System.out.println("floor "+floor);


                    databaseReference.child("Building").child(BookingAdapter.B).child(BookingAdapter.RT).child(floor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("Room Name : "+dataSnapshot.getKey());

                            for( DataSnapshot room: dataSnapshot.getChildren()){
                                String roomname = room.getKey();
                                System.out.println("Bowwwww "+roomname);
                                System.out.println("card "+BookingAdapter.B+floor+BookingAdapter.bookdate);
                                System.out.println("ysysys"+dataSnapshot.getChildrenCount());

                                // DateAdopter da=kiruba.getValue(DateAdopter.class);
                                //  System.out.println("stick "+da.getStartdate()+da.getEnddate());

                                if(hashMap.containsKey(floor+"@"+roomname+"@"+BookingAdapter.bookdate)){
                                    String checkfloor=null, checkroom=null;
                                    System.out.println("Is there");
                                    ArrayList<ConfirmBookingAdapter> adapters = hashMap.get(floor+"@"+roomname+"@"+BookingAdapter.bookdate);
                                    for(int i =0 ; i<adapters.size(); i++){
                                        System.out.println("time"+"   "+hashMap.get(floor+"@"+roomname+"@"+BookingAdapter.bookdate).get(i).getStarttime()+adapters.get(i).getEndtime()+" "+BookingAdapter.ST);
                                        String[] splited = adapters.get(i).getEndtime().split(":");
                                        String[] splited1 = adapters.get(i).getStarttime().split(":");
                                        int cloudEndTime = Integer.parseInt(splited[0]);
                                        int cloudStartTime = Integer.parseInt(splited1[0]);
                                        String[] splited2 = BookingAdapter.ST.split(":");
                                        String[] splited3 = BookingAdapter.ET.split(":");
                                        int  EndTime= Integer.parseInt(splited3[0]);
                                        int  StartTime= Integer.parseInt(splited2[0]);
                                        if(cloudStartTime==StartTime||cloudEndTime>StartTime){
                                            checkfloor=floor;
                                            checkroom=room.getKey();


                                        }else{
                                          if(!(floor.equals(checkfloor)&&room.getKey().equals(checkroom))){
                                              if(itemCardAdapter.contains(new CardAdapter(BookingAdapter.B,floor,room.getKey()))){
                                                  System.out.println("Already added.");

                                              }
                                              else{
                                                  cardAdapters.add(0,new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                  BookingAdapter.al.add(0,new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                  itemCardAdapter.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                  setCards();
                                              }
                                          }
                                        }


                                    }
                                }else{
                                    System.out.println("Not there");
                                    if(itemCardAdapter.contains(new CardAdapter(BookingAdapter.B,floor,room.getKey()))){
                                        System.out.println("Already added.");

                                    }
                                    else{
                                        cardAdapters.add(0,new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                        BookingAdapter.al.add(0,new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                        itemCardAdapter.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                        setCards();
                                    }
                                }


//                                                        BookingAdapter.al.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));





                            }
                            System.out.println("Cards : "+itemCardAdapter.size());
                            setCards();

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
    //  public class Spinner_Item_Task extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            databaseReference.child("Building").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                   for (DataSnapshot child: dataSnapshot.getChildren()){
//                       building_spinner.add(child.getKey());
//                   }
//                }
//
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            return null;
//        }
//    }
    void setCards(){
        emptyview.setVisibility(View.GONE);
        itemArrayAdapter = new ItemAdapter(R.layout.building_card, itemCardAdapter);
        recyclerView.setAdapter(itemArrayAdapter);
    }
    public class Checking_Room_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }
    }


    public class Vacant_Room_Loading_Task extends AsyncTask<String, Integer,String>{

        @Override
        protected String doInBackground(String... strings) {


            return null;
        }
    }


}