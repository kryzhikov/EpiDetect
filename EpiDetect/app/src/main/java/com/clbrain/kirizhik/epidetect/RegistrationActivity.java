package com.clbrain.kirizhik.epidetect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity {
    Button sign_in;
    Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        sign_up=findViewById(R.id.sign_up_btn);
        sign_in=findViewById(R.id.sign_in_btn);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Регистрация и тп

                //startActivity((new Intent(getApplicationContext(),MainActivity.class)));
            }
        });

    }
}
