package com.clbrain.kirizhik.epidetect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser User = mAuth.getCurrentUser();

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private void addUser(String uid) {
        Requests.apiServices.sendUID(new User(uid)).enqueue(new Callback<Color>() {
            @Override
            public void onResponse(@NonNull Call<Color> call, @NonNull Response<Color> response) {
                if (response.isSuccessful()) {
                    Log.i("COLOR", response.body().getUser_color());
                } else {
                    Log.e("ADD_USER", "responce is not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Color> call, @NonNull Throwable t) {
                Log.e("ADD_USER", "call failure");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signInOrSignUp(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });
    }

    private void signInOrSignUp(String email, String password) {
        if (email.isEmpty()) Toast.makeText(this, "Input your email", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty())
            Toast.makeText(this, "Input your password", Toast.LENGTH_SHORT).show();
        else {
            register(email, password);
            signing(email, password);
        }
    }

    private void signing(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User = mAuth.getCurrentUser();
                            assert User != null;
                            addUser(User.getUid());
                            Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            addUser(user.getUid());
                            Toast.makeText(LoginActivity.this, "Sign up is successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
