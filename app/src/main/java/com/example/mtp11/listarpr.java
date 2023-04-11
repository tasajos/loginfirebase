package com.example.mtp11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class listarpr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarpr);

        this.setTitle("Listado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}