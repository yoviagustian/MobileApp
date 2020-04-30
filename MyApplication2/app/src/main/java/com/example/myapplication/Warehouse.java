package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Warehouse extends AppCompatActivity {
    EditText et_search;
    LocalDBEncryption myDBCONN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        myDBCONN = new LocalDBEncryption(this);

        et_search = findViewById(R.id.et_search);

        ListView listView=(ListView)findViewById(R.id.listview);

        final ArrayList<String> arrayList=new ArrayList<>();

        try{
            ArrayList<String> items = myDBCONN.readAllData();
            for(int i=0; i<items.size(); i++)
            {
                arrayList.add(items.get(i));
            }
        }catch (Exception e) {
            //Log.d("SQLiteStatus", "ERROR : " + e);
        }

        //Create Adapter
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        //assign adapter to listview
        listView.setAdapter(arrayAdapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Warehouse.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cari(View view)
    {
    }

    public void tambahItem(View view)
    {
        Intent intent = new Intent(this, TambahItem.class);
        startActivity(intent);
    }
}
