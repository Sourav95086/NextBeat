package com.nextgenius.nextbeat;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterableClass extends Filter {
    ArrayList<String> strings;
    RecyclerAdapter adapter;
    int size;

    FilterableClass(ArrayList<String> strings , RecyclerAdapter adapter , int size){
        this.strings = strings;
        this.adapter = adapter;
        this.size = size;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<String> filterdData = new ArrayList<>();

            for(int i = 0 ; i<size ; i++){
                if(strings.get(i).toUpperCase().contains(constraint)){
                    filterdData.add(strings.get(i));

                }
            }
            filterResults.count = filterdData.size();
            filterResults.values = filterdData;
        }else{
            filterResults.count = strings.size();
            filterResults.values = strings;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.arrayList = (ArrayList<String>) results.values;
        adapter.notifyDataSetChanged();
    }
}
