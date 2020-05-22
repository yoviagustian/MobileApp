package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Kasir extends AppCompatActivity {
    Integer sm = 0;
    EditText et_search;
    Warehouse.itemAdapter adapter, adapter1;
    LocalDBEncryption myDBCONN;
    List<LocalDBEncryption.item> items;
    TextView tv_hasil;

    ArrayList<LocalDBEncryption.item> arrayList = new ArrayList<>();
    ArrayList<LocalDBEncryption.item> arrayList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir);
        myDBCONN = new LocalDBEncryption(this);

        et_search = findViewById(R.id.et_search);
        ListView listView=(ListView)findViewById(R.id.listview);
        ListView listView1= (ListView)findViewById(R.id.listview1);

        items = myDBCONN.readAllData();
        for(int i=0; i<items.size(); i++)
        {
            arrayList.add(items.get(i));
        }

        //Create Adapter
        adapter = new Warehouse.itemAdapter(this, arrayList);
        adapter1 = new Warehouse.itemAdapter(this, arrayList1);

        //assign adapter to listview
        listView.setAdapter(adapter);
        listView1.setAdapter(adapter1);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Kasir.this).adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Integer position = adapter.getSelectedItemPosition(i);
//                Toast.makeText(Kasir.this,"clicked item:"+position+" ",Toast.LENGTH_SHORT).show();
                tambahi(i);
            }
        });

//        ----------------------------------------------------------------

        //add listener to listview
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Integer position = adapter1.getSelectedItemPosition(i);
//                Toast.makeText(Kasir.this,"clicked item:"+position+" ",Toast.LENGTH_SHORT).show();
                kurangi(i);
            }
        });
//        --------------------------------------------------------------------

        tv_hasil = findViewById(R.id.tv_hasil);
    }

    public void cek(String kode)
    {
        ArrayList<LocalDBEncryption.item> alls = adapter.getAll();
        for(int i=0; i<alls.size(); i++)
        {
            LocalDBEncryption.item itm = alls.get(i);
            if(itm._kode == kode)
            {
                itm._stock += 1;
                adapter.setStock(i, itm);
                break;
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void kurangi(int i)
    {
        sm = sm - adapter1.getJual(i);
        LocalDBEncryption.item itm = adapter1.getItem(i);
        cek(itm._kode);

        adapter1.remove(adapter1.getItem(i));
        adapter1.notifyDataSetChanged();

        tv_hasil.setText(String.valueOf(sm));
    }

    public void tambahi(int i)
    {
        LocalDBEncryption.item itm = adapter.getItem(i);
        if(itm._stock > itm._minStock)
        {
            itm._stock -= 1;
            adapter.setStock(i, itm);
            adapter.notifyDataSetChanged();

            adapter1.add(itm);
            adapter1.notifyDataSetChanged();

            sm = sm + adapter.getJual(i);
            tv_hasil.setText(String.valueOf(sm));
        }
        else
        {
            Toast.makeText(Kasir.this,"Stock Kurang", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetList(View view)
    {
        adapter1.setListAdapter();
        adapter1.notifyDataSetChanged();

        sm = 0;
        tv_hasil.setText(String.valueOf(sm));
    }

    public void ok(View view)
    {
        List<LocalDBEncryption.item> alls;
        alls = myDBCONN.readAllData();
        for(int i=0; i<alls.size(); i++)
        {
            LocalDBEncryption.item itm1 = alls.get(i);
            LocalDBEncryption.item itm2 = adapter.getItem(i);

            if( itm1._stock - itm2._stock != 0 )
            {
                myDBCONN.createHistory(itm1._kode, itm1._nama, itm1._hargaModal, itm1._hargaJual, itm1._stock - itm2._stock);
                myDBCONN.updateData(String.valueOf(itm2._id), itm2._kode, itm2._nama, itm2._keterangan, itm2._hargaModal, itm2._hargaJual, itm2._stock, itm2._minStock);
            }
        }

        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }
}
