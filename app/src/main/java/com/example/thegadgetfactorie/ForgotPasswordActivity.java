package com.example.thegadgetfactorie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button resetButton;
    private EditText resetPassInfo;
    private ProgressBar resetProgressBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        resetPassInfo = (EditText) findViewById(R.id.resetPassword);
        resetButton = (Button) findViewById(R.id.reset_btn);
        resetProgressBr = (ProgressBar) findViewById(R.id.progressBar3);

    }
}