package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class authoritySide extends AppCompatActivity {

    private myadapterForComplaints.RecyclerViewClickListener listener;
    RecyclerView recviewComplaints;
    myadapterForComplaints adapterComplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_side);

        recviewComplaints = (RecyclerView) findViewById(R.id.recviewComplaints);
        recviewComplaints.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
    }

    private void setAdapter() {
        setOnclickListener();

        SharedPreferences sp = getSharedPreferences("AUTHDATA", MODE_PRIVATE);

        final String authName = sp.getString("authNode","");

        FirebaseRecyclerOptions<modelComplaints> options =
                new FirebaseRecyclerOptions.Builder<modelComplaints>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Complaints"), modelComplaints.class)
                        .build();
        adapterComplaints = new myadapterForComplaints(options, listener);
        recviewComplaints.setLayoutManager(new LinearLayoutManager(this));
        recviewComplaints.setAdapter(adapterComplaints);
    }

    private void setOnclickListener() {
        listener = new myadapterForComplaints.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, final String citizen) {

                Toast.makeText(authoritySide.this, "Acknowledged", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterComplaints.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterComplaints.stopListening();
    }

}