package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.projectapp.Constants.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    SharedPreferences APP_PREFERENCES_SETTINGS;
    FirebaseDatabase database;
    DatabaseReference playerRef;
    String playerName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //Toast.makeText(LogInActivity.this,"зашел",Toast.LENGTH_LONG).show();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, ListOfPlayer.class));
            finish();
        }
        setContentView(R.layout.activity_log_in);
        APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.signup);
        btnLogin = (Button) findViewById(R.id.login);
        btnReset = (Button) findViewById(R.id.reset_password);
        fonttext();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        loadText();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ResPasActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                //Toast.makeText(LogInActivity.this,"зашел"+email,Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Введите Еmail адрес!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Введите пароль!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid =user.getUid();
                                Toast.makeText(LogInActivity.this,  uid, Toast.LENGTH_LONG).show();
                                Log.i("TAG", " uid = " +  uid);// позиия элемента в списке*/
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.length_min_pas));
                                    } else {
                                        Toast.makeText(LogInActivity.this, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    SharedPreferences.Editor ed = APP_PREFERENCES_SETTINGS.edit();
                                    ed.putString(APP_PREFERENCES_NETNAME,email.substring(0,email.indexOf("@")));
                                    ed.commit();
                                    //Toast.makeText(LogInActivity.this, email.substring(0,email.indexOf("@")), Toast.LENGTH_LONG).show();
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
        //inputEmail = (EditText) findViewById(R.id.email);
        //inputPassword = (EditText) findViewById(R.id.password);
        SharedPreferences.Editor ed = APP_PREFERENCES_SETTINGS.edit();
        ed.putString(APP_PREFERENCES_EMAIL, email);
        ed.putString(APP_PREFERENCES_PASS, pass);
        ed.commit();
        //Toast.makeText(LogInActivity.this,"зашел преференсы" + APP_PREFERENCES_EMAIL+" "+ APP_PREFERENCES_PASS,Toast.LENGTH_LONG).show();
    }

    public void loadText() {
        //inputEmail = (EditText) findViewById(R.id.email);
        //inputPassword = (EditText) findViewById(R.id.password);
        String savedMail = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_EMAIL, "");
        String savedPas= APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_PASS,"");
        //Toast.makeText(LogInActivity.this,"загрузил преференсы" + savedMail ,Toast.LENGTH_LONG).show();
        inputEmail.setText(savedMail);
        inputPassword.setText(savedPas);
    }
   public void fonttext() {
       final TextView txt1 = (android.widget.TextView)findViewById(R.id.email);
       txt1.setTypeface(Typeface.createFromAsset(
               getAssets(), "fonts/JurassicPark-BL48.ttf"));
       final TextView txt2 = (TextView)findViewById(R.id.password);
       txt2.setTypeface(Typeface.createFromAsset(
               getAssets(), "fonts/JurassicPark-BL48.ttf"));
       final Button btn1 = (Button) findViewById(R.id.login);
       btn1.setTypeface(Typeface.createFromAsset(
               getAssets(), "fonts/JurassicPark-BL48.ttf"));
       final Button btn2 = (Button) findViewById(R.id.signup);
       btn2.setTypeface(Typeface.createFromAsset(
               getAssets(), "fonts/JurassicPark-BL48.ttf"));
       final Button btn3 = (Button) findViewById(R.id.reset_password);
       btn3.setTypeface(Typeface.createFromAsset(
               getAssets(), "fonts/JurassicPark-BL48.ttf"));
   }
}
