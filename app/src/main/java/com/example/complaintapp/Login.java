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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText textemail, textpassword;
    Button btnlogin, btnbypassAuth;
    TextView createTextView;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textemail = findViewById(R.id.textemail);
        textpassword = findViewById(R.id.textpassword);
        btnlogin = findViewById(R.id.btnlogin);
        createTextView = findViewById(R.id.createTextView);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnbypassAuth = findViewById(R.id.btnbypassAuth);
        btnbypassAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), authoritySide.class));
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textemail.getText().toString().trim();
                String password = textpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    textemail.setError("Plese enter the email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    textpassword.setError("Please enter the password");
                    return;
                }

                if (password.length() < 6) {
                    textpassword.setError("Password must be at least 6 characters long");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //finish();

                            String loginEmail = textemail.getText().toString().trim();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            loginEmail = loginEmail.replace(".", "");
                            final DatabaseReference myRef = database.getReference("Login").child(loginEmail);

                            final String finalLoginEmail = loginEmail;
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.hasChild("Authority")) {

                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String authName = snapshot.child("AuthNode").getValue().toString();

                                                SharedPreferences sp = getSharedPreferences("AUTHDATA", MODE_PRIVATE);
                                                final SharedPreferences.Editor editAuth = sp.edit();
                                                editAuth.putString("authNode", authName);
                                                editAuth.putString("authEmail", finalLoginEmail);
                                                editAuth.apply();

                                                Intent intent = new Intent(Login.this, authoritySide.class);
                                                intent.putExtra("authName", authName);
                                                intent.putExtra("nodeEmail", finalLoginEmail);
                                                startActivity(intent);
                                                progressBar.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                    else {
                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String authName = snapshot.child("AuthNode").getValue().toString(); //This DocNode is not getting retrieved for some reason.
                                                String citName = snapshot.child("CitNode").getValue().toString();

                                                SharedPreferences sp = getSharedPreferences("CITDATA", MODE_PRIVATE);
                                                final SharedPreferences.Editor editSnap = sp.edit();
                                                editSnap.putString("authNode", authName);
                                                editSnap.putString("citNode", citName);
                                                editSnap.putString("citEmail", finalLoginEmail);
                                                editSnap.apply();

                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                intent.putExtra("authName", authName);
                                                intent.putExtra("citName", citName);
                                                intent.putExtra("citEmail", finalLoginEmail);
                                                startActivity(intent);
                                                progressBar.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}