package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

public class Customization extends AppCompatActivity implements ViewSwitcher.ViewFactory, GestureDetector.OnGestureListener {
    private ImageSwitcher mIconeSwicher;
//

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname"; // имя кота
    public static final String APP_PREFERENCES_ICONE="0";
    public static final String APP_PREFERENCES_CB="lesson";
    SharedPreferences mSettings;
    EditText StrNick;
    int foto;
    private CheckBox checklesson;
//

    int position = 0;
    private int[] mIcone = { R.drawable.icon_enchanter, R.drawable.icon_genie,
            R.drawable.icon_hogshouse, R.drawable.icon_krazztar, R.drawable.icon_lady,
            R.drawable.icon_princess, R.drawable.icon_spiritmaster,R.drawable.icon_wizard};
    private GestureDetector mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.customactivity);
        checklesson = (CheckBox)findViewById(R.id.checklesson);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        FontTexts();
//
        Animation inAnimation = new AlphaAnimation(0, 1);
        inAnimation.setDuration(2000);
        Animation outAnimation = new AlphaAnimation(1, 0);
        outAnimation.setDuration(2000);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mIconeSwicher = (ImageSwitcher)findViewById(R.id.IconeSwither);
        mIconeSwicher.setFactory(this);
        mIconeSwicher.setInAnimation(inAnimation);
        mIconeSwicher.setOutAnimation(outAnimation);
        mIconeSwicher.setImageResource(mIcone[0]);

        foto = Integer.parseInt(APP_PREFERENCES_ICONE);
        FontTexts();
        mGestureDetector = new GestureDetector(this, this);
        loadText();


    }
    private void FontTexts(){
        final TextView title = (TextView)findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView backb1 = (TextView)findViewById(R.id.backb1);
        backb1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final EditText nick = (EditText)findViewById(R.id.inputNick);
        nick.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
    }
    public void setPositiontoIcone() {
        position++;
        if (position > mIcone.length - 1) {
            position = 0;
        }
    }

    public void setPositionPrevIcone() {
        position--;
        if (position < 0) {
            position = mIcone.length - 1;
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
        return mGestureDetector.onTouchEvent(event);
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
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
// справа налево
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                setPositiontoIcone();
                mIconeSwicher.setImageResource(mIcone[position]);
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
// слева направо
                setPositionPrevIcone();
                mIconeSwicher.setImageResource(mIcone[position]);

            }
        }
        catch (Exception e) {
// nothing
            return true;
        }
        foto = position;
        return true;
    }
    public void gotoMenu(View view) {
        saveText();
        Intent i = new Intent(Customization.this, StartActivity.class);
        startActivity(i);
        this.finish();
        //Toast.makeText(Customization.this,""+ mSettings.getString(APP_PREFERENCES_CB, ""),Toast.LENGTH_LONG).show();
    }
    void saveText() {
        StrNick = (EditText)findViewById(R.id.inputNick);
        SharedPreferences.Editor ed = mSettings.edit();
        if(checklesson.isChecked())
            ed.putString(APP_PREFERENCES_CB,"1");
        else
            ed.putString(APP_PREFERENCES_CB,"0");
        ed.putString(APP_PREFERENCES_NAME, StrNick.getText().toString());
        ed.putString(APP_PREFERENCES_ICONE, String.valueOf(foto));
        ed.commit();
    }

    public void loadText() {
        StrNick = (EditText)findViewById(R.id.inputNick);
        String savedText = mSettings.getString(APP_PREFERENCES_NAME, "");
        String savedFoto=mSettings.getString(APP_PREFERENCES_ICONE,"0");
        String chk = mSettings.getString(APP_PREFERENCES_CB,"0");
        if(chk.equals("1"))checklesson.setChecked(true);
        foto = Integer.parseInt(savedFoto);
        mIconeSwicher.setImageResource(mIcone[Integer.parseInt(savedFoto)]);
        StrNick.setText(savedText);
    }

}