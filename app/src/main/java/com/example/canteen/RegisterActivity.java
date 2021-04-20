package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmail, mPassword,mConfirmPassword;
    Button mRegisterBtn, mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    // FirebaseFirestore fStore;
    String userID;
    TextView loginLinkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmedPassword);
        mRegisterBtn = findViewById(R.id.registerButton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mLoginBtn = findViewById(R.id.loginButton);



//        if (fAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
//        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String _email = mEmail.getText().toString().trim();
                final String _password = mPassword.getText().toString().trim();
                final String _confirm_password = mConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(_email)) {
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(_password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(_confirm_password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                if (!_confirm_password.equals(_password)) {
                    mPassword.setError("Password and confirm password does not match.");
                    return;
                }

                System.out.println("1===============");
                Log.i("test1", "before");

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "User created", LENGTH_SHORT);
                            toast.show();

                            userID = fAuth.getCurrentUser().getUid();


                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            System.out.println("2============");
                            Log.i("test2", "in");

                        } else {
                            Toast toast = Toast.makeText(RegisterActivity.this, task.getException().getMessage(), LENGTH_SHORT);
                            toast.show();
                            System.out.println("3=============");

                            Log.i("test3x", task.getException().getMessage());
                            //  toast = Toast.makeText(getApplicationContext(), _email, LENGTH_SHORT);

                        }
                    }
                });

                // Toast toast = Toast.makeText(getApplicationContext(), _email, LENGTH_SHORT);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
}