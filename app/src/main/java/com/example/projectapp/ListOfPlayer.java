package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.projectapp.Constants.*;
import java.util.ArrayList;

public class ListOfPlayer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_of_player);
        APP_PREFERENCE_FIREBASE_DATABASE = FirebaseDatabase.getInstance();
        APP_PREFERENCE_FIREBASE_AUTHENTIFICATION = FirebaseAuth.getInstance();
        APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        playerName = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_NETNAME, APP_PREFERENCE_LISTOFPLAYER_DEFAULT_PLAYER);
        roomName=playerName;
        fonttext();
        gotomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfPlayer.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        chngacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APP_PREFERENCE_FIREBASE_AUTHENTIFICATION.signOut();
                Intent intent = new Intent(ListOfPlayer.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        roomsList= new ArrayList<>();
        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createroom.setText("CREATING ROOM");
                createroom.setEnabled(false);
                roomName = playerName;
                roomRef = APP_PREFERENCE_FIREBASE_DATABASE.getReference("rooms/"+ roomName+"/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);

            }
        });

        APP_PREFERENCE_LISTOFPLAYER_PLAYER_LIST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                roomRef= APP_PREFERENCE_FIREBASE_DATABASE.getReference("rooms/"+roomName+"/player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        addRoomsEventListener();
    }
    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                createroom.setText("Create Room(Join)");
                createroom.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                createroom.setText("CREATE ROOM");
                createroom.setEnabled(true);
                Toast.makeText(ListOfPlayer.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void addRoomsEventListener(){
        roomsRef= APP_PREFERENCE_FIREBASE_DATABASE.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    roomsList.clear();
                    Iterable<DataSnapshot> rooms= dataSnapshot.getChildren();
                    for(DataSnapshot snapshot: rooms){
                        roomsList.add(snapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListOfPlayer.this, android.R.layout.simple_list_item_1,roomsList);
                        APP_PREFERENCE_LISTOFPLAYER_PLAYER_LIST.setAdapter(adapter);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error - nothing
            }
        });

    }
    private void ex_FONT(TextView textView){
        textView.setTypeface(Typeface.createFromAsset(
                getAssets(), "font/jurassic_park.ttf"));
    }
    public void fonttext() {
        final TextView txt1 = findViewById(R.id.usernick);
        final Button btn1 = (Button) findViewById(R.id.createroom);
        final Button btn2 = (Button) findViewById(R.id.changeacc);
        final Button btn3 = (Button) findViewById(R.id.gotomenu);
        ex_FONT(txt1);
        ex_FONT(btn1);
        ex_FONT(btn2);
        ex_FONT(btn3);
        APP_PREFERENCE_LISTOFPLAYER_PLAYER_LIST = findViewById(R.id.playerlist);
        createroom= findViewById(R.id.createroom);
        chngacc =findViewById(R.id.changeacc);
        gotomenu = findViewById(R.id.gotomenu);
        nick = findViewById(R.id.usernick);
        nick.setText("Игрок: "+playerName);
    }

}
