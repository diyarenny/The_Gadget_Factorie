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

public class AdminForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button admin_reset_btn;
    private EditText admin_resetPassword;
    private ProgressBar resetProgressBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        admin_resetPassword = (EditText) findViewById(R.id.admin_resetPassword);
        admin_reset_btn = (Button) findViewById(R.id.admin_reset_btn);
        resetProgressBr = (ProgressBar) findViewById(R.id.progressBar3);

        admin_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = admin_resetPassword.getText().toString().trim();

                if(email.isEmpty()){
                    admin_resetPassword.setError("Email is required");
                    admin_resetPassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    admin_resetPassword.setError("Please provide a valid email");
                    admin_resetPassword.requestFocus();
                    return;
                }
                resetProgressBr.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminForgotPasswordActivity.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AdminLoginActivity.class));
                        }
                        else{
                            Toast.makeText(AdminForgotPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

    }
}