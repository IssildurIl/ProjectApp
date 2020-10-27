package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    private int screenWidth;
    private int screenHeight;

    private ImageView imagefire;
    private ImageView imagelight;

    private float imagefireX;
    private float imagefireY;
    private float imagelightX;
    private float imagelightY;
    private Handler handler =new Handler();
    private Timer timer = new Timer();
    private Timer spelltime = new Timer();
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CB="lesson";
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mainmenu);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        cst.loadText();
        startService(new Intent(this, CommonPlayer.class));

        imagefire=(ImageView)findViewById(R.id.element1);
        imagelight=(ImageView)findViewById(R.id.element2);
        WindowManager wm =getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth= size.x;
        screenHeight = size.y;

        imagelight.setX(-40.0f);
        imagelight.setY(-40.0f);
        imagefire.setX(-80.0f);
        imagefire.setY(-80.0f);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cangePos();
                        imagefire.setVisibility(View.VISIBLE);
                        imagelight.setVisibility(View.VISIBLE);

                    }
                });
            }
        },0,20);

        ///
    }

    public void cangePos(){
        imagelightX+=20;
        if (imagelight.getX()> screenWidth){
            imagelightX = -100.0f;
            imagelightY = (float)Math.floor(Math.random()*(screenHeight-imagelight.getHeight()));
        }
        imagelight.setX(imagelightX);
        imagelight.setY(imagelightY);

        imagefireX-=20;
        if (imagefire.getX()+ imagefire.getWidth()<0){
            imagefireX = screenWidth+100.0f;
            imagefireY = (float)Math.floor(Math.random()*(screenHeight-imagefire.getHeight()));
        }
        imagefire.setX(imagefireX);
        imagefire.setY(imagefireY);
    }

    //Переход в Правила
    public void goToRules(View view) {
        Intent i = new Intent(StartActivity.this, RulesActivity.class);
        startActivity(i);
        this.finish();
    }
    //Переход в параметры
    public void goToOptions(View view) {
        Intent i = new Intent(StartActivity.this, OptionActivity.class);
        startActivity(i);
        this.finish();
    }
    public void goToCustom(View view){
        Intent i = new Intent(StartActivity.this, Customization.class);
        startActivity(i);
        this.finish();
    }
    //Кнопка "выход"
    public void onBackPressed(View view) {
        moveTaskToBack(true); android.os.Process.killProcess(android.os.Process.myPid()); System.exit(1);
        stopService(new Intent(this, CommonPlayer.class));
        stopService(new Intent(StartActivity.this, BattlePlayer.class));
        stopService(new Intent(StartActivity.this, CommonPlayer.class));
    }
    //Кнопка "играть"
    public void goTogames(View view) throws InterruptedException {
        Thread.sleep(5);
        stopService(new Intent(StartActivity.this, CommonPlayer.class));

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String less = mSettings.getString(APP_PREFERENCES_CB, "0");
        //Toast.makeText(StartActivity.this,""+less,Toast.LENGTH_LONG).show();
        Intent j = new Intent(StartActivity.this, LessonActivity.class);
        startActivity(j);
        if(less.equals("0")) {
            Intent i = new Intent(StartActivity.this, DeckOfCards.class);
            startActivity(i);
        }
    }
    public void ToNet(View view){
        Intent i = new Intent(StartActivity.this, LogInActivity.class);
        startActivity(i);
        this.finish();
    }

}
