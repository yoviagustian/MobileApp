package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateItem extends AppCompatActivity {
    String id;
    EditText et_kode, et_nama, et_keterangan, et_hargaJual, et_hargaModal, et_stock, et_minStock;
    LocalDBEncryption myDBCONN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateitem);
        myDBCONN = new LocalDBEncryption(this);

        et_kode       = findViewById(R.id.et_kode);
        et_nama       = findViewById(R.id.et_nama);
        et_keterangan = findViewById(R.id.et_keterangan);
        et_hargaJual  = findViewById(R.id.et_hargaJual);
        et_hargaModal = findViewById(R.id.et_hargaModal);
        et_stock      = findViewById(R.id.et_stock);
        et_minStock   = findViewById(R.id.et_minStock);

        id = (String) getIntent().getSerializableExtra("id");
        LocalDBEncryption.item itm = myDBCONN.readData(id);

        et_kode.setText(itm._kode);
        et_nama.setText(itm._nama);
        et_keterangan.setText(itm._keterangan);
        et_hargaJual.setText(String.valueOf(itm._hargaJual));
        et_hargaModal.setText(String.valueOf(itm._hargaModal));
        et_stock.setText(String.valueOf(itm._stock));
        et_minStock.setText(String.valueOf(itm._minStock));
    }

    public void updateItem(View view) {
        String kode = et_kode.getText().toString();
        String nama = et_nama.getText().toString();
        String keterangan = et_keterangan.getText().toString();

        Integer hargaJual  = Integer.parseInt(et_hargaJual.getText().toString());
        Integer hargaModal = Integer.parseInt(et_hargaModal.getText().toString());
        Integer stock      = Integer.parseInt(et_stock.getText().toString());
        Integer minStock   = Integer.parseInt(et_minStock.getText().toString());

        boolean f = myDBCONN.updateData(id, kode, nama, keterangan, hargaModal, hargaJual, stock, minStock);;

        Intent intent = new Intent(this, Warehouse.class);
        Intent intent1 = new Intent(this, MainActivity.class);
        if(f) startActivity(intent);
        else startActivity(intent1);
    }

    public void deleteItem(View view) {
        boolean f = myDBCONN.deleteData(id);;

        Intent intent = new Intent(this, Warehouse.class);
        Intent intent1 = new Intent(this, MainActivity.class);
        if(f) startActivity(intent);
        else startActivity(intent1);
    }
}
