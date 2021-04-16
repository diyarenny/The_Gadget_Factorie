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

public class LoginActivity extends AppCompatActivity {
    //variables
    private EditText loginEmail;  //email
    private EditText loginPassword;  //password
    private Button loginBtn;   //login button
    private Button createNewAccountBtn;  //create new account button
    private ProgressBar loginProgressBr;   //login progress bar
    private TextView forgotPass;   //forgot password

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = (EditText) findViewById(R.id.reg_email);
        loginPassword = (EditText) findViewById(R.id.reg_confirm_pass);
        loginBtn = (Button) findViewById(R.id.login_btn);
        createNewAccountBtn = (Button) findViewById(R.id.login_reg_btn);
        loginProgressBr = (ProgressBar) findViewById(R.id.progressBar);
        forgotPass = (TextView) findViewById(R.id.forgotPass);

        //create new account btn, user sent to register activity
        createNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        //login to the app
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String pass = loginPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    loginEmail.setError("Email is Required");
                    loginEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loginEmail.setError("Please provide a valid email");
                    loginEmail.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    loginPassword.setError("Password is Required");
                    loginPassword.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    loginPassword.setError("Min password length should be 6 characters");
                    loginPassword.requestFocus();
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
                                Toast.makeText(LoginActivity.this, "Check your email to verify account", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Login Failed" + errorMessage, Toast.LENGTH_SHORT).show();
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