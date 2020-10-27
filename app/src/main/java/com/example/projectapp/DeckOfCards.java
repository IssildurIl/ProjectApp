package com.example.projectapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.projectapp.Constants.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DeckOfCards extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.deckofcards);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        conferment();
        startService(new Intent(DeckOfCards.this, BattlePlayer.class));
        APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setEnabled(false);
        APP_PREFERENCE_DECKOFCARDS_CLICK_END.setVisibility(View.INVISIBLE);
        APP_PREFERENCE_DECKOFCARDS_CLICK_END.setEnabled(false);
        APP_PREFERENCE_DECKOFCARDS_CLICK_END.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_TAKE_GAMAGE, 1, 1, 0, 0, 1);
                APP_PREFERENCE_DECKOFCARDS_CLICK_END.setEnabled(false);
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.animate().setDuration(1000).rotationYBy(3600f).start();
                APP_PREFERENCE_DECKOFCARDS_CLICK_END.setVisibility(View.INVISIBLE);
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setEnabled(true);
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setVisibility(View.VISIBLE);
                iv_deck.setEnabled(true);
                tableAction(APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD);
                takeCardOpp(APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD);
                for (int i=0; i<3; i++){
                    APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[i]=0;
                    APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[i]=0;
                }
                opp_turn();
                tableActionOpp(APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD);
            }
        });
        Collections.shuffle(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN);
        APP_PREFERENCES_SETTINGS = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        getText();
        fstcard.setOnDragListener(dragListener);
        seccard.setOnDragListener(dragListener);
        thirdcard.setOnDragListener(dragListener);
        iv_card1.setOnLongClickListener(longClickListener);
        iv_card2.setOnLongClickListener(longClickListener);
        iv_card3.setOnLongClickListener(longClickListener);
        iv_card4.setOnLongClickListener(longClickListener);
        iv_card5.setOnLongClickListener(longClickListener);
        iv_card6.setOnLongClickListener(longClickListener);
        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_GIVE_CARDS, 1, 1, 0, 0, 1);
                takeCard(APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD);
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setEnabled(true);
                iv_deck.setEnabled(false);
            }
        });
        APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_DICE, 1, 1, 0, 0, 1);
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setEnabled(false);
                APP_PREFERENCE_DECKOFCARDS_CLICK_END.animate().setDuration(500).rotationYBy(3600f).start();
                APP_PREFERENCE_DECKOFCARDS_ROLL_DICES.setVisibility(View.INVISIBLE);
                APP_PREFERENCE_DECKOFCARDS_CLICK_END.setEnabled(true);
                APP_PREFERENCE_DECKOFCARDS_CLICK_END.setVisibility(View.VISIBLE);
                fooRollDice();
                fooRollOneDice();
            }
        });

    }

    public void conferment(){
        final TextView textnick = findViewById(R.id.playerNick);
        final TextView textnick2 = findViewById(R.id.playerNick2);
        //местные звуки
        APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        APP_PREFERENCE_DECKOFCARDS_SOUND_GIVE_CARDS = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.givecards, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_SECRET = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.dropcard, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_TAKE_GAMAGE = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.takedmg, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_DICE = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.dice, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_DARKNESS = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.darkness, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_FIRE = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.fire, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_NATURE = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.nature, 1);
        APP_PREFERENCE_DECKOFCARDS_SOUND_ILLUSION = APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.load(this, R.raw.illusion,1);
        APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_CARDS_ISTOCHNIK = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_CARDS_KACHESTVO = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_CARDS_DEISTVIE = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_MASSIVE_DARKNESS = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_MASSIVE_ELEMENT = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_MASSIVE_ILLUSION = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_MASSIVE_NATURE = new ArrayList<>();
        APP_PREFERENCE_DECKOFCARDS_MASSIVE_SECRET = new ArrayList<>();
        createArrayListOfCards(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN);
        createArrayListOfI(APP_PREFERENCE_DECKOFCARDS_CARDS_ISTOCHNIK);
        createArrayListOfK(APP_PREFERENCE_DECKOFCARDS_CARDS_KACHESTVO);
        createArrayListOfD(APP_PREFERENCE_DECKOFCARDS_CARDS_DEISTVIE);
        createArrayListOfDarkness(APP_PREFERENCE_DECKOFCARDS_MASSIVE_DARKNESS);
        createArrayListOfElement(APP_PREFERENCE_DECKOFCARDS_MASSIVE_ELEMENT);
        createArrayListOfIllusion(APP_PREFERENCE_DECKOFCARDS_MASSIVE_ILLUSION);
        createArrayListOfNature(APP_PREFERENCE_DECKOFCARDS_MASSIVE_NATURE);
        createArrayListOfSecret(APP_PREFERENCE_DECKOFCARDS_MASSIVE_SECRET);
        APP_PREFERENCE_DECKOFCARDS_ROLL_DICES = findViewById(R.id.Roll);
        APP_PREFERENCE_DECKOFCARDS_CLICK_END = findViewById(R.id.end);
        playerIcon = findViewById(R.id.playerIcon);
        iv_deck = findViewById(R.id.iv_deck);
        iv_card1 = findViewById(R.id.iv_card1);
        iv_card2 = findViewById(R.id.iv_card2);
        iv_card3 = findViewById(R.id.iv_card3);
        iv_card4 = findViewById(R.id.iv_card4);
        iv_card5 = findViewById(R.id.iv_card5);
        iv_card6 = findViewById(R.id.iv_card6);
        fstcard = findViewById(R.id.fstcard);
        seccard = findViewById(R.id.seccard);
        thirdcard = findViewById(R.id.thirdcard);
        APP_PREFERENCE_DECKOFCARDS_NICK_NAME = findViewById(R.id.playerNick);
        APP_PREFERENCE_DECKOFCARDS_FRAMELAYOUT_SPELLS = findViewById(R.id.spells);
        APP_PREFERENCE_DECKOFCARDS_FRAMELAYOUT_HAND = findViewById(R.id.hand);
        mLeftImageView = findViewById(R.id.imageview_left);
        mRightImageView = findViewById(R.id.imageview_right);
        mMidImageView =findViewById(R.id.imageview_mid);
        APP_PREFERENCE_DECKOFCARDS_NICK_NAME = findViewById(R.id.playerNick);
        ImageView backToMenu=findViewById(R.id.deckOfCards_BackButton);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopService(new Intent(DeckOfCards.this, BattlePlayer.class));
                Intent i = new Intent(DeckOfCards.this, StartActivity.class);
                startActivity(i);
            }
        });
    }
    public void getText() {
        String savedText = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_NAME, "");
        String mySavedIcone = APP_PREFERENCES_SETTINGS.getString(APP_PREFERENCES_ICONE, APP_PREFERENCES_STRING_ZERO);
        APP_PREFERENCE_DECKOFCARDS_NICK_NAME.setText(savedText);
        playerIcon.setImageResource(APP_PREFERENCE_DECKOFCARDS_PLAYER_ICONE[Integer.parseInt(mySavedIcone)]);
    }
    private int randomDiceValue() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
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
                drawable = ((ImageView) findViewById(R.id.fstcard2)).getDrawable();
                break;
            case R.id.seccard2:
                drawable = ((ImageView) findViewById(R.id.seccard2)).getDrawable();
                break;
            case R.id.thirdcard2:
                drawable = ((ImageView) findViewById(R.id.thirdcard2)).getDrawable();
                break;
            default:
                drawable = getDrawable(R.drawable.magic_back);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeckOfCards.this, R.style.CustomDialog);
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
            Drawable get_im = null;
            boolean flag;
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setVisibility(View.VISIBLE);
                    return true;
                case DragEvent.ACTION_DROP:
                    //11.05.2020 16:59
                    switch (view.getId()) {
                        case R.id.iv_card1:
                            //11.05.2020 16:38
                            get_im = iv_card1.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card2:
                            get_im = iv_card2.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card3:
                            get_im = iv_card3.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card4:
                            get_im = iv_card4.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card5:
                            get_im = iv_card5.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card6:
                            get_im = iv_card6.getDrawable();
                            flag = changeCard(v.getId(), get_im);
                            break;
                        default:
                            flag = false;
                    }
                    // 12.05.2020 13:35 смещение карт в руке
                    if (flag) {

                        APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD - 1] = detectCard(get_im);
                        int symb=whatSymbol(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD - 1], APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD -1);
                        //19.07 воспроизведение звука по символу
                        switch (symb){
                            case 1:
                                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_DARKNESS, 1, 1, 0, 0, 1);
                                break;
                            case 2:
                                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_FIRE, 1, 1, 0, 0, 1);
                                break;
                            case 3:
                                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_ILLUSION, 1, 1, 0, 0, 1);
                                break;
                            case 4:
                                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_NATURE, 1, 1, 0, 0, 1);
                                break;
                            case 5:
                                APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL.play(APP_PREFERENCE_DECKOFCARDS_SOUND_SECRET,1,1,0,0,1);
                                break;
                        }
                        switch (view.getId()) {
                            case R.id.iv_card1:
                                get_im = iv_card2.getDrawable();
                                iv_card1.setImageDrawable(get_im);
                            case R.id.iv_card2:
                                get_im = iv_card3.getDrawable();
                                iv_card2.setImageDrawable(get_im);
                            case R.id.iv_card3:
                                get_im = iv_card4.getDrawable();
                                iv_card3.setImageDrawable(get_im);
                            case R.id.iv_card4:
                                get_im = iv_card5.getDrawable();
                                iv_card4.setImageDrawable(get_im);
                            case R.id.iv_card5:
                                get_im = iv_card6.getDrawable();
                                iv_card5.setImageDrawable(get_im);
                            case R.id.iv_card6:
                                iv_card6.setImageDrawable(null);
                        }
                    }
                    view.setVisibility(View.VISIBLE );
                case DragEvent.ACTION_DRAG_ENDED:
                    iv_card1.setVisibility(View.VISIBLE);
                    return true;
            }
            return true;
        }
    };
    public int whatSymbol(int cardtable, int num){
        if (APP_PREFERENCE_DECKOFCARDS_MASSIVE_DARKNESS.contains(cardtable)){
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num]=1;
        }
        else
        if (APP_PREFERENCE_DECKOFCARDS_MASSIVE_ELEMENT.contains(cardtable)){
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num]=2;
        }
        else
        if (APP_PREFERENCE_DECKOFCARDS_MASSIVE_ILLUSION.contains(cardtable)){
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num]=3;
            }
        else
        if (APP_PREFERENCE_DECKOFCARDS_MASSIVE_NATURE.contains(cardtable)){
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num]=4;
        }
        else
        if (APP_PREFERENCE_DECKOFCARDS_MASSIVE_SECRET.contains(cardtable)){
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num]=5;
        }
        return APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[num];
    }
    public int NumSymbol(int countSymbol[], int symbol){
        //3008
        int num = 0;
        for (int i = 0; i < countSymbol.length; i++)
        {
          if (countSymbol[i]==symbol){
              num++;
          }
        }
        return num;
    }
    public int CouNumSymb(){
        int counter=1;
       if (APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[0]!=0){counter++;}
       if (APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[1]!=0 && APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[0]!= APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[1]){counter++;}
       if(APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[2]!=0 && APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[1]!= APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[2] && APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[2]!= APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[0]){counter++;}
        return counter;
    }
    //11.05.2020 16:48 проверка свободно ли место на столе
    public boolean changeCard(int Rid, Drawable picture) {
        switch (Rid) {
            case R.id.fstcard:
                if (detectCard(fstcard.getDrawable()) == 0) {
                    if (APP_PREFERENCE_DECKOFCARDS_CARDS_ISTOCHNIK.indexOf(detectCard(picture)) > -1) {
                        fstcard.setImageDrawable(picture);
                        APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD++;
                        return true;
                    }
                }
                return false;
            case R.id.seccard:
                if (detectCard(seccard.getDrawable()) == 0) {
                    if (APP_PREFERENCE_DECKOFCARDS_CARDS_KACHESTVO.indexOf(detectCard(picture)) > -1) {
                        seccard.setImageDrawable(picture);
                        APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD++;
                        return true;
                    }
                }
                return false;
            case R.id.thirdcard:
                if (detectCard(thirdcard.getDrawable()) == 0) {
                    if (APP_PREFERENCE_DECKOFCARDS_CARDS_DEISTVIE.indexOf(detectCard(picture)) > -1) {
                        thirdcard.setImageDrawable(picture);
                        APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD++;
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
    public int detectCard(Drawable picture) {
        Drawable.ConstantState imageViewDrawableState;
        for (int i = 0; i < APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size(); i++) {
            imageViewDrawableState = getResources().getDrawable(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(i)).getConstantState();
            if (imageViewDrawableState.equals(picture.getConstantState())) {
                //Toast.makeText(PlayActivity.this,""+main_cards.get(i),Toast.LENGTH_SHORT).show();
                return APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(i);
            }
        }
        return 0;
    }

    public void fooRollOneDice() {
        APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_3 = randomDiceValue();
        mMidImageView.animate().setDuration(1000).rotationYBy(360f).start();
        int res3 = getResources().getIdentifier("dice" + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_3,
                "drawable", "com.example.projectapp");
        mMidImageView.animate().setDuration(1000).rotationYBy(3600f).start();
        mMidImageView.setImageResource(res3);
    }
    public void fooRollDice() {
        APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 = randomDiceValue();
        APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2 = randomDiceValue();
        mLeftImageView.animate().setDuration(1000).rotationYBy(360f).start();
        int res1 = getResources().getIdentifier("dice" + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1,
                "drawable", "com.example.projectapp");
        mLeftImageView.setImageResource(res1);
        mLeftImageView.animate().setDuration(1000).rotationXBy(3600f).start();
        mRightImageView.animate().setDuration(1000).rotationXBy(360f).start();
        int res2 = getResources().getIdentifier("dice" + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2,
                "drawable", "com.example.projectapp");
        mRightImageView.setImageResource(res2);
        mRightImageView.animate().setDuration(1000).rotationYBy(3600f).start();

    }
    public void takeCard(int qnt) {
        cleanTable();
        if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
            return;
        }
        switch (qnt) {
            case 6:
                iv_card1.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 5:
                iv_card2.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 4:
                iv_card3.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 3:
                iv_card4.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 2:
                iv_card5.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 1:
                iv_card6.setImageResource(APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM));
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
        }
    }
    public void takeCardOpp(int qnt) {
        int guest_num = 6 - qnt;
        if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
            return;
        }
        switch (qnt) {
            case 6:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 5:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 4:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 3:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 2:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
                if (APP_PREFERENCE_DECKOFCARDS_CARD_NUM >= APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.size()) {
                    return;
                }
            case 1:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[guest_num] = APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN.get(APP_PREFERENCE_DECKOFCARDS_CARD_NUM);
                APP_PREFERENCE_DECKOFCARDS_CARD_NUM++;
                guest_num++;
            default:
                break;
        }
    }
    public void opp_turn() {
        APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD = 0;//1
        int num;
        for (num = 0; num < APP_PREFERENCE_DECKOFCARDS_CARD_OPP.length; num++) {
            if (APP_PREFERENCE_DECKOFCARDS_CARDS_ISTOCHNIK.indexOf(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]) > -1) {
                ((ImageView) findViewById(R.id.fstcard2)).setImageResource(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]);
                remove_card_opp(num);
                APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num];
                whatSymbol(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD], APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD);
                APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD++;
                break;
            }
        }
        for (num = 0; num < APP_PREFERENCE_DECKOFCARDS_CARD_OPP.length; num++) {
            if (APP_PREFERENCE_DECKOFCARDS_CARDS_KACHESTVO.indexOf(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]) > -1) {
                ((ImageView) findViewById(R.id.seccard2)).setImageResource(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]);
                remove_card_opp(num);
                APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num];
                whatSymbol(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD], APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD);
                APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD++;
                break;
            }
        }
        for (num = 0; num < APP_PREFERENCE_DECKOFCARDS_CARD_OPP.length; num++) {
            if (APP_PREFERENCE_DECKOFCARDS_CARDS_DEISTVIE.indexOf(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]) > -1) {
                ((ImageView) findViewById(R.id.thirdcard2)).setImageResource(APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num]);
                remove_card_opp(num);
                APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[num];
                whatSymbol(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD], APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD);
                APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD++;
                break;
            }
        }

    }
    public void remove_card_opp(int iter) {
        switch (iter) {
            case 0:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[0] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[1];
            case 1:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[1] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[2];
            case 2:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[2] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[3];
            case 3:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[3] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[4];
            case 4:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[4] = APP_PREFERENCE_DECKOFCARDS_CARD_OPP[5];
            case 5:
                APP_PREFERENCE_DECKOFCARDS_CARD_OPP[5] = 0;
        }
    }
    public int[] partCardAction(int symbol,int c1,int c2,int c3,int c4,int c5,int c6){
        int val[]=new int[2];
        switch (NumSymbol(APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS,symbol)){
       case 1:
           switch (APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1)
           {
               case 2: case 3: case 4:
               val= new int[]{c1, c2};
               case 5:case 6:
               val = new int[]{c3, c4};
           }
           Toast.makeText(DeckOfCards.this,"я нанес 1"+val,Toast.LENGTH_SHORT).show();
           break;
       case 2:
           switch (APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2){
               case 2: case 3:case 4:
                   val = new int[]{c1, c2};
               case 5: case 6: case 7: case 8: case 9:
                   val = new int[]{c3, c4};
               case 10: case 11: case 12:
                   val = new int[]{c5, c6};
           }
           Toast.makeText(DeckOfCards.this,"я нанес 2"+val,Toast.LENGTH_SHORT).show();
           break;
       case 3:
           switch (APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2 + APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_3){
               case 2: case 3:case 4:
                   val = new int[]{c1, c2};
               case 5: case 6: case 7: case 8: case 9:
                   val = new int[]{c3, c4};
               case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                   val = new int[]{c5, c6};
           }
           Toast.makeText(DeckOfCards.this,"я нанес 3"+val,Toast.LENGTH_SHORT).show();
           break;
            default:
                Toast.makeText(DeckOfCards.this,"я не сработал",Toast.LENGTH_SHORT).show();

   }
       return val;
    }
    public int[] cardAction(int card) {
        switch (card) {
            //Смэрт
            case R.drawable.d_darkness_1:
                partCardAction(1,-1,0,-2,0,-6,0);
                break;
            case R.drawable.d_darkness_2:
                partCardAction(1,0,-1,-4,0,-8,0);
                break;
            case R.drawable.d_darkness_3:
                partCardAction(1,-2,0,-3,0,-4,0);
                break;
            case R.drawable.d_darkness_4:
                partCardAction(1,-2,-1,-3,-1,-5,-1);
                break;
            case R.drawable.d_element_1:
                partCardAction(2,-1,0,-2,0,-4,0);
                break;
            case R.drawable.d_element_2:
                partCardAction(2,-1,0,-3,-1,-5,-1);
                break;
            case R.drawable.d_element_3:
                partCardAction(2,-1,0,-1,0,-7,0);
                break;
            case R.drawable.d_element_4:
                partCardAction(2,-1,0,-3,0,-4,0);
                break;
            case R.drawable.d_illusion_4:
                partCardAction(3,-1,0,-3,0,-4,0);
                break;
            case R.drawable.d_nature_1:
                partCardAction(4,-1,0,-2,0,-4,0);
                break;
            case R.drawable.d_nature_2:
                partCardAction(4,0,0,0,2,0,4);
                break;
            case R.drawable.d_nature_3:
                partCardAction(4,-2,0,-3,0,-6,0);
                break;
            case R.drawable.d_nature_4:
                partCardAction(4,-1,0,-2,0,-2*NumSymbol(APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS,4),0);
                break;
            case R.drawable.d_nature_5:
                partCardAction(4,-1,1,-2,0,-2,2);
                break;
            case R.drawable.i_darkness_1:
                switch (APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1){
                    case 1:case 2: case 3:
                        return new int[]{0, APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 *(-1)};
                    case 4: case 5: case 6:
                        return new int[]{0, APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1};
                    default:
                   //     Toast.makeText(Deckofcards.this,""+"i_darkness_1",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.drawable.i_darkness_3:
                return new int[]{APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 *(-1) , APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2 *(-1)};
            case R.drawable.i_darkness_4:
                return new int[]{-2,0};
            //Стихия
            case R.drawable.i_element_1:
                return new int[]{CouNumSymb()*(-1),0};
            case R.drawable.i_element_2:
                return new int[]{-3,0};
            case R.drawable.i_element_3:
                return new int[]{-3,0};
            case R.drawable.i_element_4:
                return new int[]{-3,0};
            //Иллюзия
            //Природа
            case R.drawable.i_nature_1:
                if (APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1 ==6) return new int[]{3,3};
                else return new int[]{0,3};
            case R.drawable.i_nature_2:
                return new int[]{0,CouNumSymb()};
            case R.drawable.i_nature_4:
                return new int[]{0,2};
            case R.drawable.i_nature_5:
                return new int[]{-3,0};
            //Секрет
            case R.drawable.k_darkness_1:
                partCardAction(1,-2,0,-4,-1,-5,-2);
                break;
            case R.drawable.k_darkness_2:
                partCardAction(1,0,-3,-3,0,-5,0);
                break;
            case R.drawable.k_darkness_4:
             return new int[]{(-2)*NumSymbol(APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS,1),0};
            case R.drawable.k_element_1:
             return new int[]{(-1)*NumSymbol(APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS,2),0};
            case R.drawable.k_element_2:
                partCardAction(2,-1,0,-3,-1,-5,0);
                break;
            case R.drawable.k_element_3:
                if (getHP() %2!=0){
                    return new int[]{(-1)*CouNumSymb(),0};
                }
            case R.drawable.k_nature_1:
                partCardAction(4,-1,0,-1,1,-3,3);
                break;
            case R.drawable.k_nature_3:
             return new int[]{(-2)*CouNumSymb(),0};
            case R.drawable.k_nature_4:
              return new int[]{-5, 0};
            case R.drawable.k_nature_5:
                return new int[]{-2, 0};
        }
        return new int[]{0, 0};
    }
    public void tableAction(int qnt) {
        int[] hp = {0, 0};
        for (int i = 0; i < qnt; i++) {
            hp = cardAction(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[i]);
            setHP(getHP() + hp[0]);
            setSelfHP(getSelfHP() + hp[1]);
        }
        String aaa="me";
        for (int i=0; i<3; i++){
            aaa+=" "+ APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[i];
        }
        //Toast.makeText(Deckofcards.this,aaa,Toast.LENGTH_SHORT).show();
    }
    public void tableActionOpp(int qnt) {
        fooRollDice();
        String aa="";
        fooRollOneDice();
        int[] hp = {0, 0};
        for (int i = 0; i < qnt; i++) {
            hp = cardAction(APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[i]);
            /*aa= ((TextView) findViewById(R.id.playerNick)).getText().toString();
            aa+=" "+hp[0]+";"+hp[1];
            ((TextView) findViewById(R.id.playerNick)).setText(aa);*/
            setHP(getHP() + hp[1]);
            setSelfHP(getSelfHP() + hp[0]);
        }
        String aaa="Opp";
        for (int i=0; i<3; i++){
            aaa+=" "+ APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[i];
        }
        //Toast.makeText(Deckofcards.this,aaa,Toast.LENGTH_SHORT).show();
    }
    public int getHP() {
        return Integer.parseInt(((TextView) findViewById(R.id.hp2)).getText().toString());
    }

    public void setHP(int hp) {
        int dead;
        if (hp <= 0) {
            dead = Integer.parseInt(((TextView) findViewById(R.id.dead2)).getText().toString());
            if (dead < 2) {
                ((TextView) findViewById(R.id.dead2)).setText(Integer.toString(dead + 1));
                ((TextView) findViewById(R.id.hp2)).setText(Integer.toString(5));
            } else {
                ((TextView) findViewById(R.id.dead2)).setText("3");
                ((TextView) findViewById(R.id.hp2)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(DeckOfCards.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы победили!");
                alertDialog.setIcon(R.drawable.end_win);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(DeckOfCards.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
                stopService(new Intent(DeckOfCards.this, BattlePlayer.class));
                startService(new Intent(DeckOfCards.this, CommonPlayer.class));
            }
        } else {
            ((TextView) findViewById(R.id.hp2)).setText(Integer.toString(hp));
        }
    }
    public int getSelfHP() {
        return Integer.parseInt(((TextView) findViewById(R.id.hp)).getText().toString());
    }
    public void setSelfHP(int hp) {
        int dead;
        if (hp <= 0) {
            dead = Integer.parseInt(((TextView) findViewById(R.id.dead)).getText().toString());
            if (dead < 2) {
                ((TextView) findViewById(R.id.dead)).setText(Integer.toString(dead + 1));
                ((TextView) findViewById(R.id.hp)).setText(Integer.toString(5));
            } else {
                ((TextView) findViewById(R.id.dead)).setText("3");
                ((TextView) findViewById(R.id.hp)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(DeckOfCards.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы проиграли! Не отчаивайтесь, это не конец света, есть же некромантия!");
                alertDialog.setIcon(R.drawable.end_dead);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(DeckOfCards.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
                stopService(new Intent(DeckOfCards.this, BattlePlayer.class));
                startService(new Intent(DeckOfCards.this, CommonPlayer.class));
            }
        } else {
            ((TextView) findViewById(R.id.hp)).setText(Integer.toString(hp));
        }
    }
    public void createArrayListOfCards(ArrayList main_cards) {
        main_cards.add(R.drawable.d_darkness_1);
        main_cards.add(R.drawable.d_darkness_2);
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);

        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);
        main_cards.add(R.drawable.d_element_3);
        main_cards.add(R.drawable.d_element_4);

        main_cards.add(R.drawable.d_illusion_4);

        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);
        main_cards.add(R.drawable.d_nature_4);
        main_cards.add(R.drawable.d_nature_5);
        main_cards.add(R.drawable.i_darkness_1);
        main_cards.add(R.drawable.i_darkness_3);
        main_cards.add(R.drawable.i_darkness_4);

        main_cards.add(R.drawable.i_element_1);
        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);
        main_cards.add(R.drawable.i_element_4);

        main_cards.add(R.drawable.i_nature_1);
        main_cards.add(R.drawable.i_nature_2);
        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);

        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);
        main_cards.add(R.drawable.k_darkness_4);

        main_cards.add(R.drawable.k_element_1);
        main_cards.add(R.drawable.k_element_2);
        main_cards.add(R.drawable.k_element_3);

        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_3);
        main_cards.add(R.drawable.k_nature_4);
        main_cards.add(R.drawable.k_nature_5);

        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfI(ArrayList main_cards) {
        main_cards.add(R.drawable.i_darkness_1);
        main_cards.add(R.drawable.i_darkness_3);
        main_cards.add(R.drawable.i_darkness_4);

        main_cards.add(R.drawable.i_element_1);
        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);
        main_cards.add(R.drawable.i_element_4);

        main_cards.add(R.drawable.i_nature_1);
        main_cards.add(R.drawable.i_nature_2);
        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);
    }
    public void createArrayListOfK(ArrayList main_cards) {
        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);
        main_cards.add(R.drawable.k_darkness_4);

        main_cards.add(R.drawable.k_element_1);
        main_cards.add(R.drawable.k_element_2);
        main_cards.add(R.drawable.k_element_3);

        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_3);
        main_cards.add(R.drawable.k_nature_4);
        main_cards.add(R.drawable.k_nature_5);

        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfD(ArrayList main_cards) {
        main_cards.add(R.drawable.d_darkness_1);
        main_cards.add(R.drawable.d_darkness_2);
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);

        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);
        main_cards.add(R.drawable.d_element_3);
        main_cards.add(R.drawable.d_element_4);

        main_cards.add(R.drawable.d_illusion_4);

        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);
        main_cards.add(R.drawable.d_nature_4);
        main_cards.add(R.drawable.d_nature_5);
    }
    public void createArrayListOfDarkness(ArrayList darkness_mas) {
        darkness_mas.add(R.drawable.d_darkness_1);
        darkness_mas.add(R.drawable.d_darkness_2);
        darkness_mas.add(R.drawable.d_darkness_3);
        darkness_mas.add(R.drawable.d_darkness_4);
        darkness_mas.add(R.drawable.i_darkness_1);
        darkness_mas.add(R.drawable.i_darkness_2);
        darkness_mas.add(R.drawable.i_darkness_3);
        darkness_mas.add(R.drawable.i_darkness_4);
        darkness_mas.add(R.drawable.k_darkness_1);
        darkness_mas.add(R.drawable.k_darkness_2);
        darkness_mas.add(R.drawable.k_darkness_3);
        darkness_mas.add(R.drawable.k_darkness_4);
    }
    public void createArrayListOfElement(ArrayList element_mas) {
        element_mas.add(R.drawable.d_element_1);
        element_mas.add(R.drawable.d_element_2);
        element_mas.add(R.drawable.d_element_3);
        element_mas.add(R.drawable.d_element_4);
        element_mas.add(R.drawable.i_element_1);
        element_mas.add(R.drawable.i_element_2);
        element_mas.add(R.drawable.i_element_3);
        element_mas.add(R.drawable.i_element_4);
        element_mas.add(R.drawable.k_element_1);
        element_mas.add(R.drawable.k_element_2);
        element_mas.add(R.drawable.k_element_3);
        element_mas.add(R.drawable.k_element_4);
    }
    public void createArrayListOfIllusion(ArrayList illusion_mas) {
        illusion_mas.add(R.drawable.d_illusion_1);
        illusion_mas.add(R.drawable.d_illusion_2);
        illusion_mas.add(R.drawable.d_illusion_3);
        illusion_mas.add(R.drawable.d_illusion_4);
        illusion_mas.add(R.drawable.i_illusion_1);
        illusion_mas.add(R.drawable.i_illusion_2);
        illusion_mas.add(R.drawable.i_illusion_3);
        illusion_mas.add(R.drawable.i_illusion_4);
        illusion_mas.add(R.drawable.k_illusion_1);
        illusion_mas.add(R.drawable.k_illusion_2);
        illusion_mas.add(R.drawable.k_illusion_3);
        illusion_mas.add(R.drawable.k_illusion_4);
    }
    public void createArrayListOfNature(ArrayList nature_mas){
        nature_mas.add(R.drawable.d_nature_1);
        nature_mas.add(R.drawable.d_nature_2);
        nature_mas.add(R.drawable.d_nature_3);
        nature_mas.add(R.drawable.d_nature_4);
        nature_mas.add(R.drawable.d_nature_5);
        nature_mas.add(R.drawable.i_nature_1);
        nature_mas.add(R.drawable.i_nature_2);
        nature_mas.add(R.drawable.i_nature_3);
        nature_mas.add(R.drawable.i_nature_4);
        nature_mas.add(R.drawable.i_nature_5);
        nature_mas.add(R.drawable.k_nature_1);
        nature_mas.add(R.drawable.k_nature_2);
        nature_mas.add(R.drawable.k_nature_3);
        nature_mas.add(R.drawable.k_nature_4);
        nature_mas.add(R.drawable.k_nature_5);
    }
    public void createArrayListOfSecret(ArrayList secret_mas){
        secret_mas.add(R.drawable.d_secret_1);
        secret_mas.add(R.drawable.d_secret_2);
        secret_mas.add(R.drawable.d_secret_3);
        secret_mas.add(R.drawable.d_secret_4);
        secret_mas.add(R.drawable.i_secret_1);
        secret_mas.add(R.drawable.i_secret_2);
        secret_mas.add(R.drawable.i_secret_3);
        secret_mas.add(R.drawable.i_secret_4);
        secret_mas.add(R.drawable.k_secret_1);
        secret_mas.add(R.drawable.k_secret_2);
        secret_mas.add(R.drawable.k_secret_3);
        secret_mas.add(R.drawable.k_secret_4);
    }
    public void cleanTable() {
        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);
        ((ImageView) findViewById(R.id.fstcard2)).setImageResource(R.drawable.i_card);
        ((ImageView) findViewById(R.id.seccard2)).setImageResource(R.drawable.k_card);
        ((ImageView) findViewById(R.id.thirdcard2)).setImageResource(R.drawable.d_card);
        for (int i=0; i<3; i++){
            APP_PREFERENCE_DECKOFCARDS_CARD_TABLE[i]=0;
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS[i]=0;
        }
        APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD = 0;
    }

}