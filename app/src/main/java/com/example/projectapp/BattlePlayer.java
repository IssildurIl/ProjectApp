package com.example.projectapp;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.util.Random;
public class BattlePlayer extends Service {
    public static MediaPlayer APP_PREFERENCES_MEDIA_PLAYER;
    public static final int[] APP_PREFERENCES_BATTLE_MUSIC ={R.raw.battle, R.raw.battle2, R.raw.battle3};
    public static int APP_PREFERENCE_PLAYER_POSITION;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Random random = new Random();
        APP_PREFERENCE_PLAYER_POSITION = random.nextInt(APP_PREFERENCES_BATTLE_MUSIC.length);
        APP_PREFERENCES_MEDIA_PLAYER = MediaPlayer.create(this, APP_PREFERENCES_BATTLE_MUSIC[APP_PREFERENCE_PLAYER_POSITION]);
        APP_PREFERENCES_MEDIA_PLAYER.start();
        APP_PREFERENCES_MEDIA_PLAYER.setLooping(true);
    }

    @Override
    public void onDestroy() {
        APP_PREFERENCES_MEDIA_PLAYER.stop();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        APP_PREFERENCES_MEDIA_PLAYER.start();
    }
}