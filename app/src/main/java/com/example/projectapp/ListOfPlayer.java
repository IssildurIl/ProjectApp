package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.projectapp.Constants.*;
import java.util.ArrayList;
import java.util.List;

public class ListOfPlayer extends AppCompatActivity {
    ListView playerlist;
    Button createroom,chngacc,gotomenu;
    TextView nick;
    List<String> roomsList;
    String playerName= "";
    String roomName= "";
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    private SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_of_player);
        Typeface typeBold = Typeface.createFromAsset(getAssets(),"fonts/JurassicPark-BL48.ttf");
        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        playerName = mSettings.getString(APP_PREFERENCES_NETNAME,"default player");
        roomName=playerName;
        fonttext();
        playerlist = findViewById(R.id.playerlist);
        createroom= findViewById(R.id.createroom);
        chngacc =findViewById(R.id.changeacc);
        gotomenu = findViewById(R.id.gotomenu);
        nick = findViewById(R.id.usernick);
        nick.setText("Игрок: "+playerName);
        //all exiting available rooms
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
                auth.signOut();
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
                roomRef = database.getReference("rooms/"+ roomName+"/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);

            }
        });

        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join existing room and add yourself at player2
                roomName = roomsList.get(position);
                roomRef=database.getReference("rooms/"+roomName+"/player2");
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
                //join the room
                createroom.setText("Create Room(Join)");
                createroom.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(),PlayActivity.class);
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
        roomsRef=database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    roomsList.clear();
                    Iterable<DataSnapshot> rooms= dataSnapshot.getChildren();
                    for(DataSnapshot snapshot: rooms){
                        roomsList.add(snapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListOfPlayer.this, android.R.layout.simple_list_item_1,roomsList);
                        playerlist.setAdapter(adapter);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error - nothing
            }
        });

    }
    public void fonttext() {
        final TextView txt1 = (android.widget.TextView)findViewById(R.id.usernick);
        txt1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final Button btn1 = (Button) findViewById(R.id.createroom);
        btn1.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final Button btn2 = (Button) findViewById(R.id.changeacc);
        btn2.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final Button btn3 = (Button) findViewById(R.id.gotomenu);
        btn3.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

    }

}
