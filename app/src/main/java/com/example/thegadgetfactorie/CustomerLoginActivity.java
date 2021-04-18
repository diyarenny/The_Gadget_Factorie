package com.example.thegadgetfactorie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerLoginActivity extends AppCompatActivity {
    //variables
    private EditText customer_email;  //email
    private EditText customer_pass;  //password
    private Button customer_login_btn;   //login button
    private Button customer_register_btn;  //create new account button
    private ProgressBar loginProgressBr;   //login progress bar
    private TextView forgotPass;   //forgot password

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        mAuth = FirebaseAuth.getInstance();

        customer_email = (EditText) findViewById(R.id.customer_email);
        customer_pass = (EditText) findViewById(R.id.customer_pass);
        customer_login_btn = (Button) findViewById(R.id.customer_login_btn);
        customer_register_btn = (Button) findViewById(R.id.customer_register_btn);
        loginProgressBr = (ProgressBar) findViewById(R.id.progressBar);
        forgotPass = (TextView) findViewById(R.id.forgotPass);

        //create new account btn, user sent to register activity
        customer_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerRegisterActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerForgotPasswordActivity.class));
            }
        });

        //login to the app
        customer_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = customer_email.getText().toString().trim();
                String pass = customer_pass.getText().toString().trim();

                if (email.isEmpty()) {
                    customer_email.setError("Email is Required");
                    customer_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    customer_email.setError("Please provide a valid email");
                    customer_email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    customer_pass.setError("Password is Required");
                    customer_pass.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    customer_pass.setError("Min password length should be 6 characters");
                    customer_pass.requestFocus();
                    return;
                }

                loginProgressBr.setVisibility(View.GONE);
                //authenticates the user
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()){
                                loginProgressBr.setVisibility(View.VISIBLE);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(CustomerLoginActivity.this, "Check your email to verify account", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            loginProgressBr.setVisibility(View.GONE);
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(CustomerLoginActivity.this, "Login Failed" + errorMessage, Toast.LENGTH_SHORT).show();
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