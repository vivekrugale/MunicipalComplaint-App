package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;

public class RegisterAuthority extends AppCompatActivity {
    EditText textfullname, textemail, textpassword, textphone, textward;
    Button btnregister;
    TextView loginTextView, authorityTextView;
    ProgressBar progressBar2;

    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_authority);

        textfullname = findViewById(R.id.textafullname);
        textemail = findViewById(R.id.textaemail);
        textpassword = findViewById(R.id.textapassword);
        textphone = findViewById(R.id.textaphone);
        textward = findViewById(R.id.textward);
        btnregister = findViewById(R.id.btnaregister);
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        authorityTextView = (TextView) findViewById(R.id.textViewBack);
        progressBar2 = findViewById(R.id.progressBar2);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = textemail.getText().toString().trim();
                String password = textpassword.getText().toString().trim();
                final String fullname = textfullname.getText().toString();
                final String phone = textphone.getText().toString();
                final String ward = textward.getText().toString();

                fAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();

                if (TextUtils.isEmpty(fullname)){
                    textpassword.setError("Please enter your name");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    textemail.setError("Plese enter the email");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    textpassword.setError("Please enter the password");
                    return;
                }

                if (password.length() < 6){
                    textpassword.setError("Password must be at least 6 characters long");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    textpassword.setError("Please enter your phone number");
                    return;
                }

                if (TextUtils.isEmpty(ward)){
                    textpassword.setError("Please enter your ward number");
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterAuthority.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            final DatabaseReference myref = firebaseDatabase.getReference("Authority").child(fullname);
                            myref.child("Name").setValue(fullname);
                            myref.child("Email").setValue(email);
                            myref.child("Phone").setValue(phone);
                            myref.child("Ward").setValue(ward);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final String nodeName = textfullname.getText().toString();
                            String nodeEmail = textemail.getText().toString();
                            nodeEmail = nodeEmail.replace(".", "");

                            final DatabaseReference LogRef = database.getReference("Login").child(nodeEmail);
                            LogRef.child("Authority").setValue("Authority");
                            LogRef.child("AuthNode").setValue(nodeName);

                            SharedPreferences sp = getSharedPreferences("AUTHDATA", MODE_PRIVATE);
                            final SharedPreferences.Editor editAuth = sp.edit();
                            editAuth.putString("authNode", nodeName);
                            editAuth.putString("authEmail", nodeEmail);
                            editAuth.apply();

                            startActivity(new Intent(getApplicationContext(), authoritySide.class));

                            Toast.makeText(RegisterAuthority.this,"Authority Registered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterAuthority.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        authorityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}