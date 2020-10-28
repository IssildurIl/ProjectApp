package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import static com.example.projectapp.Constants.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        APP_PREFERENCE_FIREBASE_AUTHENTIFICATION = FirebaseAuth.getInstance();
        if (APP_PREFERENCE_FIREBASE_AUTHENTIFICATION.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, ListOfPlayer.class));
            finish();
        }
        fonttext();
        APP_PREFERENCE_FIREBASE_AUTHENTIFICATION = FirebaseAuth.getInstance();
        loadText();
        APP_PREFERENCE_LOGINACTIVITY_BUTTON_SIGN_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });
        APP_PREFERENCE_LOGINACTIVITY_BUTTON_RESET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ResPasActivity.class));
            }
        });
        APP_PREFERENCE_LOGINACTIVITY_BUTTON_LOG_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = APP_PREFERENCE_LOGINACTIVITY_INPUT_EMAIL.getText().toString();
                final String password = APP_PREFERENCE_LOGINACTIVITY_INPUT_PASSWORD.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.eaddr, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.epass, Toast.LENGTH_SHORT).show();
                    return;
                }
                APP_PREFERENCE_FIREBASE_AUTHENTIFICATION.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        APP_PREFERENCE_LOGINACTIVITY_INPUT_PASSWORD.setError(getString(R.string.length_min_pas));
                                    } else {
                                        Toast.makeText(LogInActivity.this, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    SharedPreferences.Editor ed = APP_PREFERENCES_SETTINGS.edit();
                                    ed.putString(APP_PREFERENCES_NETNAME,email.substring(0,email.indexOf("@")));
                                    ed.commit();
                                    saveText(email,password);
                                    Intent intent = new Intent(LogInActivity.this, ListOfPlayer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        

    }
    public void saveText(String email, String pass) {
        SharedPreferences.Editor ed = APP_PREFERENCES_SETTINGS.edit();
        ed.putString(APP_PREFERENCES_EMAIL, email);
        ed.putString(APP_PREFERENCES_PASS, pass);
        ed.commit();
    }

    public void loadText() {
        String savedMail = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_EMAIL, "");
        String savedPas= APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_PASS,"");
        APP_PREFERENCE_LOGINACTIVITY_INPUT_EMAIL.setText(savedMail);
        APP_PREFERENCE_LOGINACTIVITY_INPUT_PASSWORD.setText(savedPas);
    }
   public void fonttext() {
       APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
       APP_PREFERENCE_LOGINACTIVITY_INPUT_EMAIL = findViewById(R.id.email);
       APP_PREFERENCE_LOGINACTIVITY_INPUT_PASSWORD = findViewById(R.id.password);
       APP_PREFERENCE_LOGINACTIVITY_BUTTON_SIGN_UP = findViewById(R.id.signup);
       APP_PREFERENCE_LOGINACTIVITY_BUTTON_LOG_IN =  findViewById(R.id.login);
       APP_PREFERENCE_LOGINACTIVITY_BUTTON_RESET = findViewById(R.id.reset_password);
   }
}
