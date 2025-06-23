package com.nextgenius.nextbeat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<String> arrayList;
    ArrayList<File> mySong;
    FilterableClass filterableClass;
    ArrayList<String> search;

    RecyclerAdapter(Context context ,ArrayList<String> arrayList ,  ArrayList<File> mySong){
        this.context = context;
        this.arrayList = arrayList;
        this.mySong = mySong;
        search = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row , parent , false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.textView.setText(arrayList.get(position));
    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = arrayList.get(position);
            Intent intent = new Intent(context , PlaySong.class);
            intent.putExtra("item",item);
            intent.putExtra("list",mySong);
            intent.putExtra("position" , position);
            context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterableClass==null){
            filterableClass = new FilterableClass(search , this ,arrayList.size());

        }
        return filterableClass;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.songName);
            linearLayout = itemView.findViewById(R.id.clickableArea);
        }
    }
}
