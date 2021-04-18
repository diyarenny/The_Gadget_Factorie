package com.example.thegadgetfactorie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thegadgetfactorie.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerRegisterActivity extends AppCompatActivity {
    //variables
    public static final String TAG = "TAG";
    private EditText customer_reg_username; //register username
    private EditText customer_reg_email;  //register email
    private EditText customer_reg_pass;  //register password
    private EditText customer_reg_confirm_pass;  //register confirm password
    private ProgressBar regProgressBr;  //reg progress bar

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        customer_reg_username = (EditText) findViewById(R.id.customer_reg_username);
        customer_reg_email = (EditText) findViewById(R.id.customer_reg_email);
        customer_reg_pass = (EditText) findViewById(R.id.customer_reg_pass);
        customer_reg_confirm_pass = (EditText) findViewById(R.id.customer_reg_confirm_pass);
        Button customer_reg_btn = (Button) findViewById(R.id.customer_reg_btn);
        Button customer_reg_login_btn = (Button) findViewById(R.id.customer_reg_login_btn);
        regProgressBr = (ProgressBar) findViewById(R.id.progressBar2);

        //already have an account - back to login
        customer_reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
            }
        });

        //register a new account
        customer_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = customer_reg_username.getText().toString().trim();
                final String email = customer_reg_email.getText().toString().trim();
                final String pass = customer_reg_pass.getText().toString().trim();
                final String confirmPass = customer_reg_confirm_pass.getText().toString().trim();

                if (username.isEmpty()) {
                    customer_reg_username.setError("Username is Required");
                    customer_reg_username.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    customer_reg_email.setError("Email is Required");
                    customer_reg_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    customer_reg_email.setError("Please provide a valid email");
                    customer_reg_email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    customer_reg_pass.setError("Password is Required");
                    customer_reg_pass.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    customer_reg_pass.setError("Min password length should be 6 characters");
                    customer_reg_pass.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    customer_reg_confirm_pass.setError("Confirm Password is Required");
                    customer_reg_confirm_pass.requestFocus();
                    return;
                }
                if (!confirmPass.equals(pass)) {
                    customer_reg_confirm_pass.setError("Confirm password must match Password");
                    customer_reg_confirm_pass.requestFocus();
                    return;
                }

                regProgressBr.setVisibility(View.GONE);
                //creates a new user into the database with an email and password
                mAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(username, email, pass);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        regProgressBr.setVisibility(View.VISIBLE);
                                        Toast.makeText(CustomerRegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(CustomerRegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            regProgressBr.setVisibility(View.GONE);
                            Toast.makeText(CustomerRegisterActivity.this, "Failed to register, Try again!", Toast.LENGTH_LONG).show();
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