package com.example.mtp11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mtp11.adapter.mtpadapter;
import com.example.mtp11.models.mtpcontroller;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listarpr extends AppCompatActivity {

    RecyclerView mRecycler;
    mtpadapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarpr);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewMTP);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = mFirestore.collection("mtpdb");

        FirestoreRecyclerOptions<mtpcontroller> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<mtpcontroller>().setQuery(query,mtpcontroller.class).build();


        mAdapter = new mtpadapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



        this.setTitle("Listado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}