package com.example.complaintapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private myadapterForServices.RecyclerViewClickListener listener;
    RecyclerView recview;
    myadapterForServices adapterServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recview = (RecyclerView) findViewById(R.id.recview);
        setAdapter();
    }

    private void setAdapter() {
        setOnclickListener();
        FirebaseRecyclerOptions<modelServices> options =
                new FirebaseRecyclerOptions.Builder<modelServices>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Services"), modelServices.class)
                        .build();
        adapterServices = new myadapterForServices(options, listener);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapterServices);
    }

    private void setOnclickListener() {
        listener = new myadapterForServices.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, String nameService) {
//                SharedPreferences nsp = getSharedPreferences("Navigation", MODE_PRIVATE);
//                final SharedPreferences.Editor NAVED = nsp.edit();
//                NAVED.putString("patNode", nameService);
//                NAVED.apply();

                Intent i = getIntent();
                String authName = i.getStringExtra("authName");
                String citName = i.getStringExtra("citName");
                String citEmail = i.getStringExtra("citeEmail");

                SharedPreferences sp = getSharedPreferences("CITDATA", MODE_PRIVATE);
                final SharedPreferences.Editor editSnap = sp.edit();
                editSnap.putString("ServiceName", nameService);
                editSnap.apply();

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference myRef = database.getReference("Login").child("aa@gmailcom");
//                myRef.child("ServiceNode").setValue(nameService);

                //Intent intent = new Intent(MainActivity.this, viewf.class);
                Intent intent = new Intent(MainActivity.this, viewForAuth.class);
                intent.putExtra("ServiceName", nameService);
                intent.putExtra("authName", authName);
                intent.putExtra("citName", citName);
                intent.putExtra("citEmail", citEmail);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterServices.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterServices.stopListening();
    }
}