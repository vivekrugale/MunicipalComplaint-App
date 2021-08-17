package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorityList extends AppCompatActivity {

    private myadapter.RecyclerViewClickListener listener;
    RecyclerView recview;
    myadapter adapter;

    TextView bypass2main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_list);

        bypass2main = findViewById(R.id.textView6);

        bypass2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recview = (RecyclerView) findViewById(R.id.recviewd);
        recview.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
    }

    private void setAdapter() {
        setOnclickListener();
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Authority"), model.class)
                        .build();
        adapter = new myadapter(options, listener);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);
    }

    private void setOnclickListener() {
        listener = new myadapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, final String name) {

                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                SharedPreferences sp = getSharedPreferences("UsersData", MODE_PRIVATE);

                final String fullname = sp.getString("UserFullname","");
                final String[] email = {sp.getString("UserEmail", "")};
                String phone = sp.getString("UserPhone","");

//                SharedPreferences nsp = getSharedPreferences("Navigation", MODE_PRIVATE);
//                final SharedPreferences.Editor NAVED = nsp.edit();
//                NAVED.putString("loginRole",role);
//                NAVED.putString("VFPdocNode", name);
//                NAVED.putString("VFPpatNode", fullname);
//                NAVED.apply();

                users info = new users(fullname, email[0], phone);

                FirebaseDatabase.getInstance().getReference("Authority")
                        .child(name).child("Citizens").child(fullname)
                        .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        email[0] = email[0].replace(".", "");
                        final DatabaseReference myRef = database.getReference("Login").child(email[0]);
                        myRef.child("Citizen").setValue("Citizen");
                        myRef.child("AuthNode").setValue(name);
                        myRef.child("CitNode").setValue(fullname);

                        SharedPreferences citsp = getSharedPreferences("CITDATA", MODE_PRIVATE);
                        final SharedPreferences.Editor editSnap = citsp.edit();
                        editSnap.putString("authNode", name);
                        editSnap.putString("citNode", fullname);
                        editSnap.putString("citEmail", email[0]);
                        editSnap.apply();

                        Intent intent = new Intent(AuthorityList.this, MainActivity.class);
                        intent.putExtra("AuthName", name);
                        intent.putExtra("CitName", fullname);
                        intent.putExtra("citEmail", email);
                        startActivity(intent);

                        Toast.makeText(AuthorityList.this, "Registered", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
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