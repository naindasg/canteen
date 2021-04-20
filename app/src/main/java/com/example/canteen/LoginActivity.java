package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    EditText mEmail,mPassword;
    Button mLoginBtn;
    Button mRegisterLinkButton;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginButton);
        fAuth = FirebaseAuth.getInstance();
        mRegisterLinkButton = findViewById(R.id.registerLinkButton);

//                if (fAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
//        }


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mEmail.setError("Password is required");
                    return;
                }

                if(password.length()<5){
                    mEmail.setError("Password must be >=5 characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            Log.d("TAG", "onComplete: " + email);

                        }else{
                            Toast toast = Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

            }
        });


        mRegisterLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Log.d("TAG", "onComplete: " + "Lets register");
                startActivity(intent);


            }
        });


    }


}