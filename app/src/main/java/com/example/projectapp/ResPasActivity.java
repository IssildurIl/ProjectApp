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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResPasActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_res_pas);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fonttext();
        auth = FirebaseAuth.getInstance();

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
                    Toast.makeText(getApplication(), "Введите email, который указывали при регистрации ", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResPasActivity.this, "Мы отправили вам инструкции для смены пароля!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResPasActivity.this, "Ошибка при смене пароля!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
    public void fonttext() {
        final TextView txt1 = (android.widget.TextView)findViewById(R.id.hint);
        txt1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView txt2 = (TextView)findViewById(R.id.email);
        txt2.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final Button btn1 = (Button) findViewById(R.id.btn_res_pas);
        btn1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final Button btn2 = (Button) findViewById(R.id.btn_back);
        btn2.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
    }

}