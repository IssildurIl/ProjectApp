package com.example.projectapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LessonActivity extends AppCompatActivity{
    ImageButton Click;
    ImageButton rollDicesButton;
    TextView nick;
    ImageView iv_deck, iv_card1, iv_card2, iv_card3, iv_card4, iv_card5, iv_card6;
    public int[] card_table = {0, 0, 0};
    public int[] card_opp = {0, 0, 0, 0, 0, 0};
    ImageView fstcard, seccard, thirdcard;
    int card_num = 0, leave_card = 6, leave_opp = 6;
    ImageView PlayerIcon, mLeftImageView, mRightImageView,mMidImageView;
    // view.startAnimation(animRotate);
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    public static final String APP_PREFERENCES_ICONE = "0";
    private int[] mIcone = { R.drawable.icon_enchanter, R.drawable.icon_genie,
            R.drawable.icon_hogshouse, R.drawable.icon_krazztar, R.drawable.icon_lady,
            R.drawable.icon_princess, R.drawable.icon_spiritmaster,R.drawable.icon_wizard};
    //
    ArrayList<Integer> main_cards;
    ArrayList<Integer> cards_i;
    ArrayList<Integer> cards_k;
    ArrayList<Integer> cards_d;
    ArrayList<Integer> darkness_mas, element_mas, illusion_mas, nature_mas, secret_mas;
    int countSymbol[] = new int[3]; // 1,2,3,4,5
    // поле
    ImageView tab_im1;
    //
    FrameLayout spells, hand;
    int value1, value2, value3;
    //звуки в игре

    private SoundPool soundPool;
    private SoundPool less;
    private int sound1, secretsound, sound3, sound4,darknesssound,firesound,naturesound,illusionsound,hellosp,hintcardsp,hintplayersp,finallysp,dicetablesp,customsp ;

    //анимация
    //
    //голосовое
    private TextView result_tv;
    private Button startlisten,stoplisten,mute;
    private ImageButton hintbtn;
    private SpeechRecognizerManager mSpeechManager;
    //обучение
    TextView hintopp,hintdice,hintplayer,hinttable,hintcard,hintcardhelp,hintfinal,hello;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.trainactivity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        //голосовые команды
        //общие звуки
        stopService(new Intent(LessonActivity.this, commonplayer.class));
        //startService(new Intent(LessonActivity.this, battleplayer.class));
        //местные звуки
        soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        less= new SoundPool(7,AudioManager.STREAM_MUSIC,0);
        hellosp= less.load(this, R.raw.hello, 1);
        hintcardsp = less.load(this, R.raw.hintcard, 1);
        hintplayersp= less.load(this, R.raw.hintplayer, 1);
        finallysp= less.load(this, R.raw.soundend,1);
        dicetablesp= less.load(this, R.raw.dicetable, 1);
        customsp= less.load(this,R.raw.custom,1);
        sound1 = soundPool.load(this, R.raw.givecards, 1);
        secretsound = soundPool.load(this, R.raw.dropcard, 1);
        sound3 = soundPool.load(this, R.raw.takedmg, 1);
        sound4 = soundPool.load(this, R.raw.dice, 1);
        darknesssound = soundPool.load(this, R.raw.darkness, 1);
        firesound = soundPool.load(this, R.raw.fire, 1);
        naturesound = soundPool.load(this, R.raw.nature, 1);
        illusionsound = soundPool.load(this, R.raw.illusion,1);
        //музыка
        //
        hintdice=findViewById(R.id.hintdice);
        hintbtn= findViewById(R.id.NextHint);
        hintplayer =findViewById(R.id.hintplayer);
        hintopp =findViewById(R.id.hintopp);
        hintdice = findViewById(R.id.hintdice);
        hinttable =findViewById(R.id.hinttable);
        hintcard =findViewById(R.id.hintcard);
        hintcardhelp=findViewById(R.id.hintcardhelp);
        hintfinal=findViewById(R.id.hintfinal);
        hello=findViewById(R.id.hello);
        //
        trainnxt();
        final TextView textnick = (TextView)findViewById(R.id.playerNick);
        textnick .setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView textnick2 = (TextView)findViewById(R.id.playerNick2);
        textnick2.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        rollDicesButton = (ImageButton) findViewById(R.id.Roll);
        rollDicesButton.setEnabled(false);
        Click = (ImageButton) findViewById(R.id.end);
        Click.setVisibility(View.INVISIBLE);
        Click.setEnabled(false);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sound3, 1, 1, 0, 0, 1);
                Click.setEnabled(false);
                rollDicesButton.animate().setDuration(1000).rotationYBy(3600f).start();
                Click.setVisibility(View.INVISIBLE);
                rollDicesButton.setEnabled(true);
                rollDicesButton.setVisibility(View.VISIBLE);
                iv_deck.setEnabled(true);
                tableAction(leave_card);
                takeCardOpp(leave_opp);
                for (int i=0; i<3; i++){
                    card_table[i]=0;
                    countSymbol[i]=0;
                }
                opp_turn();
                tableActionOpp(leave_opp);
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
        darkness_mas = new ArrayList<>();
        createArrayListOfDarkness(darkness_mas);
        element_mas = new ArrayList<>();
        createArrayListOfElement(element_mas);
        illusion_mas = new ArrayList<>();
        createArrayListOfIllusion(illusion_mas);
        nature_mas = new ArrayList<>();
        createArrayListOfNature(nature_mas);
        secret_mas = new ArrayList<>();
        createArrayListOfSecret(secret_mas);
        //shuffle the cards
        Collections.shuffle(main_cards);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        PlayerIcon = findViewById(R.id.playerIcon);
        nick = findViewById(R.id.playerNick);
        spells = (FrameLayout) findViewById(R.id.spells);
        hand = (FrameLayout) findViewById(R.id.hand);
        //

        getText();
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


        iv_card1.setOnLongClickListener(longClickListener);
        iv_card2.setOnLongClickListener(longClickListener);
        iv_card3.setOnLongClickListener(longClickListener);
        iv_card4.setOnLongClickListener(longClickListener);
        iv_card5.setOnLongClickListener(longClickListener);
        iv_card6.setOnLongClickListener(longClickListener);
//


        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sound1, 1, 1, 0, 0, 1);
                takeCard(leave_card);
                rollDicesButton.setEnabled(true);
                iv_deck.setEnabled(false);
            }
        });

//кубики

        mLeftImageView = (ImageView) findViewById(R.id.imageview_left);
        mRightImageView = (ImageView) findViewById(R.id.imageview_right);
        mMidImageView =(ImageView)findViewById(R.id.imageview_mid);
        rollDicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(sound4, 1, 1, 0, 0, 1);
                rollDicesButton.setEnabled(false);
                Click.animate().setDuration(500).rotationYBy(3600f).start();
                rollDicesButton.setVisibility(View.INVISIBLE);
                Click.setEnabled(true);
                Click.setVisibility(View.VISIBLE);
                fooRollDice();
                fooRollOneDice();
            }
        });

    }

    public void getText() {

        nick = (TextView) findViewById(R.id.playerNick);
        String savedText = mSettings.getString(APP_PREFERENCES_NAME, "");
        String mysavedIcone = mSettings.getString(APP_PREFERENCES_ICONE, "0");
        nick.setText(savedText);
        PlayerIcon.setImageResource(mIcone[Integer.parseInt(mysavedIcone)]);
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
                //drawable = iv_deck.getDrawable();
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LessonActivity.this, R.style.CustomDialog);
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
                case DragEvent.ACTION_DRAG_STARTED:
                    view.setVisibility(View.INVISIBLE);
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setVisibility(View.INVISIBLE);
                    return true;
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
                        //cardAction(detectCard(get_im));
                        card_table[leave_card - 1] = detectCard(get_im);
                        int symb=whatSymbol(card_table[leave_card - 1],leave_card-1);
                        Toast.makeText(LessonActivity.this,""+symb,Toast.LENGTH_LONG).show();
                        //19.07 воспроизведение звука по символу
                        switch (symb){
                            case 1:
                                soundPool.play(darknesssound, 1, 1, 0, 0, 1);
                                break;
                            case 2:
                                soundPool.play(firesound, 1, 1, 0, 0, 1);
                                break;
                            case 3:
                                soundPool.play(illusionsound, 1, 1, 0, 0, 1);
                                break;
                            case 4:
                                soundPool.play(naturesound, 1, 1, 0, 0, 1);
                                break;
                            case 5:
                                soundPool.play(secretsound,1,1,0,0,1);
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
        if (darkness_mas.contains(cardtable)){
            countSymbol[num]=1;
        }
        else
        if (element_mas.contains(cardtable)){
            countSymbol[num]=2;
        }
        else
        if (illusion_mas.contains(cardtable)){
            countSymbol[num]=3;
        }
        else
        if (nature_mas.contains(cardtable)){
            countSymbol[num]=4;
        }
        else
        if (secret_mas.contains(cardtable)){
            countSymbol[num]=5;
        }
        return countSymbol[num];
    }
    public int NumSymbol(int countSymbol[], int symbol){
        int num = 1;
        for (int i = 0; i < countSymbol.length; i++)
        {
            if (countSymbol[i]==symbol){
                num+=1;
            }
        }
        return num;
    }
    public int CouNumSymb(){
        int counter=0;
        if (countSymbol[0]!=0){counter++;}
        if (countSymbol[1]!=0 && countSymbol[0]!=countSymbol[1]){counter++;}
        if(countSymbol[2]!=0 && countSymbol[1]!=countSymbol[2] && countSymbol[2]!=countSymbol[0]){counter++;}
        return counter;
    }
    //11.05.2020 16:48 проверка свободно ли место на столе
    public boolean changeCard(int Rid, Drawable picture) {
        switch (Rid) {
            case R.id.fstcard:
                if (detectCard(fstcard.getDrawable()) == 0) {
                    if (cards_i.indexOf(detectCard(picture)) > -1) {
                        fstcard.setImageDrawable(picture);
                        leave_card++;
                        return true;
                    }
                }
                return false;
            case R.id.seccard:
                if (detectCard(seccard.getDrawable()) == 0) {
                    if (cards_k.indexOf(detectCard(picture)) > -1) {
                        seccard.setImageDrawable(picture);
                        leave_card++;
                        return true;
                    }
                }
                return false;
            case R.id.thirdcard:
                if (detectCard(thirdcard.getDrawable()) == 0) {
                    if (cards_d.indexOf(detectCard(picture)) > -1) {
                        thirdcard.setImageDrawable(picture);
                        leave_card++;
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
        for (int i = 0; i < main_cards.size(); i++) {
            imageViewDrawableState = getResources().getDrawable(main_cards.get(i)).getConstantState();
            if (imageViewDrawableState.equals(picture.getConstantState())) {
                //Toast.makeText(PlayActivity.this,""+main_cards.get(i),Toast.LENGTH_SHORT).show();
                return main_cards.get(i);
            }
        }
        return 0;
    }
    public void gotoMenu(View view) {
        Intent i = new Intent(LessonActivity.this, StartActivity.class);
        startActivity(i);
        this.finish();
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
    public void takeCard(int qnt) {
        cleanTable();
        if (card_num >= main_cards.size()) {
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
    public void takeCardOpp(int qnt) {
        int guest_num = 6 - qnt;
        if (card_num >= main_cards.size()) {
            return;
        }
        switch (qnt) {
            case 6:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card12)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 5:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card22)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 4:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card32)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 3:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card42)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 2:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card52)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num >= main_cards.size()) {
                    return;
                }
            case 1:
                card_opp[guest_num] = main_cards.get(card_num);
                card_num++;
                //((ImageView)findViewById(R.id.iv_card62)).setImageResource(card_opp[guest_num]);
                guest_num++;
            default:
                break;
        }
    }
    public void opp_turn() {
        leave_opp = 0;
        int num;
        for (num = 0; num < card_opp.length; num++) {
            if (cards_i.indexOf(card_opp[num]) > -1) {
                ((ImageView) findViewById(R.id.fstcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp] = card_opp[num];
                whatSymbol(card_table[leave_opp],leave_opp);
                leave_opp++;
                break;
            }
        }
        for (num = 0; num < card_opp.length; num++) {
            if (cards_k.indexOf(card_opp[num]) > -1) {
                ((ImageView) findViewById(R.id.seccard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp] = card_opp[num];
                whatSymbol(card_table[leave_opp],leave_opp);
                leave_opp++;
                break;
            }
        }
        for (num = 0; num < card_opp.length; num++) {
            if (cards_d.indexOf(card_opp[num]) > -1) {
                ((ImageView) findViewById(R.id.thirdcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp] = card_opp[num];
                whatSymbol(card_table[leave_opp],leave_opp);
                leave_opp++;
                break;
            }
        }

    }

    public void remove_card_opp(int iter) {
        switch (iter) {
            case 0:
                card_opp[0] = card_opp[1];
            case 1:
                card_opp[1] = card_opp[2];
            case 2:
                card_opp[2] = card_opp[3];
            case 3:
                card_opp[3] = card_opp[4];
            case 4:
                card_opp[4] = card_opp[5];
            case 5:
                card_opp[5] = 0;
        }
    }

    public int[] cardAction(int card) {
        switch (card) {
            //Смэрт
            case R.drawable.d_darkness_1:
                switch (NumSymbol(countSymbol,1)){
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
                switch (NumSymbol(countSymbol,1)){
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
                switch (NumSymbol(countSymbol,1)){
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
                switch (NumSymbol(countSymbol,1)){
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
                switch (NumSymbol(countSymbol,2)){
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
                switch (NumSymbol(countSymbol,2)){
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
                switch (NumSymbol(countSymbol,2)){
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
                switch (NumSymbol(countSymbol,2)){
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
                switch (NumSymbol(countSymbol,3)){
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
                switch (NumSymbol(countSymbol,4)){
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
                switch (NumSymbol(countSymbol,4)){
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
                switch (NumSymbol(countSymbol,4)){
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
                switch (NumSymbol(countSymbol,4)){
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
                                return new int[]{-2*NumSymbol(countSymbol,4), 0};
                        }
                        break;
                    case 3:
                        switch (value1+value2+value3){
                            case 2: case 3:case 4:
                                return new int[]{-1, 0};
                            case 5: case 6: case 7: case 8: case 9:
                                return new int[]{-2, 0};
                            case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17:case 18:
                                return new int[]{-2*NumSymbol(countSymbol,4), 0};
                        }
                        break;
                }
                break;
            case R.drawable.d_nature_5:
                switch (NumSymbol(countSymbol,4)){
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
                return new int[]{-CouNumSymb(),0};
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
                return new int[]{0,+CouNumSymb()};
            case R.drawable.i_nature_4:
                return new int[]{0,+2};
            case R.drawable.i_nature_5:
                return new int[]{-3,0};
            //Секрет

            case R.drawable.k_darkness_1:
                switch (NumSymbol(countSymbol,1)){
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
                switch (NumSymbol(countSymbol,1)){
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
                return new int[]{-2*NumSymbol(countSymbol,1),0};

            case R.drawable.k_element_1:
                return new int[]{-NumSymbol(countSymbol,2),0};
            case R.drawable.k_element_2:
                switch (NumSymbol(countSymbol,2)){
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
                    return new int[]{-CouNumSymb(),0};
                }
            case R.drawable.k_nature_1:
                switch (NumSymbol(countSymbol,4)){
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
                return new int[]{-2*CouNumSymb(),0};
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
            hp = cardAction(card_table[i]);
            setHP(getHP() + hp[0]);
            setSelfHP(getSelfHP() + hp[1]);
        }
        String aaa="me";
        for (int i=0; i<3; i++){
            aaa+=" "+countSymbol[i];
        }
        //Toast.makeText(Deckofcards.this,aaa,Toast.LENGTH_SHORT).show();
    }

    public void tableActionOpp(int qnt) {
        fooRollDice();
        String aa="";
        fooRollOneDice();
        int[] hp = {0, 0};
        for (int i = 0; i < qnt; i++) {
            hp = cardAction(card_table[i]);
            /*aa= ((TextView) findViewById(R.id.playerNick)).getText().toString();
            aa+=" "+hp[0]+";"+hp[1];
            ((TextView) findViewById(R.id.playerNick)).setText(aa);*/
            setHP(getHP() + hp[1]);
            setSelfHP(getSelfHP() + hp[0]);
        }
        String aaa="Opp";
        for (int i=0; i<3; i++){
            aaa+=" "+countSymbol[i];
        }
        Toast.makeText(LessonActivity.this,aaa,Toast.LENGTH_SHORT).show();
    }


  /*
    public void opp_table(int qnt){
        for (int i=0; i<qnt; i++){
            if (cards_i.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.fstcard2)).setImageResource(card_table[i]);
            }
        }
        for (int i=0; i<qnt; i++){
            if (cards_k.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.seccard2)).setImageResource(card_table[i]);
            }
        }
        for (int i=0; i<qnt; i++){
            if (cards_d.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(card_table[i]);
            }
        }
    }
   */

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
                AlertDialog alertDialog = new AlertDialog.Builder(LessonActivity.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы победили!");
                alertDialog.setIcon(R.drawable.end_win);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(LessonActivity.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
                stopService(new Intent(LessonActivity.this, battleplayer.class));
                startService(new Intent(LessonActivity.this, commonplayer.class));
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
                AlertDialog alertDialog = new AlertDialog.Builder(LessonActivity.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы проиграли! Не отчаивайтесь, это не конец света, есть же некромантия!");
                alertDialog.setIcon(R.drawable.end_dead);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(LessonActivity.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
                stopService(new Intent(LessonActivity.this, battleplayer.class));
                startService(new Intent(LessonActivity.this, commonplayer.class));
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
            card_table[i]=0;
            countSymbol[i]=0;
        }
        leave_card = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(LessonActivity.this, battleplayer.class));
        startService(new Intent(LessonActivity.this, commonplayer.class));
        soundPool.release();
        soundPool = null;
    }
    public void trainnxt(){
        hello.setText("Добро пожаловать\n в обучение!\nНажмите кнопку ниже, чтобы начать");
        less.play(hellosp, 1, 1, 0, 0, 1);
        hello.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        hintplayer.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        hintopp.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hinttable.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hintcard.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hintcardhelp.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hintfinal.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hintdice.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));

        hintbtn.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View v) {
                i++;
                //12.08 22:18
                switch (i){
                    case 1:
                        hello.setVisibility(View.INVISIBLE);
                        hintdice.setVisibility(View.INVISIBLE);
                        hintopp.setVisibility(View.VISIBLE);
                        hintplayer.setVisibility(View.VISIBLE);
                        hintcard.setVisibility(View.INVISIBLE);
                        hintplayer.setText("здесь находятся данные о Вашем персонаже:\n" + "Его здоровье - черным цветом, и оставшиеся жизни - красным");
                        hintopp.setText("здесь находятся данные о персонаже противника:\n" + "Его здоровье - черным цветом, и оставшиеся жизни - красным");
                        less.play(hintplayersp, 1, 1, 0, 0, 1);
                        break;
                    case 2:
                        hintopp.setVisibility(View.INVISIBLE);
                        hintplayer.setVisibility(View.INVISIBLE);
                        hinttable.setVisibility(View.VISIBLE);
                        hintdice.setVisibility(View.VISIBLE);
                        hinttable.setText("Чтобы раздать карты, нажмите на колоду");
                        hintdice.setText("Эти кубики определят силу\n 'Могучего броска' вашего заклинания");
                        less.play(dicetablesp, 1, 1, 0, 0, 1);
                        break;
                    case 3:
                        hinttable.setVisibility(View.INVISIBLE);
                        hintdice.setVisibility(View.INVISIBLE);
                        hintcard.setVisibility(View.VISIBLE);
                        hintcardhelp.setVisibility(View.VISIBLE);
                        hintcard.setText("это ваши заклинания. они раздаются\n случайно. Нажмите на карту и потяните, чтобы переместить:\n"
                                +"Карта 'Источник' в желтый слот,\n"
                                +"'Качество' - в Оранжевый.\n"
                                +"'Действие' - в Малиновый слот.");
                        hintcardhelp.setText("Чтобы узнать больше информации, нажмите\n на карту.");
                        less.play(hintcardsp, 1, 1, 0, 0, 1);
                        break;
                    case 4:
                        hintcard.setVisibility(View.INVISIBLE);
                        hintcardhelp.setVisibility(View.INVISIBLE);
                        hintfinal.setVisibility(View.VISIBLE);
                        hintfinal.setText("Как только заклинание будет готово, нажмите ROLL. Дождитесь выпадения значений на кубиках\n и нажмите END OF TURN.\nВаша цель - убить противника заклинаниями. Удачи!");
                        less.play(finallysp, 1, 1, 0, 0, 1);
                        break;
                    case 5:
                        hintfinal.setText("     Если вы хотите закончить обучение, перейдите в\n Customization и уберите галочку");
                        less.play(customsp, 1, 1, 0, 0, 1);
                        break;
                    case 6:
                        hintfinal.setVisibility(View.INVISIBLE);
                        hintbtn.setVisibility(View.GONE);
                        break;
                    default:
                }
            }
        });
    }
}