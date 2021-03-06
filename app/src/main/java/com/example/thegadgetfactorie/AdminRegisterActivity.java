package com.example.thegadgetfactorie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class AdminRegisterActivity extends AppCompatActivity {
    //variables
    public static final String TAG = "TAG";
    private EditText admin_reg_username; //register username
    private EditText admin_reg_email;  //register email
    private EditText admin_reg_pass;  //register password
    private EditText admin_reg_confirm_pass;  //register confirm password
    private ProgressBar regProgressBr;  //reg progress bar

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        admin_reg_username = (EditText) findViewById(R.id.admin_reg_username);
        admin_reg_email = (EditText) findViewById(R.id.admin_reg_email);
        admin_reg_pass = (EditText) findViewById(R.id.admin_reg_pass);
        admin_reg_confirm_pass = (EditText) findViewById(R.id.admin_reg_confirm_pass);
        Button admin_reg_btn = (Button) findViewById(R.id.admin_reg_btn);
        Button admin_reg_login_btn = (Button) findViewById(R.id.admin_reg_login_btn);
        regProgressBr = (ProgressBar) findViewById(R.id.progressBar2);

        //already have an account - back to login
        admin_reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminLoginActivity.class));
            }
        });

        //register a new account
        admin_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = admin_reg_username.getText().toString().trim();
                final String email = admin_reg_email.getText().toString().trim();
                final String pass = admin_reg_pass.getText().toString().trim();
                final String confirmPass = admin_reg_confirm_pass.getText().toString().trim();

                if (username.isEmpty()) {
                    admin_reg_username.setError("Username is Required");
                    admin_reg_username.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    admin_reg_email.setError("Email is Required");
                    admin_reg_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    admin_reg_email.setError("Please provide a valid email");
                    admin_reg_email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    admin_reg_pass.setError("Password is Required");
                    admin_reg_pass.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    admin_reg_pass.setError("Min password length should be 6 characters");
                    admin_reg_pass.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    admin_reg_confirm_pass.setError("Confirm Password is Required");
                    admin_reg_confirm_pass.requestFocus();
                    return;
                }
                if (!confirmPass.equals(pass)) {
                    admin_reg_confirm_pass.setError("Confirm password must match Password");
                    admin_reg_confirm_pass.requestFocus();
                    return;
                }

                regProgressBr.setVisibility(View.GONE);

                //creates a new user into the database with an email and password
                mAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Admin admin = new Admin(username, email, pass);

                            FirebaseDatabase.getInstance().getReference("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        regProgressBr.setVisibility(View.VISIBLE);
                                        Toast.makeText(AdminRegisterActivity.this, "Admin has been registered successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(AdminRegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            regProgressBr.setVisibility(View.GONE);
                            Toast.makeText(AdminRegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    //checks if the user already has an account, if yes then user send to the main activity
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}