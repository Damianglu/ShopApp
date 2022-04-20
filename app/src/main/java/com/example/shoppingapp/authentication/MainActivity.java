package com.example.shoppingapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.views.MainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText mloginEmailAddress, mloginPassword;
    private Button mloginButton, mexistingAccountButton;
    private TextView mforgotPasswordTextView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mloginEmailAddress = findViewById(R.id.loginEmailAddress);
        mloginPassword = findViewById(R.id.loginPassword);
        mloginButton = findViewById(R.id.loginButton);
        mexistingAccountButton = findViewById(R.id.existingAccountButton);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//        if(firebaseUser == null) {
//            startActivity(new Intent(MainActivity.this, Register.class));
//        }
        mexistingAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });

//        mforgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
//            }
//        });
        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mloginEmailAddress.getText().toString().trim();
                String password = mloginPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Login the user
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                               // checkEmailVerification();
                                if(task.getResult().getUser().isEmailVerified()){
                                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, MainPage.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Verify your email please", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Account does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","LOGIN ERROR" + e.getMessage());
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainPage.class));
                    finish();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);

            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Verify your email please", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}