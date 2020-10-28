package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import static com.example.projectapp.Constants.*;
import androidx.appcompat.app.AppCompatActivity;

public class Customization extends AppCompatActivity implements ViewSwitcher.ViewFactory, GestureDetector.OnGestureListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.customactivity);
        findViewById();
        APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        APP_PREFERENCE_CUSTOMIZATION_IN_ANIMATION = new AlphaAnimation(APP_PREFERENCES_INT_ZERO,APP_PREFERENCES_INT_ONE);
        APP_PREFERENCE_CUSTOMIZATION_IN_ANIMATION.setDuration(2000);
        APP_PREFERENCE_CUSTOMIZATION_OUT_ANIMATION = new AlphaAnimation(APP_PREFERENCES_INT_ONE, APP_PREFERENCES_INT_ZERO);
        APP_PREFERENCE_CUSTOMIZATION_OUT_ANIMATION.setDuration(2000);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setFactory(this);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setInAnimation(APP_PREFERENCE_CUSTOMIZATION_IN_ANIMATION);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setOutAnimation(APP_PREFERENCE_CUSTOMIZATION_OUT_ANIMATION);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setImageResource(APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE[0]);
        APP_PREFERENCES_CUSTOMIZATION_NEW_ICONE_POSITION = Integer.parseInt(APP_PREFERENCES_ICONE);
        APP_PREFERENCES_CUSTOMIZATION_GESTURE_DESTRUCTOR = new GestureDetector(this, this);
        loadText();
    }

    private void findViewById(){
        APP_PREFERENCES_CUSTOMIZATION_TITLE = findViewById(R.id.title);
        APP_PREFERENCES_CUSTOMIZATION_HINT = findViewById(R.id.hint);
        APP_PREFERENCES_CUSTOMIZATION_BUTTON_BACK = findViewById(R.id.deckOfCards_BackButton);
        APP_PREFERENCES_CUSTOMIZATION_NICK = findViewById(R.id.inputNick);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER = findViewById(R.id.IconeSwither);
        APP_PREFERENCES_CUSTOMIZATION_CHECK_LESSON = findViewById(R.id.checklesson);
    }

    public void setPositionToIcone() {
        APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION++;
        if (APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION > APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE.length - APP_PREFERENCES_INT_ONE) {
            APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION = APP_PREFERENCES_INT_ZERO;
        }
    }

    public void setPositionPrevIcone() {
        APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION--;
        if (APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION < APP_PREFERENCES_INT_ZERO) {
            APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION = APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE.length - APP_PREFERENCES_INT_ONE;
        }
    }

    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new
                ImageSwitcher.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(0x00000000);
        return imageView;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return APP_PREFERENCES_CUSTOMIZATION_GESTURE_DESTRUCTOR.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {}
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        try {
            if (Math.abs(e1.getY() - e2.getY()) > APP_PREFERENCES_CUSTOMIZATION_SWIPE_MAX_OFF_PATH)
                return false;
// справа налево
            if (e1.getX() - e2.getX() > APP_PREFERENCES_CUSTOMIZATION_SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > APP_PREFERENCES_CUSTOMIZATION_SWIPE_THRESHOLD_VELOCITY) {
                setPositionToIcone();
                APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setImageResource(APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE[APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION]);
            } else if (e2.getX() - e1.getX() > APP_PREFERENCES_CUSTOMIZATION_SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > APP_PREFERENCES_CUSTOMIZATION_SWIPE_THRESHOLD_VELOCITY) {
// слева направо
                setPositionPrevIcone();
                APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setImageResource(APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE[APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION]);

            }
        }
        catch (Exception e) {
// nothing
            return true;
        }
        APP_PREFERENCES_CUSTOMIZATION_NEW_ICONE_POSITION = APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION;
        return true;
    }
    void saveText() {
        SharedPreferences.Editor ed = APP_PREFERENCES_SETTINGS.edit();
        if(APP_PREFERENCES_CUSTOMIZATION_CHECK_LESSON.isChecked())
            ed.putString(APP_PREFERENCES_CB, APP_PREFERENCES_STRING_ONE);
        else
            ed.putString(APP_PREFERENCES_CB, APP_PREFERENCES_STRING_ZERO);
        ed.putString(APP_PREFERENCES_NAME, APP_PREFERENCES_CUSTOMIZATION_NICK.getText().toString());
        ed.putString(APP_PREFERENCES_ICONE, String.valueOf(APP_PREFERENCES_CUSTOMIZATION_NEW_ICONE_POSITION));
        ed.commit();
    }
    public void loadText() {
        APP_PREFERENCES_CUSTOMIZATION_SAVED_TEXT = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_NAME, "");
        APP_PREFERENCES_CUSTOMIZATION_SAVED_FOTO = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_ICONE, APP_PREFERENCES_STRING_ZERO);
        APP_PREFERENCES_CUSTOMIZATION_CHECK = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_CB, APP_PREFERENCES_STRING_ZERO);
        if(APP_PREFERENCES_CUSTOMIZATION_CHECK.equals(APP_PREFERENCES_STRING_ONE)) APP_PREFERENCES_CUSTOMIZATION_CHECK_LESSON.setChecked(true);
        APP_PREFERENCES_CUSTOMIZATION_NEW_ICONE_POSITION = Integer.parseInt(APP_PREFERENCES_CUSTOMIZATION_SAVED_FOTO);
        APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER.setImageResource(APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE[Integer.parseInt(APP_PREFERENCES_CUSTOMIZATION_SAVED_FOTO)]);
        APP_PREFERENCES_CUSTOMIZATION_NICK.setText(APP_PREFERENCES_CUSTOMIZATION_SAVED_TEXT);
    }
    public void goToMenu(View view) {
        saveText();
        finish();
    }
}