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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText textfullname, textemail, textpassword, textphone;
    Button btnregister, btnbypass2alist;
    TextView loginTextView, authorityTextView;
    ProgressBar progressBar2;

    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textfullname = findViewById(R.id.textfullname);
        textemail = findViewById(R.id.textemail);
        textpassword = findViewById(R.id.textpassword);
        textphone = findViewById(R.id.textphone);
        btnregister = findViewById(R.id.btnregister);
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        authorityTextView = (TextView) findViewById(R.id.textViewAuthority);
        progressBar2 = findViewById(R.id.progressBar2);

        btnbypass2alist = findViewById(R.id.btnbypass2alist);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = textemail.getText().toString().trim();
                String password = textpassword.getText().toString().trim();
                final String fullname = textfullname.getText().toString();
                final String phone = textphone.getText().toString();

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

                progressBar2.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            SharedPreferences sp = getSharedPreferences("UsersData", MODE_PRIVATE);
                            final SharedPreferences.Editor Edit = sp.edit();

//                            final DatabaseReference myref = firebaseDatabase.getReference("Users");
//                            myref.child("Name").setValue(fullname);
//                            myref.child("Email").setValue(email);
//                            myref.child("Phone").setValue(phone);


                            Edit.putString("UserFullname", fullname);
                            Edit.putString("UserEmail", email);
                            Edit.putString("UserPhone", phone);
                            Edit.apply();

                            startActivity(new Intent(getApplicationContext(), AuthorityList.class));

                            Toast.makeText(Register.this,"Registered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        btnbypass2alist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AuthorityList.class));
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
                startActivity(new Intent(getApplicationContext(), RegisterAuthority.class));
            }
        });

    }
}