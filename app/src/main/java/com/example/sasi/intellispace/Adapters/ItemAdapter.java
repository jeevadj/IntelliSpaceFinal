package com.example.sasi.intellispace.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasi.intellispace.R;

import java.util.ArrayList;

/**
 * Created by hp on 06-02-2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public int layoutid;
    public static MyClickListener myClickListener;
    public static ArrayList<CardAdapter> itemlist;
    public ItemAdapter(int layout , ArrayList<CardAdapter> list) {
        this.layoutid = layout;
        this.itemlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutid,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView building = holder.tv1;
        TextView floor = holder.tv2;
        TextView room = holder.tv3;

        building.setText(itemlist.get(position).getBuilding());
        floor.setText(itemlist.get(position).getFloor());
        room.setText(itemlist.get(position).getRoom());
        System.out.println("bow"+itemlist.get(position).getBuilding());
    }

    @Override
    public int getItemCount() {
        return itemlist.size()>0?itemlist.size():0;
    }

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3;

        public ViewHolder(final View itemView) {
            super(itemView);

            tv1=(TextView)itemView.findViewById(R.id.text_building);
            tv2=(TextView)itemView.findViewById(R.id.text_floor);
            tv3=(TextView)itemView.findViewById(R.id.text_room);
            System.out.println("fow "+itemlist.size());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), getAdapterPosition()+"   "+getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    myClickListener.onItemClick(getAdapterPosition(), view);
                }
            });


        }
    }

    public interface MyClickListener{
        void onItemClick(int position,View v);
    }
    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }
}