package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    TextView textView;
    private int screenWidth;
    private int screenHeight;

    private ImageView imagefire;
    private ImageView imagelight;

    private float imagefireX;
    private float imagefireY;
    private float imagelightX;
    private float imagelightY;
    Customization cst =new Customization();


    private Handler handler =new Handler();
    private Timer timer = new Timer();
    private Timer spelltime = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mainmenu);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        cst.loadText();
        FontTexts();
        startService(new Intent(this, commonplayer.class));



        ///
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
                        //imagefire.setVisibility(View.VISIBLE);
                        //imagelight.setVisibility(View.VISIBLE);

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

    //Стиль текста
    private void FontTexts(){
        final TextView start = (TextView)findViewById(R.id.start);
        start.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView quick = (TextView)findViewById(R.id.quick);
        quick.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView settings = (TextView)findViewById(R.id.settings);
        settings.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView about = (TextView)findViewById(R.id.rules);
        about.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView exit = (TextView)findViewById(R.id.exit);
        exit.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView NetGame = (TextView)findViewById(R.id.Netgame);
        NetGame.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
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
        stopService(new Intent(this, commonplayer.class));
    }
    //Кнопка "играть"
    public void goTogames(View view){
        Intent i = new Intent(StartActivity.this, Deckofcards.class);
        startActivity(i);

    }
    public void ToNet(View view){
        Intent i = new Intent(StartActivity.this, LogInActivity.class);
        startActivity(i);
        this.finish();
    }

}
