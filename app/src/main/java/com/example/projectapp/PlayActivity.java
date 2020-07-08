package com.example.projectapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PlayActivity extends AppCompatActivity {
    Button Click;
    String playerName = "";
    String roomName = "";
    String role ="";
    String message ="";
    Button rollDicesButton;
    boolean flag1=true;
    TextView nick;
    ImageView iv_deck, iv_card1, iv_card2, iv_card3, iv_card4, iv_card5, iv_card6;
    public int[] c_card_table = { 0, 0, 0};
    public int[] c_table_opp = { 0, 0, 0};
    public int[] c_card_hand = { 0, 0, 0, 0, 0, 0};
    public int[] s_card_table = { 0, 0, 0};
    public int[] s_table_opp = { 0, 0, 0};
    public int[] s_card_hand = { 0, 0, 0, 0, 0, 0, 0};
    ImageView fstcard,seccard,thirdcard;
    int card_num=0, c_leave_card=6, c_leave_opp=0, s_leave_card=6, s_leave_opp=6;
    ImageView PlayerIcon, mLeftImageView,mRightImageView;
    int mIcon, mDesk;
    //
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    //
    ArrayList<Integer> main_cards;
    ArrayList<Integer> cards_i;
    ArrayList<Integer> cards_k;
    ArrayList<Integer> cards_d;
    // поле
    ImageView tab_im1;
    //
    FrameLayout spells, hand;
    int value1, value2;
    String mas_card="";
    //
    FirebaseDatabase database;
    DatabaseReference messageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Click = findViewById(R.id.clickbtn);
        Click.setEnabled(false);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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
        ((ImageView)findViewById(R.id.playerIcon)).setImageResource(R.drawable.icon_enchanter);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.setEnabled(false);
                //rollDicesButton.setEnabled(true);
                message= role+":";
                switch (role){
                    case "guest":
                        if (s_leave_card<4){
                            tableAction(s_leave_card);
                        }else{
                            tableAction(3);//
                        }
                        /*
                        if (s_leave_opp<4) {
                            tableActionOpp(s_leave_opp);
                        }
                         */



                        mas_card="";
                        takeCardToOpp(s_leave_opp);
                        //Toast.makeText(PlayActivity.this,""+s_card_hand[0]+"-"+s_card_hand[5],Toast.LENGTH_SHORT).show();
                        for (int i=0; i<s_leave_opp; i++){
                            mas_card+=s_card_hand[i]+"+";
                        }
                        message+="card:"+s_leave_opp+"+"+mas_card;
                        //Toast.makeText(PlayActivity.this,mas_card,Toast.LENGTH_SHORT).show();
                        mas_card="";
                        for (int i=0; i<s_leave_card; i++){
                            mas_card+=s_card_table[i]+"*";
                        }
                        message+="table:"+s_leave_card+"*"+mas_card;
                        break;
                    case "host":
                        if (c_leave_opp<4){
                            tableActionOpp(c_leave_opp);
                        }
                        /*
                        if (c_leave_card<4) {
                            tableAction(c_leave_card);
                        }

                         */
                        mas_card="";
                        for (int i=0; i<c_leave_card; i++){
                            mas_card+=c_card_table[i]+"*";
                        }
                        message+="table:"+c_leave_card+"*"+mas_card;
                }
                message+="dice"+value1+value2;
                messageRef.setValue(message);

                //opp_turn();

            }
        });
        main_cards = new ArrayList<>();
        createArrayListOfCards(main_cards);
        cards_i = new ArrayList<>();
        createArrayListOfI(cards_i);
        cards_k = new ArrayList<>();
        createArrayListOfK(cards_k);
        cards_d = new ArrayList<>();
        createArrayListOfD(cards_d);
        //shuffle the cards
        Collections.shuffle(main_cards);

        messageRef = database.getReference("rooms/"+ roomName+"/message");
        switch(role){
            case "host":
                message= role+":table:0*dice11";
                break;
            case "guest":

                takeCardToOpp(6);
                for (int i=0; i<6; i++){
                    mas_card+=s_card_hand[i]+"+";
                }
                message= role+":card:"+6+"+"+mas_card+"table:0*dice11";

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

        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);

        iv_card1.setOnLongClickListener(longClickListener);
        iv_card2.setOnLongClickListener(longClickListener);
        iv_card3.setOnLongClickListener(longClickListener);
        iv_card4.setOnLongClickListener(longClickListener);
        iv_card5.setOnLongClickListener(longClickListener);
        iv_card6.setOnLongClickListener(longClickListener);
        /*
        Intent intent = getIntent();
        mIcon = Integer.parseInt(intent.getStringExtra("I2"));
        mDesk = Integer.parseInt(intent.getStringExtra("D2"));
        String NickName = getIntent().getStringExtra("mnick");
        PlayerIcon.setImageResource(mIcon);
        // set background
        //getWindow().setBackgroundDrawableResource(mDesk);
        //nick.setText(NickName);

         */

        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(role){
                    case "guest":
                        takeCard(s_leave_card);
                }
                cleanTable();
            }
        });

//кубики
        rollDicesButton = (Button) findViewById(R.id.Roll);
        rollDicesButton.setEnabled(false); //
        mLeftImageView = (ImageView) findViewById(R.id.imageview_left);
        mRightImageView = (ImageView) findViewById(R.id.imageview_right);
        rollDicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.setEnabled(true);
                rollDicesButton.setEnabled(false);
                fooRollDice();
            }
        });
//
    }

    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text=""+ dataSnapshot.getValue(String.class);
                int d1, d2;
                int last_index, prev_index;
                String[] sub_str = { "", "", "", "", "", "", "" };
                if(role.equals("host")){
                    if(dataSnapshot.getValue(String.class).contains("guest:")){
                        rollDicesButton.setEnabled(true);
                        prev_index=text.indexOf("+");
                        c_leave_card=Integer.parseInt(text.substring(prev_index-1,prev_index));
                        last_index=text.lastIndexOf("+");

                        if (prev_index!=last_index) {
                            sub_str = text.substring(prev_index + 1, last_index).split("\\+");
                            //Toast.makeText(PlayActivity.this, sub_str.toString(), Toast.LENGTH_SHORT).show();
                            int j=0;
                            for (int i = (6-c_leave_card); i < 6; i++) {
                                c_card_hand[i] = Integer.parseInt(sub_str[j]);
                                j++;
                            }
                        }else{
                            for (int i = (6-c_leave_card); i < 6; i++) {
                                c_card_hand[i] = 0;
                            }
                        }
                        takeCardOpp(c_leave_card, c_card_hand);

                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);

                        prev_index=text.indexOf("*");
                        c_leave_opp=Integer.parseInt(text.substring(prev_index-1,prev_index));
                        last_index=text.lastIndexOf("*");
                        if (prev_index!=last_index) {
                            sub_str = text.substring(prev_index + 1, last_index).split("\\*");
                            for (int i = 0; i < c_leave_opp; i++) {
                                c_table_opp[i] = Integer.parseInt(sub_str[i]);
                            }
                            setTable(c_leave_opp, c_table_opp);
                            tableAction(c_leave_opp); //c_leave_card
                        }
                        c_leave_card=0;
                        /*
                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);
                                                */
                        Click.setEnabled(true);
                        //((TextView)findViewById(R.id.test)).setText(text);
                        Toast.makeText(PlayActivity.this,text.replace("guest:",""),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(dataSnapshot.getValue(String.class).contains("host:")){
                        rollDicesButton.setEnabled(true);

                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);

                        prev_index=text.indexOf("*");
                        s_leave_opp=Integer.parseInt(text.substring(prev_index-1,prev_index));
                        last_index=text.lastIndexOf("*");
                        if (prev_index!=last_index) {
                            sub_str = text.substring(prev_index + 1, last_index).split("\\*");
                            for (int i = 0; i < s_leave_opp; i++) {
                                s_table_opp[i] = Integer.parseInt(sub_str[i]);
                            }
                            setTable(s_leave_opp, s_table_opp);
                            tableActionOpp(s_leave_opp);
                        }
                        /*
                        prev_index=text.indexOf("dice");
                        d1=Integer.parseInt(text.substring(prev_index+4,prev_index+5));
                        d2=Integer.parseInt(text.substring(prev_index+5,prev_index+6));
                        fooSetDice(d1,d2);
                                              */
                        //((TextView)findViewById(R.id.test)).setText(text);
                        Toast.makeText(PlayActivity.this,text.replace("host:",""),Toast.LENGTH_SHORT).show();
                    }
            }
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
                                s_card_table[s_leave_card-1]=detectCard(get_im);
                                break;
                            case "host":
                                c_card_table[c_leave_card-1]=detectCard(get_im);
                                break;
                        }
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
                        }
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
                    if (cards_i.indexOf(detectCard(picture)) > -1) {
                        fstcard.setImageDrawable(picture);
                        switch(role){
                            case "guest":
                                s_leave_card++;
                                break;
                            case "host":
                                c_leave_card++;
                                break;
                        }
                        return true;
                    }
                }
                return false;
            case R.id.seccard:
                if (detectCard(seccard.getDrawable())==0){
                    if (cards_k.indexOf(detectCard(picture)) > -1) {
                        seccard.setImageDrawable(picture);
                        switch(role){
                            case "guest":
                                s_leave_card++;
                                break;
                            case "host":
                                c_leave_card++;
                                break;
                        }
                        return true;
                    }
                }
                return false;
            case R.id.thirdcard:
                if (detectCard(thirdcard.getDrawable())==0){
                    if (cards_d.indexOf(detectCard(picture)) > -1) {
                        thirdcard.setImageDrawable(picture);
                        switch(role){
                            case "guest":
                                s_leave_card++;
                                break;
                            case "host":
                                c_leave_card++;
                                break;
                        }
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
        for (int i=0; i<main_cards.size(); i++){
            imageViewDrawableState=getResources().getDrawable(main_cards.get(i)).getConstantState();
            if (imageViewDrawableState.equals(picture.getConstantState())){
                //Toast.makeText(PlayActivity.this,""+main_cards.get(i),Toast.LENGTH_SHORT).show();
                return main_cards.get(i);
            }
        }
        return 0;
    }

    public void gotoMenu(View view) {
        Intent i = new Intent(PlayActivity.this, StartActivity.class);
        startActivity(i);
        this.finish();
    }

    public void fooRollDice(){
        value1 = randomDiceValue();
        value2 = randomDiceValue();
        int res1 = getResources().getIdentifier("dice" + value1,
                "drawable", "com.example.projectapp");
        int res2 = getResources().getIdentifier("dice" + value2,
                "drawable", "com.example.projectapp");
        mLeftImageView.setImageResource(res1);
        mRightImageView.setImageResource(res2);
    }

    public void fooSetDice(int d1, int d2){
        value1 = d1;
        value2 = d2;
        int res1 = getResources().getIdentifier("dice" + value1,
                "drawable", "com.example.projectapp");
        int res2 = getResources().getIdentifier("dice" + value2,
                "drawable", "com.example.projectapp");
        mLeftImageView.setImageResource(res1);
        mRightImageView.setImageResource(res2);
    }

    public void takeCard(int qnt){
        if (card_num>=main_cards.size()){
            return;
        }
        switch (qnt) {
            case 6:
                iv_card1.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 5:
                iv_card2.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 4:
                iv_card3.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 3:
                iv_card4.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 2:
                iv_card5.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 1:
                iv_card6.setImageResource(main_cards.get(card_num));
                card_num++;
        }
    }

    public void takeCardToOpp(int qnt){
        for (int i=0; i<qnt; i++){
            if (card_num<main_cards.size()){
                s_card_hand[i]=main_cards.get(card_num);
                card_num++;
            }else{
                s_card_hand[i]=0;
            }
        }
    }

    public void takeCardOpp(int qnt, int[] mas){
        int guest_num=6-qnt;
        switch (qnt){
            case 6:
                ((ImageView)findViewById(R.id.iv_card1)).setImageResource(mas[guest_num]);
                guest_num++;
            case 5:
                ((ImageView)findViewById(R.id.iv_card2)).setImageResource(mas[guest_num]);
                guest_num++;

            case 4:
                ((ImageView)findViewById(R.id.iv_card3)).setImageResource(mas[guest_num]);
                guest_num++;
            case 3:
                ((ImageView)findViewById(R.id.iv_card4)).setImageResource(mas[guest_num]);
                guest_num++;
            case 2:
                ((ImageView)findViewById(R.id.iv_card5)).setImageResource(mas[guest_num]);
                guest_num++;
            case 1:
                ((ImageView)findViewById(R.id.iv_card6)).setImageResource(mas[guest_num]);
                guest_num++;
            default:
                break;
        }
    }

    public void setTable(int qnt, int[] mas){
        int iter=0;
        while(iter<qnt) {
            if (cards_i.indexOf(mas[iter]) > -1) {
                ((ImageView) findViewById(R.id.fstcard2)).setImageResource(mas[iter]);
                iter++;
                if (iter >= qnt) {
                    return;
                }
            }
            if (cards_k.indexOf(mas[iter]) > -1) {
                ((ImageView) findViewById(R.id.seccard2)).setImageResource(mas[iter]);
                iter++;
                if (iter >= qnt) {
                    return;
                }
            }
            if (cards_d.indexOf(mas[iter]) > -1) {
                ((ImageView) findViewById(R.id.thirdcard2)).setImageResource(mas[iter]);
                iter++;
                if (iter >= qnt) {
                    return;
                }
            }
        }
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

    public int[] cardAction(int card){
        switch(card){
            case R.drawable.d_darkness_3:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_darkness_4:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, -1};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, -1};
                    case 10: case 11: case 12:
                        return new int[] {-5, -1};
                }
                break;
            case R.drawable.d_element_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-2, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_element_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-5, 0};
                }
                break;
            case R.drawable.d_illusion_4:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_nature_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-2, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_nature_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {0, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {0, 2};
                    case 10: case 11: case 12:
                        return new int[] {0, 4};
                }
                break;
            case R.drawable.d_nature_3:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-6, 0};
                }
                break;
            case R.drawable.i_element_2:
            case R.drawable.i_element_3:
                return new int[] {-3, 0};
            case R.drawable.i_nature_4:
                return new int[] {0, 2};
            case R.drawable.i_nature_5://
                return new int[] {-3, 0};
            case R.drawable.i_secret_4:
                return new int[] {-3, 0};
            case R.drawable.k_darkness_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-4, -1};
                    case 10: case 11: case 12:
                        return new int[] {-5, -2};
                }
                break;
            case R.drawable.k_darkness_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {0, -3};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-5, 0};
                }
                break;
            case R.drawable.k_nature_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-1, 1};
                    case 10: case 11: case 12:
                        return new int[] {-3, 3};
                }
                break;
            case R.drawable.k_nature_4:
                return new int[] {-1, 0};
            case R.drawable.k_secret_1:
                return new int[] {-2, 0};
        }
        return new int[] {0, 0};
    }

    public void tableAction(int qnt){
        int[] hp={0, 0};
        switch(role){
            case "guest":
                for (int i=0; i<qnt; i++){
                    hp=cardAction(s_card_table[i]);
                    setHP(getHP()+hp[0]);
                    setSelfHP(getSelfHP()+hp[1]);
                }
                break;
            case "host":
                for (int i=0; i<qnt; i++){
                    hp=cardAction(c_card_table[i]);
                    setHP(getHP()+hp[0]);
                    setSelfHP(getSelfHP()+hp[1]);
                }
                break;
        }
    }

    public void tableActionOpp(int qnt){
        if (qnt==6){return;}
        fooRollDice();
        switch(role){
            case "guest":
                int[] hp={0, 0};
                for (int i=0; i<qnt; i++){
                    hp=cardAction(s_table_opp[i]);
                    setHP(getHP()+hp[1]);
                    setSelfHP(getSelfHP()+hp[0]);
                }
                break;
            case "host":
                for (int i=0; i<qnt; i++){
                    hp=cardAction(c_table_opp[i]);
                    setHP(getHP()+hp[1]);
                    setSelfHP(getSelfHP()+hp[0]);
                }
                break;
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
                alertDialog.setIcon(R.drawable.end_win);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PlayActivity.this, StartActivity.class);
                        startActivity(i);
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

    public void createArrayListOfCards(ArrayList main_cards){
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);

        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);

        main_cards.add(R.drawable.d_illusion_4);

        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);

        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);

        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);

        main_cards.add(R.drawable.i_secret_4);

        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);

        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_4);

        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfI(ArrayList main_cards){
        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);
        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);
        main_cards.add(R.drawable.i_secret_4);
    }
    public void createArrayListOfK(ArrayList main_cards){
        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);
        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_4);
        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfD(ArrayList main_cards){
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);
        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);
        main_cards.add(R.drawable.d_illusion_4);
        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);
    }

    public void cleanTable(){
        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);
        ((ImageView)findViewById(R.id.fstcard2)).setImageResource(R.drawable.i_card);
        ((ImageView)findViewById(R.id.seccard2)).setImageResource(R.drawable.k_card);
        ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(R.drawable.d_card);
        switch(role){
            case "guest":
                s_leave_card=0;
                break;
            case "host":
                c_leave_card=0;
                break;
        }
    }
}