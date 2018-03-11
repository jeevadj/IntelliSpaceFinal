package com.example.sasi.intellispace.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sasi.intellispace.R;

import java.util.ArrayList;

/**
 * Created by Nasa on 06-Mar-18.
 */

public class Event_Card_Adapter extends RecyclerView.Adapter<Event_Card_Adapter.ViewHolder> {
    public int LayoutId;
    public static Event_Card_Adapter.MyClickListener myClickListener;
    public ArrayList<EventAdapter> itemlist = new ArrayList<>();

    public Event_Card_Adapter(int layoutId, ArrayList<EventAdapter> itemlist) {
        LayoutId = layoutId;
        this.itemlist = itemlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(LayoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      TextView Building = holder.t1;
      TextView Floor = holder.t2;
      TextView Room = holder.t3;
      TextView Start = holder.t4;
      TextView End = holder.t5;

      Building.setText(itemlist.get(position).getBuilding());
      Floor.setText(itemlist.get(position).getFloor());
      Room.setText(itemlist.get(position).getRoomName());
      Start.setText(itemlist.get(position).getStartT());
      End.setText(itemlist.get(position).getEndT());
    }

    @Override
    public int getItemCount() {
        return itemlist!=null?itemlist.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3,t4,t5;
        public ViewHolder(View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.building_name);
            t2=(TextView)itemView.findViewById(R.id.floor_name);
            t3=(TextView)itemView.findViewById(R.id.room_name);
            t4=(TextView)itemView.findViewById(R.id.start_time);
            t5=(TextView)itemView.findViewById(R.id.end_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public interface MyClickListener{
        void onItemClick(int position,View v);
    }
    public void setOnItemClickListener(Event_Card_Adapter.MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }
}
