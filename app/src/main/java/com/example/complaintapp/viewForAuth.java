package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewForAuth extends AppCompatActivity {
    RecyclerView recview;
    myadapter_for_cit adapter;
    FloatingActionButton fadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_auth);

        recview = (RecyclerView)findViewById(R.id.rcview);
        recview.setLayoutManager(new LinearLayoutManager(this));

//        SharedPreferences nsp = getSharedPreferences("Navigation", MODE_PRIVATE);
//        String docNode = nsp.getString("docNode", "");
//        String patNode = nsp.getString("patNode", "");

//        Intent i = getIntent();
//        String nodeEmail = i.getStringExtra("nodeEmail");

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference("Login").child("aa@gmail.com");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String docName = snapshot.child("DocNode").getValue().toString();
//                String patName = snapshot.child("PatNode").getValue().toString();
//
//                SharedPreferences sp = getSharedPreferences("Snapshot", MODE_PRIVATE); //because docName is not valid out of @Override
//                final SharedPreferences.Editor editSnap = sp.edit();
//                editSnap.putString("docNode", docName);
//                editSnap.putString("patNode",patName);
//                editSnap.apply();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        SharedPreferences sp = getSharedPreferences("CITDATA", MODE_PRIVATE);
        final String authName = sp.getString("authNode", "");
        final String ServiceName = sp.getString("ServiceName", "");
        final String citNode = sp.getString("citNode", "");

        Intent i = getIntent();
        //final String authName = i.getStringExtra("authName");
        String citName = i.getStringExtra("citName");
        String citEmail = i.getStringExtra("citeEmail");
        //final String ServiceName = i.getStringExtra("ServiceName");

        FirebaseRecyclerOptions<model_cit_recview> options =
                new FirebaseRecyclerOptions.Builder<model_cit_recview>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Citizens").child(citNode).child("Complaints").child(ServiceName), model_cit_recview.class)
                        .build();

        adapter = new myadapter_for_cit(options);
        recview.setAdapter(adapter);

        fadd = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewForAuth.this, addComplaint.class);
                intent.putExtra("authName", authName);
                intent.putExtra("ServiceName", ServiceName);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}