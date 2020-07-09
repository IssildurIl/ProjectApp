package com.example.projectapp;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class battleplayer extends Service {
    private static final String TAG = "MyBattleMusic";
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        int [] mp  ={R.raw.battle,R.raw.battle2,R.raw.battle3};
        Random random = new Random();
        int pos = random.nextInt(mp.length);
        player = MediaPlayer.create(this, mp[pos]);
        player.start();
        player.setLooping(true);
    }

    @Override
    public void onDestroy() {
        player.stop();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        player.start();
    }
}