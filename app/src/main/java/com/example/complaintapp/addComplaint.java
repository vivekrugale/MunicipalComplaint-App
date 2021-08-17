package com.example.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addComplaint extends AppCompatActivity {
    EditText editComplaint,editArea;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        editComplaint = findViewById(R.id.editComplaint);
        editArea = findViewById(R.id.editArea);
        btnAdd = findViewById(R.id.btnAddCom);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String complaint = editComplaint.getText().toString().trim();
                String area = editArea.getText().toString().trim();

                dataholder_complaintData obj= new dataholder_complaintData(complaint,area);

                Intent i = getIntent();
                //String authName = i.getStringExtra("authName");
                //String ServiceName = i.getStringExtra("ServiceName");

                SharedPreferences sp = getSharedPreferences("CITDATA", MODE_PRIVATE);
                final SharedPreferences.Editor edit = sp.edit();
                final String authName = sp.getString("authNode", "");
                final String ServiceName = sp.getString("ServiceName", "");
                final String citNode = sp.getString("citNode", "");
                edit.putString("area", area);
                String ComplaintNode = ServiceName + area;

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference node = db.getReference("Authority").child(authName).child("Complaints").child(ComplaintNode);

                node.child("complaint").setValue(complaint);
                node.child("area").setValue(area);
                node.child("user").setValue(citNode);
                node.child("field").setValue(ServiceName);
                editArea.setText("");
                editComplaint.setText("");

                DatabaseReference citRef = db.getReference("Authority").child(authName).child("Citizens").child(citNode).child("Complaints").child(ServiceName).child("node");
                citRef.child("complaint").setValue(complaint);
                citRef.child("area").setValue(area);

                Toast.makeText(getApplicationContext(),"Complaint Added",Toast.LENGTH_LONG).show();

            }
        });

    }
}