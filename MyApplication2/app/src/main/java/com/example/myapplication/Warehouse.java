package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Warehouse extends AppCompatActivity {
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        et_search = findViewById(R.id.et_search);
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
