package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginButton;
    Button registerLinkButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        fAuth = FirebaseAuth.getInstance();
        registerLinkButton = findViewById(R.id.registerLinkButton);

//        if (fAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailString)) {
                    email.setError("Please enter your email address");
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Please enter your password");
                    return;
                }

                fAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Convert email into universityInitials
                            String[] universityInitials = emailString.split("@");
                            final String universityInitialsString = universityInitials[0];

                            //Start MenuActivity
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.putExtra("universityInitials", universityInitialsString);
                            intent.putExtra("email", emailString);
                            startActivity(intent);

                            //Show success message
                            Toast toast = Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            //show failure message
                            Toast toast = Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        });

        registerLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Nothing happens
    }
}