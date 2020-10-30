package com.example.projectapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import static com.example.projectapp.Constants.*;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btnSignIn = (Button) findViewById(R.id.sign_in_btn);
        btnSignUp = (Button) findViewById(R.id.sign_up_btn);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnResetPassword = (Button) findViewById(R.id.btn_res_pas);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResPasActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "@string/enterEmailsign", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "@string/enterPassign", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "@string/sixsignerr", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                APP_PREFERENCE_FIREBASE_AUTHENTIFICATION.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "@string/regsuccess", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, StartActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "@string/refailure",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(SignUpActivity.this, BattlePlayer.class));
        stopService(new Intent(SignUpActivity.this, CommonPlayer.class));
    }

    // развернули приложение
    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(SignUpActivity.this, CommonPlayer.class));
    }

}