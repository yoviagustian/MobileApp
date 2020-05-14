package com.example.myapplication;

import android.widget.Filter;

import java.util.ArrayList;

public class SearchFilters extends Filter {

    ArrayList<LocalDBEncryption.item> filteredList;
    Warehouse.itemAdapter adapter;

    public SearchFilters(ArrayList<LocalDBEncryption.item> filteredList, Warehouse.itemAdapter adapter) {
        this.filteredList = filteredList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults filterResults = new FilterResults();

        // doing some validation


        if(constraint != null && constraint.length() >0) {

            // Change To Upper Case
            constraint = constraint.toString().toUpperCase();

            // Holding the data that is filtered

            ArrayList<LocalDBEncryption.item> filteredMovie = new ArrayList<>();

            for (int i = 0; i <filteredList.size(); i++ ) {

                if(filteredList.get(i)._nama.toUpperCase().contains(constraint)) {

                    filteredMovie.add(filteredList.get(i));

                }



            }


            filterResults.count = filteredMovie.size();
            filterResults.values = filteredMovie;

        }else {

            filterResults.count = filteredList.size();
            filterResults.values = filteredList;
        }


        return filterResults;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.itemList = (ArrayList<LocalDBEncryption.item>) results.values;
        adapter.notifyDataSetChanged();

    }
}
