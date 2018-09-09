package com.clbrain.kirizhik.epidetect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    Button signIn;
    Button signUp;
    Button men;
    Button women;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText mName;
    private EditText mNum;
    private EditText mParNum;
    private EditText mDOB;
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        signUp =findViewById(R.id.sign_up_btn);
        signIn =findViewById(R.id.sign_in_btn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Регистрация и тп
                register(mEmail.getText().toString(),mPassword.getText().toString());
            }
        });
        men = findViewById(R.id.btn_men);
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                men.setSelected(true);
                women.setSelected(false);
            }
        });
        women = findViewById(R.id.btn_women);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                women.setSelected(true);
                men.setSelected(false);
            }
        });
    }
    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Sign up is successful", Toast.LENGTH_SHORT).show();
                            startActivity((new Intent(getApplicationContext(),MainActivity.class)));
                        }
                    }
                });
    }
}
