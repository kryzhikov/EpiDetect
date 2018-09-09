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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FirebaseDatabase mFbdb;
    private DatabaseReference mDbref;
    private String sex;
    public String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mName=findViewById(R.id.fio);
        mNum=findViewById(R.id.emgnum);
        mParNum=findViewById(R.id.parnum);
        mDOB=findViewById(R.id.dob);
        mEmail=(EditText)findViewById(R.id.mail);
        mPassword=(EditText)findViewById(R.id.password);
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
                SignUp(mEmail.getText().toString(),mPassword.getText().toString());
            }
        });
        men = findViewById(R.id.btn_men);
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                men.setSelected(true);
                women.setSelected(false);
                sex="Мужской";
            }
        });
        women = findViewById(R.id.btn_women);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                women.setSelected(true);
                men.setSelected(false);
                sex="Женский";
            }
        });
    }
    private void SignUp(String email, String password) {
        if (email.isEmpty()) Toast.makeText(this, "Input your email", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty())
            Toast.makeText(this, "Input your password", Toast.LENGTH_SHORT).show();
        else {
            register(email, password);
        }
    }
    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(mAuth.getUid(),mName.getText().toString(),mNum.getText().toString(),mParNum.getText().toString(),mDOB.getText().toString(),mEmail.getText().toString(),sex);
                            mFbdb = FirebaseDatabase.getInstance();
                            mDbref = mFbdb.getReference();
                            uid=mAuth.getUid();
                            mDbref.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                            Toast.makeText(RegistrationActivity.this, "Sign up is successful", Toast.LENGTH_SHORT).show();
                            startActivity((new Intent(getApplicationContext(),MainActivity.class)));
                        }
                    }
                });
    }
}
