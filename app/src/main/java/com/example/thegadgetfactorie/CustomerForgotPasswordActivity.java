package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class CustomerForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button customer_reset_btn;
    private EditText customer_resetPassword;
    private ProgressBar resetProgressBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        customer_resetPassword = (EditText) findViewById(R.id.customer_resetPassword);
        customer_reset_btn = (Button) findViewById(R.id.customer_reset_btn);
        resetProgressBr = (ProgressBar) findViewById(R.id.progressBar3);

        customer_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = customer_resetPassword.getText().toString().trim();

                if(email.isEmpty()){
                    customer_resetPassword.setError("Email is required");
                    customer_resetPassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    customer_resetPassword.setError("Please provide a valid email");
                    customer_resetPassword.requestFocus();
                    return;
                }
                resetProgressBr.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CustomerForgotPasswordActivity.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
                        }
                        else{
                            Toast.makeText(CustomerForgotPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }
}