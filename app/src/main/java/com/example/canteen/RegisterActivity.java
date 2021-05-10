package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, email, password, confirmPassword, studentOrStaff;
    Button registerButton, goBackToLogin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String userID;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmedPassword);
        studentOrStaff = findViewById(R.id.studentOrStaff);
        registerButton = findViewById(R.id.registerButton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        goBackToLogin = findViewById(R.id.goBackToLoginPage);
        ref = FirebaseDatabase.getInstance().getReference("/customer");

        /*
        if (fAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }
         */

        //Handle login
        goBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //Handle Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameString = firstName.getText().toString().trim();
                final String lastNameString = lastName.getText().toString().trim();
                final String emailString = email.getText().toString().trim();
                final String passwordString = password.getText().toString().trim();
                final String confirmPasswordString = confirmPassword.getText().toString().trim();
                final String studentOrStaffString = studentOrStaff.getText().toString().trim();

                if (TextUtils.isEmpty(firstNameString)) {
                    firstName.setError("Please enter your name");
                    return;
                }

                if (TextUtils.isEmpty(lastNameString)) {
                    lastName.setError("Please enter your last name");
                    return;
                }

                if (TextUtils.isEmpty(emailString)) {
                    email.setError("Please enter an email address");
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Please enter a password");
                    return;
                }

                if (passwordString.length() < 6) {
                    password.setError("Password must at least 6 characters long");
                    return;
                }

                if (TextUtils.isEmpty(confirmPasswordString)) {
                    confirmPassword.setError("Please confirm your password");
                    return;
                }

                if (!confirmPasswordString.equals(passwordString)) {
                    password.setError("Password and confirmed password does not match.");
                    confirmPassword.setError("Password and confirmed password does not match.");
                    return;
                }

                if(!studentOrStaffString.equalsIgnoreCase("student") && !studentOrStaffString.equalsIgnoreCase("staff")) {
                    studentOrStaff.setError("Please enter student or staff");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //get userID
                            userID = fAuth.getCurrentUser().getUid();

                            //Create a customer object to store data in database
                            Customer customer = new Customer();
                            customer.setUserId(userID);
                            customer.setFirstName(firstNameString);
                            customer.setLastName(lastNameString);
                            customer.setEmail(emailString);
                            String[] universityInitials = emailString.split("@");
                            final String universityInitialsString = universityInitials[0];
                            customer.setUniversityInitials(universityInitialsString);
                            customer.setType(studentOrStaffString);
                            ref.child(universityInitialsString).setValue(customer);

                            //Start LoginActivity
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                            //Show success message
                            Toast toast = Toast.makeText(getApplicationContext(), "User created", LENGTH_SHORT);
                            toast.show();

                        } else {
                            //Show failure message
                            Toast toast = Toast.makeText(RegisterActivity.this, task.getException().getMessage(), LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Nothing happens
    }
}