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

public class AdminLoginActivity extends AppCompatActivity {
    //variables
    private EditText admin_email;  //email
    private EditText admin_pass;  //password
    private Button admin_login_btn;   //login button
    private Button admin_register_btn;  //create new account button
    private ProgressBar loginProgressBr;   //login progress bar
    private TextView admin_forgotPass;   //forgot password

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        admin_email = (EditText) findViewById(R.id.admin_email);
        admin_pass = (EditText) findViewById(R.id.admin_pass);
        admin_login_btn = (Button) findViewById(R.id.admin_login_btn);
        admin_register_btn = (Button) findViewById(R.id.admin_register_btn);
        loginProgressBr = (ProgressBar) findViewById(R.id.progressBar);
        admin_forgotPass = (TextView) findViewById(R.id.admin_forgotPass);

        //create new account btn, user sent to register activity
        admin_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminRegisterActivity.class));
            }
        });

        admin_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminForgotPasswordActivity.class));
            }
        });

        //login to the app
        admin_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = admin_email.getText().toString().trim();
                String pass = admin_pass.getText().toString().trim();

                if (email.isEmpty()) {
                    admin_email.setError("Email is Required");
                    admin_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    admin_email.setError("Please provide a valid email");
                    admin_email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    admin_pass.setError("Password is Required");
                    admin_pass.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    admin_pass.setError("Min password length should be 6 characters");
                    admin_pass.requestFocus();
                    return;
                }

                loginProgressBr.setVisibility(View.VISIBLE);
                //authenticates the user
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                loginProgressBr.setVisibility(View.GONE);
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(AdminLoginActivity.this, "Check your email to verify account", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(AdminLoginActivity.this, "Login Failed" + errorMessage, Toast.LENGTH_SHORT).show();
                            loginProgressBr.setVisibility(View.GONE);
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