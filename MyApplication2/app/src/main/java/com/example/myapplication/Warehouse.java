package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Warehouse extends AppCompatActivity {
    itemAdapter adapter;
    EditText et_search;
    LocalDBEncryption myDBCONN;
    List<LocalDBEncryption.item> items;

    public static class itemAdapter extends ArrayAdapter<LocalDBEncryption.item> implements Filterable {

        public Context mContext;
        SearchFilters filter;
        ArrayList<LocalDBEncryption.item> filteredList;
        public ArrayList<LocalDBEncryption.item> itemList = new ArrayList<>();

        public itemAdapter(@NonNull Context context, ArrayList<LocalDBEncryption.item> list) {
            super(context, 0 , list);
            mContext = context;
            itemList = list;
            filteredList = list;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public LocalDBEncryption.item getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public int getSelectedItemPosition(int position) {
            return itemList.get(position)._id;
        }

        public int getJual(int position) {
            return itemList.get(position)._hargaJual;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext)
                        .inflate(R.layout.activity_list,parent,false);

            LocalDBEncryption.item currentitem = itemList.get(position);

            TextView name = (TextView) listItem.findViewById(R.id.textView_name);
            name.setText(currentitem._nama);

            return listItem;
        }

        @Override
        public Filter getFilter() {

            if(filter == null) {

                filter = new SearchFilters(filteredList, this);
            }

            return filter;
        }

        public void setListAdapter() {
            itemList.clear();
        }
    }
//      ----------------------- Main ----------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        myDBCONN = new LocalDBEncryption(this);

        et_search = findViewById(R.id.et_search);
        ListView listView=(ListView)findViewById(R.id.listview);

        ArrayList<LocalDBEncryption.item> arrayList = new ArrayList<>();
        items = myDBCONN.readAllData();
        for(int i=0; i<items.size(); i++)
        {
            arrayList.add(items.get(i));
        }

        //Create Adapter
        adapter = new itemAdapter(this, arrayList);

        //assign adapter to listview
        listView.setAdapter(adapter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Warehouse.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer position = adapter.getSelectedItemPosition(i);
//                Toast.makeText(Warehouse.this,"clicked item:"+position+" ",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Warehouse.this, UpdateItem.class);

                intent.putExtra("id", String.valueOf(position));

                startActivity(intent);
            }
        });
    }

    public void tambahItem(View view)
    {
        Intent intent = new Intent(this, TambahItem.class);
        startActivity(intent);
    }
}
