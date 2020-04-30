package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Kasir extends AppCompatActivity {
    EditText et_bil1, et_bil2;
    TextView tv_hasil;

    LocalDBEncryption myDBCONN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir);

        et_bil1 = findViewById(R.id.et_bil1);
        et_bil2 = findViewById(R.id.et_bil2);
        tv_hasil = findViewById(R.id.tv_hasil);

        myDBCONN = new LocalDBEncryption(this);
    }

    public void hitung(View view)
    {
        double bil1 = Double.parseDouble(et_bil1.getText().toString());
        double bil2 = Double.parseDouble(et_bil1.getText().toString());
        double hasil = bil1+bil2 ;

        tv_hasil.setText(String.valueOf(myDBCONN.readData("tes")));
    }
}
