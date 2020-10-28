package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.projectapp.Constants.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResPasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        fonttext();
        APP_PREFERENCE_FIREBASE_AUTHENTIFICATION = FirebaseAuth.getInstance();
        APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        inputEmail.setText(APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_EMAIL,""));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), R.string.regemail, Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                APP_PREFERENCE_FIREBASE_AUTHENTIFICATION.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResPasActivity.this, R.string.sendInst, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResPasActivity.this, R.string.errorpas, Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
    public void fonttext() {
        inputEmail =  findViewById(R.id.email);
        btnReset = findViewById(R.id.btn_res_pas);
        btnBack = findViewById(R.id.btn_back);
    }

}