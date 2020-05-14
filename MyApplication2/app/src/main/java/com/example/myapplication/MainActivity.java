package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    LocalDBEncryption myDBCONN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toWarehouse(View view)
    {
        Intent intent = new Intent(this, Warehouse.class);
        startActivity(intent);
    }

    public void toKasir(View view)
    {
        Intent intent = new Intent(this, Kasir.class);
        startActivity(intent);
    }
}
