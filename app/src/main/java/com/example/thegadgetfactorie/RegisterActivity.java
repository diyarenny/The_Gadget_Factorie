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

public class RegisterActivity extends AppCompatActivity {
    //variables
    public static final String TAG = "TAG";
    private EditText regUsername; //register username
    private EditText regEmail;  //register email
    private EditText regPassword;  //register password
    private EditText regConfirmPassword;  //register confirm password
    private ProgressBar regProgressBr;  //reg progress bar

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        regUsername = (EditText) findViewById(R.id.reg_username);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regPassword = (EditText) findViewById(R.id.reg_pass);
        regConfirmPassword = (EditText) findViewById(R.id.reg_confirm_pass);
        Button regBtn = (Button) findViewById(R.id.reg_btn);
        Button regLoginBtn = (Button) findViewById(R.id.reg_login_btn);
        regProgressBr = (ProgressBar) findViewById(R.id.progressBar2);

        //already have an account - back to login
        regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        //register a new account
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = regUsername.getText().toString().trim();
                final String email = regEmail.getText().toString().trim();
                final String pass = regPassword.getText().toString().trim();
                final String confirmPass = regConfirmPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    regUsername.setError("Username is Required");
                    regUsername.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    regEmail.setError("Email is Required");
                    regEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    regEmail.setError("Please provide a valid email");
                    regEmail.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    regPassword.setError("Password is Required");
                    regPassword.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    regPassword.setError("Min password length should be 6 characters");
                    regPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    regConfirmPassword.setError("Confirm Password is Required");
                    regConfirmPassword.requestFocus();
                    return;
                }
                if (!confirmPass.equals(pass)) {
                    regConfirmPassword.setError("Confirm password must match Password");
                    regConfirmPassword.requestFocus();
                    return;
                }

                regProgressBr.setVisibility(View.VISIBLE);

                //creates a new user into the database with an email and password
                mAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        regProgressBr.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
                                        regProgressBr.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
                            regProgressBr.setVisibility(View.GONE);
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