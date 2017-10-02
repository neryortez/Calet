package com.altechonduras.calet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.altechonduras.calet.R;
import com.altechonduras.calet.Utilities;
import com.altechonduras.calet.dialogs.DialogMGP;
import com.altechonduras.calet.objects.MGP;
import com.altechonduras.calet.views.AdaptadorMGP;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ActividadMGP extends AppCompatActivity {

    private AdaptadorMGP mMyAdapter;
    private com.google.firebase.database.Query mQuery;
    private ArrayList<String> mAdapterKeys;
    private ArrayList<MGP> mAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mgp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAdapterItems = new ArrayList<>();
        mAdapterKeys = new ArrayList<>();


        setupFirebase();
        setupRecyclerview();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogMGP(ActividadMGP.this).show();
            }
        });
    }

    private void setupFirebase() {
        String firebaseLocation = Utilities.getMGPdir(getApplicationContext());
        mQuery = FirebaseDatabase.getInstance().getReference(firebaseLocation);
        mQuery.keepSynced(true);
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mMyAdapter = new AdaptadorMGP(mQuery, mAdapterItems, mAdapterKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }


}
