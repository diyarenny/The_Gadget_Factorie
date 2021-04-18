package com.example.thegadgetfactorie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    private Button adminLogin, customerLogin;
    private ProgressBar progressBar0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        adminLogin = (Button) findViewById(R.id.admin_login);
        customerLogin = (Button) findViewById(R.id.customer_login);
        progressBar0 = (ProgressBar) findViewById(R.id.progressBar0);

        progressBar0.setVisibility(View.GONE);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar0.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), AdminLoginActivity.class));
            }
        });

        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar0.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
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