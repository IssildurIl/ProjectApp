package com.example.projectapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_btn);
        btnSignUp = (Button) findViewById(R.id.sign_up_btn);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_res_pas);
        fonttext();
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
                    Toast.makeText(getApplicationContext(), "Введите Email адрес!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Введите пароль!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Пароль слишком короткий. Он должен быть не менее 6 знаков!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, StartActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Ошибка регестрации",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void fonttext() {
        final TextView txt1 = (android.widget.TextView)findViewById(R.id.email);
        txt1.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
        final TextView txt2 = (TextView)findViewById(R.id.password);
        txt2.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
        final Button btn1 = (Button) findViewById(R.id.sign_in_btn);
        btn1.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
        final Button btn2 = (Button) findViewById(R.id.sign_up_btn);
        btn2.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
        final Button btn3 = (Button) findViewById(R.id.btn_res_pas);
        btn3.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
    }

}