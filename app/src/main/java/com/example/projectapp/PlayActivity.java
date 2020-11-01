package com.example.projectapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.projectapp.Constants.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    ImageButton Click;
    String playerName = "";
    String roomName = "";
    String role ="";
    String message ="";
    ImageButton rollDicesButton;
    boolean flag1=true;
    TextView nick;
    ImageView iv_deck, iv_card1, iv_card2, iv_card3, iv_card4, iv_card5, iv_card6;

    public CardHand hostHand = new CardHand();
    public CardHand guestHand = new CardHand();
    public CardTable hostTable = new CardTable();
    public CardTable guestTable = new CardTable();

     /*
    public CardHand hostHand;
    public CardHand guestHand;
    public CardTable hostTable;
    public CardTable guestTable;
     */
    public int[] c_card_table = { 0, 0, 0};
    public int[] c_table_opp = { 0, 0, 0};
    public int[] c_card_hand = { 0, 0, 0, 0, 0, 0};
    public int[] s_card_table = { 0, 0, 0};
    public int[] s_table_opp = { 0, 0, 0};
    public int[] s_card_hand = { 0, 0, 0, 0, 0, 0, 0};
    ArrayList main_cards;
    ImageView fstcard,seccard,thirdcard;
    int card_num=0, c_leave_card=6, c_leave_opp=0, s_leave_card=6, s_leave_opp=6;
    ImageView PlayerIcon, mLeftImageView,mRightImageView,mMidImageView;
    int countSymbol[] = new int[3];
    //
    private SharedPreferences mSettings;
    private int[] mIcone = { R.drawable.icon_enchanter, R.drawable.icon_genie,
            R.drawable.icon_hogshouse, R.drawable.icon_krazztar, R.drawable.icon_lady,
            R.drawable.icon_princess, R.drawable.icon_spiritmaster,R.drawable.icon_wizard};
    // поле
    ImageView tab_im1;
    //
    FrameLayout spells, hand;
    int value1, value2,value3;
    //
    FirebaseDatabase database;
    DatabaseReference messageRef;
    DatabaseReference roomsRef;
    private SoundPool soundPool;
    private int sound1, secretsound, sound3, sound4,darknesssound,firesound,naturesound,illusionsound ;
    List<String> roomsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        stopService(new Intent(PlayActivity.this, CommonPlayer.class));
        startService(new Intent(PlayActivity.this, BattlePlayer.class));

        /*
        hostHand = new CardHand();
        guestHand = new CardHand();
        hostTable = new CardTable();
        guestTable = new CardTable();
         */
        //местные звуки
        soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        final TextView textnick = (TextView)findViewById(R.id.playerNick);
        roomsList= new ArrayList<>();
        sound1 = soundPool.load(this, R.raw.givecards, 1);
        secretsound = soundPool.load(this, R.raw.dropcard, 1);
        sound3 = soundPool.load(this, R.raw.takedmg, 1);
        sound4 = soundPool.load(this, R.raw.dice, 1);
        darknesssound = soundPool.load(this, R.raw.darkness, 1);
        firesound = soundPool.load(this, R.raw.fire, 1);
        naturesound = soundPool.load(this, R.raw.nature, 1);
        illusionsound = soundPool.load(this, R.raw.illusion,1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Click = findViewById(R.id.end);
        Click.setEnabled(false);

        database = FirebaseDatabase.getInstance();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        playerName = mSettings.getString(APP_PREFERENCES_NETNAME,"default player");
        String savedIcone = mSettings.getString(APP_PREFERENCES_ICONE, "0");
        getText();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){

            roomName= extras.getString("roomName");
            if(roomName.equals(playerName)){
                role="host";
                ((TextView)findViewById(R.id.playerNick)).setText(playerName);
            } else{
                role="guest";
                ((TextView)findViewById(R.id.playerNick)).setText(playerName);
                ((TextView)findViewById(R.id.playerNick2)).setText(roomName);
            }
        }
        ((ImageView)findViewById(R.id.playerIcon)).setImageResource(mIcone[Integer.parseInt(savedIcone)]);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.setEnabled(false);
                soundPool.play(sound3, 1, 1, 0, 0, 1);
                //rollDicesButton.setEnabled(true);
                message= role+":";
                switch (role){
                    case "guest":
                        getPrompt(Constants.TAKE);
                        tableAction(guestTable);
                        card_num=hostHand.takeCards(card_num, Constants.MAIN_CARDS);
                        message+="card+"+hostHand.getString();
                        message+="table*"+guestTable.getString();
                        break;
                    case "host":
                        getPrompt(Constants.HOST_WAIT);
                        message+="table*"+hostTable.getString();
                }
                message+="dice"+value1+value2+value3;
                messageRef.setValue(message);
            }
        });

        //shuffle the cards
        Collections.shuffle(Constants.MAIN_CARDS);
        ImageView backButton=findViewById(R.id.activity_play_BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    removeDataFromDatabase();
                    messageRef.setValue("exit");
                    finish();
                }
        });
        messageRef = database.getReference("rooms/"+ roomName+"/message");
        cleanTable();
        switch(role){
            case "host":
                getPrompt(Constants.CONNECT);
                //message= role+":table*"+hostTable.getString();
                message= role+":table*0*0*0*";
                break;
            case "guest":
                getPrompt(Constants.WAIT);
                card_num=guestHand.takeCards(card_num, Constants.MAIN_CARDS);
                //Toast.makeText(PlayActivity.this, ""+card_num, Toast.LENGTH_SHORT).show();
                setHand(guestHand.getCards());
                card_num=hostHand.takeCards(card_num, Constants.MAIN_CARDS);
                message= role+":card+"+hostHand.getString()+"table*"+guestTable.getString()+"dice111";
                //message= role+":card:0+table:0*dice11";
        }
        messageRef.setValue(message);
        addRoomEventListener();

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        PlayerIcon = findViewById(R.id.playerIcon);
        nick = findViewById(R.id.playerNick);
        spells = (FrameLayout) findViewById(R.id.spells);
        hand = (FrameLayout) findViewById(R.id.hand);
        //

        //
        iv_deck = findViewById(R.id.iv_deck);
        iv_card1 = findViewById(R.id.iv_card1);
        iv_card2 = findViewById(R.id.iv_card2);
        iv_card3 = findViewById(R.id.iv_card3);
        iv_card4 = findViewById(R.id.iv_card4);
        iv_card5 = findViewById(R.id.iv_card5);
        iv_card6 = findViewById(R.id.iv_card6);

        fstcard = (ImageView) findViewById(R.id.fstcard);
        seccard = (ImageView) findViewById(R.id.seccard);
        thirdcard = (ImageView) findViewById(R.id.thirdcard);

        fstcard.setOnDragListener(dragListener);
        seccard.setOnDragListener(dragListener);
        thirdcard.setOnDragListener(dragListener);
        /*
        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);

         */

        iv_card1.setOnLongClickListener(longClickListener);
        iv_card2.setOnLongClickListener(longClickListener);
        iv_card3.setOnLongClickListener(longClickListener);
        iv_card4.setOnLongClickListener(longClickListener);
        iv_card5.setOnLongClickListener(longClickListener);
        iv_card6.setOnLongClickListener(longClickListener);


        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sound1, 1, 1, 0, 0, 1);
                switch(role){
                    case "guest":
                        getPrompt(Constants.WAIT);
                        card_num=guestHand.takeCards(card_num, Constants.MAIN_CARDS);
                        setHand(guestHand.getCards());
                        break;
                    case "host":
                        getPrompt(Constants.ACTIV_ROLL);
                        setHand(hostHand.getCards());
                }
                cleanTable();
            }
        });

//кубики
        rollDicesButton = (ImageButton) findViewById(R.id.Roll);
        rollDicesButton.setEnabled(false); //
        mLeftImageView = (ImageView) findViewById(R.id.imageview_left);
        mRightImageView = (ImageView) findViewById(R.id.imageview_right);
        mMidImageView =(ImageView) findViewById(R.id.imageview_mid);
        rollDicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(sound4, 1, 1, 0, 0, 1);
                getPrompt(Constants.END);
                Click.setEnabled(true);
                rollDicesButton.setEnabled(false);
                fooRollDice();
                fooRollOneDice();
            }
        });
//
    }

    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text=""+ dataSnapshot.getValue(String.class);
                int d1s = value1, d2s = value2, d3s = value3;
                int d1, d2, d3;
                int last_index, prev_index;
                //String[] sub_str = { "", "", "", "", "", "", "" };
                if(text.contains("exit")){
                    Toast.makeText(PlayActivity.this,text.replace("Игра закончена, противник вышел из игры",""),Toast.LENGTH_SHORT).show();
                    removeDataFromDatabase();
                    finish();
                    //Intent i = new Intent(PlayActivity.this, StartActivity.class);
                    //startActivity(i);
                }
                else if(role.equals("host")){
                    try{
                        if(dataSnapshot.getValue(String.class).contains("guest:")) {
                            rollDicesButton.setEnabled(true);
                            if (hostTable.qntCardOnTable()==0) {
                                getPrompt(Constants.ACTIV_ROLL);
                            }else{
                                getPrompt(Constants.TAKE);
                            }

                            prev_index = text.indexOf("+");
                            last_index = text.lastIndexOf("+");
                            hostHand.setCards(text.substring(prev_index + 1, last_index).split("\\+"));
                            setHand(hostHand.getCards());
                            prev_index = text.indexOf("*");
                            last_index = text.lastIndexOf("*");
                            guestTable.setCards(text.substring(prev_index + 1, last_index).split("\\*"));
                            prev_index = text.indexOf("dice");
                            d1 = Integer.parseInt(text.substring(prev_index + 4, prev_index + 5));
                            d2 = Integer.parseInt(text.substring(prev_index + 5, prev_index + 6));
                            d3 = Integer.parseInt(text.substring(prev_index + 6, prev_index + 7));
                            fooSetDice(d1, d2, d3);
                            setTable(guestTable.getCardTable());
                            tableActionOpp(guestTable);
                            fooSetDice(d1s, d2s, d3s);
                            tableAction(hostTable);
                            guestTable.cleanTable();
                            hostTable.cleanTable();
                        /*
                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);
                                                */
                            Click.setEnabled(true);
                            //((TextView)findViewById(R.id.test)).setText(text);
                            //Toast.makeText(PlayActivity.this, text.replace("guest:", ""), Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){}
                }else if(role.equals("guest")){
                    try{
                        if(dataSnapshot.getValue(String.class).contains("host:")){
                            rollDicesButton.setEnabled(true);
                            getPrompt(Constants.ROLL);

                            prev_index = text.indexOf("*");
                            last_index = text.lastIndexOf("*");
                            hostTable.setCards(text.substring(prev_index + 1, last_index).split("\\*"));
                            prev_index = text.indexOf("dice");
                            d1 = Integer.parseInt(text.substring(prev_index + 4, prev_index + 5));
                            d2 = Integer.parseInt(text.substring(prev_index + 5, prev_index + 6));
                            d3 = Integer.parseInt(text.substring(prev_index + 6, prev_index + 7));
                            fooSetDice(d1, d2, d3);
                            setTable(hostTable.getCardTable());
                            tableActionOpp(hostTable);

                            for (int i=0; i<3; i++) {
                                hostHand.removeById(hostTable.getCardByNum(i));
                            }
                            guestTable.cleanTable();
                            hostTable.cleanTable();


                        /*
                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);
                                              */
                            //((TextView)findViewById(R.id.test)).setText(text);
                            //Toast.makeText(PlayActivity.this,text.replace("host:",""),Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){}
                }
                try{
                    if(dataSnapshot.getValue(String.class).contains("exit")){
                        Toast.makeText(PlayActivity.this,text.replace("Игра закончена, противник вышел из игры",""),Toast.LENGTH_SHORT).show();
                        removeDataFromDatabase();
                        finish();
                        //Intent i = new Intent(PlayActivity.this, StartActivity.class);
                        //startActivity(i);
                    }
                }catch (NullPointerException e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                messageRef.setValue(message);
            }
        });
    }

    public void getText(){

        nick =(TextView) findViewById(R.id.playerNick);
        String savedText = mSettings.getString(APP_PREFERENCES_NAME, "");
        nick.setText(savedText);
        //PlayerIcon.setImageResource();
    }
    private int randomDiceValue() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
    //
    public void onImClick(View v) {
        Drawable drawable;
        switch (v.getId()) {
            case R.id.iv_card1:
                drawable = iv_card1.getDrawable();
                break;
            case R.id.iv_card2:
                drawable = iv_card2.getDrawable();
                break;
            case R.id.iv_card3:
                drawable = iv_card3.getDrawable();
                break;
            case R.id.iv_card4:
                drawable = iv_card4.getDrawable();
                break;
            case R.id.iv_card5:
                drawable = iv_card5.getDrawable();
                break;
            case R.id.iv_card6:
                drawable = iv_card6.getDrawable();
                break;
            case R.id.fstcard:
                drawable = fstcard.getDrawable();
                break;
            case R.id.seccard:
                drawable = seccard.getDrawable();
                break;
            case R.id.thirdcard:
                drawable = thirdcard.getDrawable();
                break;
            case R.id.fstcard2:
                drawable = ((ImageView)findViewById(R.id.fstcard2)).getDrawable();
                break;
            case R.id.seccard2:
                drawable = ((ImageView)findViewById(R.id.seccard2)).getDrawable();
                break;
            case R.id.thirdcard2:
                drawable = ((ImageView)findViewById(R.id.thirdcard2)).getDrawable();
                break;
            default:
                drawable = iv_deck.getDrawable();
        }
        if (drawable==null){
            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this, R.style.CustomDialog);
        final ImageView image = new ImageView(this);
        image.setImageDrawable(drawable);
        alertDialog.setView(image);
        alertDialog.show();
    }

    //10.05.2020 3:23
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowbuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowbuilder, v, 0);
            return true;
        }
    };
    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            Drawable get_im=null;
            boolean flag;
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DROP:
                    //11.05.2020 16:59
                    switch (view.getId()) {
                        case R.id.iv_card1:
                            //11.05.2020 16:38
                            get_im=iv_card1.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card2:
                            get_im=iv_card2.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card3:
                            get_im=iv_card3.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card4:
                            get_im=iv_card4.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card5:
                            get_im=iv_card5.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card6:
                            get_im=iv_card6.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        default:
                            flag=false;
                    }
                    // 12.05.2020 13:35 смещение карт в руке
                    if (flag){

                        //cardAction(detectCard(get_im));
                        //card_table[leave_card-1]=detectCard(get_im);
                        switch(role){
                            case "guest":
                                guestTable.addCard2Table(detectCard(get_im));
                                guestHand.removeById(detectCard(get_im));

                                switch (view.getId()) {
                                    case R.id.iv_card1:
                                        iv_card1.setImageResource(guestHand.getCardByNum(0));
                                    case R.id.iv_card2:
                                        iv_card2.setImageResource(guestHand.getCardByNum(1));
                                    case R.id.iv_card3:
                                        iv_card3.setImageResource(guestHand.getCardByNum(2));
                                    case R.id.iv_card4:
                                        iv_card4.setImageResource(guestHand.getCardByNum(3));
                                    case R.id.iv_card5:
                                        iv_card5.setImageResource(guestHand.getCardByNum(4));
                                    case R.id.iv_card6:
                                        iv_card6.setImageResource(guestHand.getCardByNum(5));                                      
                                }
                                break;
                            case "host":
                                hostTable.addCard2Table(detectCard(get_im));
                                hostHand.removeById(detectCard(get_im));

                                switch (view.getId()) {
                                    case R.id.iv_card1:
                                        iv_card1.setImageResource(hostHand.getCardByNum(0));
                                    case R.id.iv_card2:
                                        iv_card2.setImageResource(hostHand.getCardByNum(1));
                                    case R.id.iv_card3:
                                        iv_card3.setImageResource(hostHand.getCardByNum(2));
                                    case R.id.iv_card4:
                                        iv_card4.setImageResource(hostHand.getCardByNum(3));
                                    case R.id.iv_card5:
                                        iv_card5.setImageResource(hostHand.getCardByNum(4));
                                    case R.id.iv_card6:
                                        iv_card6.setImageResource(hostHand.getCardByNum(5));
                                }
                                break;
                        }
                        /*
                        switch (view.getId()) {
                            case R.id.iv_card1:
                                get_im=iv_card2.getDrawable();
                                iv_card1.setImageDrawable(get_im);
                            case R.id.iv_card2:
                                get_im=iv_card3.getDrawable();
                                iv_card2.setImageDrawable(get_im);
                            case R.id.iv_card3:
                                get_im=iv_card4.getDrawable();
                                iv_card3.setImageDrawable(get_im);
                            case R.id.iv_card4:
                                get_im=iv_card5.getDrawable();
                                iv_card4.setImageDrawable(get_im);
                            case R.id.iv_card5:
                                get_im=iv_card6.getDrawable();
                                iv_card5.setImageDrawable(get_im);
                            case R.id.iv_card6:
                                iv_card6.setImageDrawable(null);
                        }*/
                    }
            }
            return true;
        }
    };

    //11.05.2020 16:48 проверка свободно ли место на столе
    public boolean changeCard(int Rid, Drawable picture){
        switch (Rid) {
            case R.id.fstcard:
                if (detectCard(fstcard.getDrawable())==0) {
                    if (Constants.I_MAIN_CARDS.contains(detectCard(picture))) {
                        fstcard.setImageDrawable(picture);
                        return true;
                    }
                }
                return false;
            case R.id.seccard:
                if (detectCard(seccard.getDrawable())==0){
                    if (Constants.K_MAIN_CARDS.contains(detectCard(picture))) {
                        seccard.setImageDrawable(picture);
                        return true;
                    }
                }
                return false;
            case R.id.thirdcard:
                if (detectCard(thirdcard.getDrawable())==0){
                    if (Constants.D_MAIN_CARDS.contains(detectCard(picture))) {
                        thirdcard.setImageDrawable(picture);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public int detectCard(Drawable picture){
        Drawable.ConstantState imageViewDrawableState;
        for (int i=0; i<Constants.MAIN_CARDS.size(); i++){
            imageViewDrawableState=getResources().getDrawable(Constants.MAIN_CARDS.get(i)).getConstantState();
            if (imageViewDrawableState.equals(picture.getConstantState())){
                //Toast.makeText(PlayActivity.this,""+main_cards.get(i),Toast.LENGTH_SHORT).show();
                return Constants.MAIN_CARDS.get(i);
            }
        }
        return 0;
    }




    public void fooSetDice(int d1, int d2, int d3){
        value1 = d1;
        value2 = d2;
        value3 = d3;
        int res1 = getResources().getIdentifier("dice" + value1,
                "drawable", "com.example.projectapp");
        int res2 = getResources().getIdentifier("dice" + value2,
                "drawable", "com.example.projectapp");
        int res3 = getResources().getIdentifier("dice" + value3,
                "drawable", "com.example.projectapp");
        mLeftImageView.setImageResource(res1);
        mRightImageView.setImageResource(res2);
        mMidImageView.setImageResource(res3);
    }

    public void setTable(int[] mas){
        ((ImageView) findViewById(R.id.fstcard2)).setImageResource(mas[0]);
        ((ImageView) findViewById(R.id.seccard2)).setImageResource(mas[1]);
        ((ImageView) findViewById(R.id.thirdcard2)).setImageResource(mas[2]);
    }
    public void setHand(int[] mas){
        ((ImageView) findViewById(R.id.iv_card1)).setImageResource(mas[0]);
        ((ImageView) findViewById(R.id.iv_card2)).setImageResource(mas[1]);
        ((ImageView) findViewById(R.id.iv_card3)).setImageResource(mas[2]);
        ((ImageView) findViewById(R.id.iv_card4)).setImageResource(mas[3]);
        ((ImageView) findViewById(R.id.iv_card5)).setImageResource(mas[4]);
        ((ImageView) findViewById(R.id.iv_card6)).setImageResource(mas[5]);
    }
    /*
    public void opp_turn(){
        leave_opp=0;
        int num;
        for (num=0; num<card_opp.length; num++){
            if (cards_i.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.fstcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }
        for (num=0; num<card_opp.length; num++){
            if (cards_k.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.seccard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }
        for (num=0; num<card_opp.length; num++){
            if (cards_d.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }

    }
     */
/*
    public void remove_card_opp(int iter){
        switch (iter) {
            case 0:
                card_opp[0]=card_opp[1];
            case 1:
                card_opp[1]=card_opp[2];
            case 2:
                card_opp[2]=card_opp[3];
            case 3:
                card_opp[3]=card_opp[4];
            case 4:
                card_opp[4]=card_opp[5];
            case 5:
                card_opp[5]=0;
        }
    }
 */

    public int[] cardAction(CardTable card, int pos){
        card.defAllSymbols();
        switch (card.getCardByNum(pos)) {
            //Смэрть
            case R.drawable.d_darkness_1:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1)
                        {
                            case 2: case 3: case 4:
                            return new int[]{-1, 0};
                            case 5:case 6:
                            return new int[]{-2, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12:
                                return new int[]{-6, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-6, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_darkness_2:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{0, -1};
                            case 5:case 6:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{0, -1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-4, 0};
                            case 10: case 11: case 12:
                                return new int[]{-8, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{0, -1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-4, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-8, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_darkness_3:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-2, 0};
                            case 5:case 6:
                                return new int[]{-3, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-2, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-6, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_darkness_4:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-2, -1};
                            case 5:case 6:
                                return new int[]{-3, -1};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-2, -1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12:
                                return new int[]{-5, -1};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-2, -1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-5, -1};
                        }
                        break;
                }
                break;
            //Стихия
            case R.drawable.d_element_1:
                switch (card.countSymbol(2)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-2, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-4, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_element_2:
                switch (card.countSymbol(2)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-3, -1};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12:
                                return new int[]{-5, -1};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-5, -1};
                        }
                        break;
                }
                break;
            case R.drawable.d_element_3:
                switch (card.countSymbol(2)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-1, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-1, 0};
                            case 10: case 11: case 12:
                                return new int[]{-7, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-1, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-7, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_element_4:
                switch (card.countSymbol(2)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-3, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-4, 0};
                        }
                        break;
                }
                break;

            //Иллюзия! Думай как делать сокровища
            case R.drawable.d_illusion_4:
                switch (card.countSymbol(3)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-3, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-4, 0};
                        }
                        break;
                }
                break;

            //Природа
            case R.drawable.d_nature_1:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-2, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12:
                                return new int[]{-4, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-4, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_nature_2:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{0, 0};
                            case 5:case 6:
                                return new int[]{0, +2};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{0, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{0, +2};
                            case 10: case 11: case 12:
                                return new int[]{0, +4};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{0, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{0, +2};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{0, +4};
                        }
                        break;
                }
                break;
            case R.drawable.d_nature_3:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-2, 0};
                            case 5:case 6:
                                return new int[]{-3, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-2, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12:
                                return new int[]{-6, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-2, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-6, 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_nature_4:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-2, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12:
                                return new int[]{-2*card.countSymbol(4), 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-2*card.countSymbol(4), 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_nature_5:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, +1};
                            case 5:case 6:
                                return new int[]{-2, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2) {
                            case 2: case 3: case 4:
                                return new int[]{-1, +1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12:
                                return new int[]{-2, +2};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, +1};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-2, +2};
                        }
                        break;
                }
                break;
            // ДЕЛАЙ СЕКРЕТ
            ///ИСТОЧНИКИ
            //Тьма
            case R.drawable.i_darkness_1:
                switch (value1){
                    case 1:case 2: case 3:
                        return new int[]{0,-value1};
                    case 4: case 5: case 6:
                        return new int[]{0,+value1};
                }
                break;
            case R.drawable.i_darkness_3:
                return new int[]{-value1 , -value2};
            case R.drawable.i_darkness_4:
                return new int[]{-2,0};
            //Стихия
            case R.drawable.i_element_1:
                return new int[]{-card.countDifSymbol(),0};
            case R.drawable.i_element_2:
                return new int[]{-3,0};
            case R.drawable.i_element_3:
                return new int[]{-3,0};
            case R.drawable.i_element_4:
                return new int[]{-3,0};
            //Иллюзия
            //Природа
            case R.drawable.i_nature_1:
                if (value1==6) return new int[]{+3,+3};
                else return new int[]{0,+3};
            case R.drawable.i_nature_2:
                return new int[]{0,+card.countDifSymbol()};
            case R.drawable.i_nature_4:
                return new int[]{0,+2};
            case R.drawable.i_nature_5:
                return new int[]{-3,0};
            //Секрет

            case R.drawable.k_darkness_1:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-2, 0};
                            case 5:case 6:
                                return new int[]{-4, -1};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-2, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-4, -1};
                            case 10: case 11: case 12:
                                return new int[]{-5, -2};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-2, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-4, -1};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-5, -2};
                        }
                        break;
                }
                break;
            case R.drawable.k_darkness_2:
                switch (card.countSymbol(1)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{0, -3};
                            case 5:case 6:
                                return new int[]{-3, 0};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{0, -3};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12:
                                return new int[]{-5, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{0, -3};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-5, 0};
                        }
                        break;
                }
                break;
            case R.drawable.k_darkness_4:
                return new int[]{-2*card.countSymbol(1),0};

            case R.drawable.k_element_1:
                return new int[]{-card.countSymbol(2),0};
            case R.drawable.k_element_2:
                switch (card.countSymbol(2)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-3, -1};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12:
                                return new int[]{-5, 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-3, -1};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-5, 0};
                        }
                        break;
                }
                break;
            case R.drawable.k_element_3:
                if (getHP() %2!=0){
                    return new int[]{-card.countDifSymbol(),0};
                }
            case R.drawable.k_nature_1:
                switch (card.countSymbol(4)){
                    case 1:
                        switch (value1){
                            case 2: case 3: case 4:
                                return new int[]{-1, 0};
                            case 5:case 6:
                                return new int[]{-1, +1};
                        }
                        break;
                    case 2:
                        switch (value1+value2){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-1, +1};
                            case 10: case 11: case 12:
                                return new int[]{-3, +3};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-1, +1};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-3, +3};
                        }
                        break;
                }
                break;
            case R.drawable.k_nature_3:
                return new int[]{-2*card.countDifSymbol(),0};
            case R.drawable.k_nature_4:
                return new int[]{-1, 0};
            case R.drawable.k_nature_5:
                return new int[]{-2, 0};
        }
        return new int[]{0, 0};
    }

    public void tableAction(CardTable cardTable){
        int[] hp={0, 0};
        for (int i=0; i<3; i++){
            hp=cardAction(cardTable, i);
            setHP(getHP()+hp[0]);
            setSelfHP(getSelfHP()+hp[1]);
        }
    }

    public void tableActionOpp(CardTable cardTable){
        //fooRollDice();
        int[] hp={0, 0};
        for (int i=0; i<3; i++){
            hp=cardAction(cardTable, i);
            setHP(getHP()+hp[1]);
            setSelfHP(getSelfHP()+hp[0]);
        }
    }

    public int getHP(){
        return Integer.parseInt(((TextView)findViewById(R.id.hp2)).getText().toString());
    }

    public void setHP(int hp){
        int dead;
        if (hp<=0){
            dead=Integer.parseInt(((TextView)findViewById(R.id.dead2)).getText().toString());
            if(dead<2){
                ((TextView)findViewById(R.id.dead2)).setText(Integer.toString(dead+1));
                ((TextView)findViewById(R.id.hp2)).setText(Integer.toString(5));
            }else{
                ((TextView)findViewById(R.id.dead2)).setText("3");
                ((TextView)findViewById(R.id.hp2)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы победили!");
                /*
                message="exit";
                messageRef.setValue(message);
                */
                removeDataFromDatabase();
                alertDialog.setIcon(R.drawable.end_win);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PlayActivity.this.finish();
                        //Intent i = new Intent(PlayActivity.this, StartActivity.class);
                        //startActivity(i);
                    }
                });
                alertDialog.show();
            }
        }else{
            ((TextView)findViewById(R.id.hp2)).setText(Integer.toString(hp));
        }
    }
    public int getSelfHP(){
        return Integer.parseInt(((TextView)findViewById(R.id.hp)).getText().toString());
    }

    public void setSelfHP(int hp){
        int dead;
        if (hp<=0){
            dead=Integer.parseInt(((TextView)findViewById(R.id.dead)).getText().toString());
            if(dead<2){
                ((TextView)findViewById(R.id.dead)).setText(Integer.toString(dead+1));
                ((TextView)findViewById(R.id.hp)).setText(Integer.toString(5));
            }else{
                ((TextView)findViewById(R.id.dead)).setText("3");
                ((TextView)findViewById(R.id.hp)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы проиграли! Не отчаивайтесь, это не конец света, есть же некромантия!");
                alertDialog.setIcon(R.drawable.end_dead);
                removeDataFromDatabase();
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PlayActivity.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
            }
        }else{
            ((TextView)findViewById(R.id.hp)).setText(Integer.toString(hp));
        }
    }

    public void cleanTable(){
        ((ImageView)findViewById(R.id.fstcard)).setImageResource(Constants.I_CARD);
        ((ImageView)findViewById(R.id.seccard)).setImageResource(Constants.K_CARD);
        ((ImageView)findViewById(R.id.thirdcard)).setImageResource(Constants.D_CARD);
        ((ImageView)findViewById(R.id.fstcard2)).setImageResource(Constants.I_CARD);
        ((ImageView)findViewById(R.id.seccard2)).setImageResource(Constants.K_CARD);
        ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(Constants.D_CARD);
        switch(role){
            case "guest":
                guestTable.cleanTable();
                break;
            case "host":
                hostTable.cleanTable();
                break;
        }
    }

    public void fooRollOneDice() {
        value3 = randomDiceValue();
        mMidImageView.animate().setDuration(1000).rotationYBy(360f).start();
        int res3 = getResources().getIdentifier("dice" + value3,
                "drawable", "com.example.projectapp");
        mMidImageView.animate().setDuration(1000).rotationYBy(3600f).start();
        mMidImageView.setImageResource(res3);
    }
    public void fooRollDice() {
        value1 = randomDiceValue();
        value2 = randomDiceValue();
        mLeftImageView.animate().setDuration(1000).rotationYBy(360f).start();
        int res1 = getResources().getIdentifier("dice" + value1,
                "drawable", "com.example.projectapp");
        mLeftImageView.setImageResource(res1);
        mLeftImageView.animate().setDuration(1000).rotationXBy(3600f).start();
        mRightImageView.animate().setDuration(1000).rotationXBy(360f).start();
        int res2 = getResources().getIdentifier("dice" + value2,
                "drawable", "com.example.projectapp");
        mRightImageView.setImageResource(res2);
        mRightImageView.animate().setDuration(1000).rotationYBy(3600f).start();
    }

    public void getPrompt(String mes){
        ((TextView)findViewById(R.id.prompt)).setText(mes);
    }

    void removeDataFromDatabase(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference("rooms/"+playerName);
        root.setValue(null);
    }
    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(PlayActivity.this, BattlePlayer.class));
        stopService(new Intent(PlayActivity.this, CommonPlayer.class));
    }

    // развернули приложение
    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(PlayActivity.this, BattlePlayer.class));
    }
}