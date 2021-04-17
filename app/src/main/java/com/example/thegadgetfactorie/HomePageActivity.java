package com.example.thegadgetfactorie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class HomePageActivity extends AppCompatActivity {

    private Button adminLogin, customerLogin;
    private ProgressBar progressBar0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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
}