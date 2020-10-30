package com.example.projectapp;

import android.content.SharedPreferences;
import android.media.SoundPool;
import android.view.GestureDetector;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class Constants {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    public static final String APP_PREFERENCES_ICONE="0";
    public static final String APP_PREFERENCES_CB="lesson";
    public static final String APP_PREFERENCES_NETNAME = "net player name";
    public static final String APP_PREFERENCES_EMAIL = "mail";
    public static final String APP_PREFERENCES_PASS="pass";
    public static SharedPreferences APP_PREFERENCES_SETTINGS;
    public static String APP_PREFERENCES_STRING_ZERO ="0";
    public static String APP_PREFERENCES_STRING_ONE ="1";
    public static int APP_PREFERENCES_INT_ZERO=0;
    public static int APP_PREFERENCES_INT_ONE=1;
    public static FirebaseAuth APP_PREFERENCE_FIREBASE_AUTHENTIFICATION;
    //BattlePlayer/CommonPlayer


    //Customization
    public static String APP_PREFERENCES_CUSTOMIZATION_SAVED_TEXT;
    public static String APP_PREFERENCES_CUSTOMIZATION_SAVED_FOTO;
    public static String APP_PREFERENCES_CUSTOMIZATION_CHECK;
    public static int APP_PREFERENCES_CUSTOMIZATION_NEW_ICONE_POSITION;
    public static int APP_PREFERENCES_CUSTOMIZATION_MICONE_POSITION = 0;
    public static final int APP_PREFERENCES_CUSTOMIZATION_SWIPE_MIN_DISTANCE = 120;
    public static final int APP_PREFERENCES_CUSTOMIZATION_SWIPE_MAX_OFF_PATH = 250;
    public static final int APP_PREFERENCES_CUSTOMIZATION_SWIPE_THRESHOLD_VELOCITY = 100;
    public static final int[] APP_PREFERENCES_CUSTOMIZATION_MASS_ICONE = { R.drawable.icon_enchanter, R.drawable.icon_genie,
            R.drawable.icon_hogshouse, R.drawable.icon_krazztar, R.drawable.icon_lady,
            R.drawable.icon_princess, R.drawable.icon_spiritmaster,R.drawable.icon_wizard};
    public static ImageSwitcher APP_PREFERENCES_CUSTOMIZATION_ICONE_SWITCHER;
    public static CheckBox APP_PREFERENCES_CUSTOMIZATION_CHECK_LESSON;
    public static GestureDetector APP_PREFERENCES_CUSTOMIZATION_GESTURE_DESTRUCTOR;
    public static TextView APP_PREFERENCES_CUSTOMIZATION_TITLE;
    public static TextView APP_PREFERENCES_CUSTOMIZATION_HINT;
    public static TextView APP_PREFERENCES_CUSTOMIZATION_BUTTON_BACK;
    public static EditText APP_PREFERENCES_CUSTOMIZATION_NICK;
    public static Animation APP_PREFERENCE_CUSTOMIZATION_IN_ANIMATION;
    public static Animation APP_PREFERENCE_CUSTOMIZATION_OUT_ANIMATION;

    //DeckOfCards
    public static ImageButton APP_PREFERENCE_DECKOFCARDS_CLICK_END, APP_PREFERENCE_DECKOFCARDS_ROLL_DICES;
    public static TextView APP_PREFERENCE_DECKOFCARDS_NICK_NAME;
    public static ImageView iv_deck, iv_card1, iv_card2, iv_card3, iv_card4, fstcard, seccard, thirdcard,
            iv_card5, iv_card6, playerIcon, mLeftImageView, mRightImageView,mMidImageView;
    public static int[] APP_PREFERENCE_DECKOFCARDS_CARD_TABLE = {0, 0, 0}, APP_PREFERENCE_DECKOFCARDS_CARD_OPP = {0, 0, 0, 0, 0, 0},
            APP_PREFERENCE_DECKOFCARDS_PLAYER_ICONE = { R.drawable.icon_enchanter, R.drawable.icon_genie, R.drawable.icon_hogshouse, R.drawable.icon_krazztar, R.drawable.icon_lady,
            R.drawable.icon_princess, R.drawable.icon_spiritmaster,R.drawable.icon_wizard},
            APP_PREFERENCE_DECKOFCARDS_COUNT_SYMBOLS =new int[3];// 1,2,3,4,5
    public static int APP_PREFERENCE_DECKOFCARDS_CARD_NUM = 0, APP_PREFERENCE_DECKOFCARDS_LEAVE_PLAYER_CARD = 6, APP_PREFERENCE_DECKOFCARDS_LEAVE_OPP_CARD = 6,
            APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_1, APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_2, APP_PREFERENCE_DECKOFCARDS_DICE_VALUE_3, APP_PREFERENCE_DECKOFCARDS_SOUND_GIVE_CARDS,
            APP_PREFERENCE_DECKOFCARDS_SOUND_SECRET, APP_PREFERENCE_DECKOFCARDS_SOUND_TAKE_GAMAGE, APP_PREFERENCE_DECKOFCARDS_SOUND_DICE, APP_PREFERENCE_DECKOFCARDS_SOUND_DARKNESS,
            APP_PREFERENCE_DECKOFCARDS_SOUND_FIRE, APP_PREFERENCE_DECKOFCARDS_SOUND_NATURE, APP_PREFERENCE_DECKOFCARDS_SOUND_ILLUSION;
    public static ArrayList<Integer> APP_PREFERENCE_DECKOFCARDS_CARDS_MAIN, APP_PREFERENCE_DECKOFCARDS_CARDS_ISTOCHNIK, APP_PREFERENCE_DECKOFCARDS_CARDS_KACHESTVO, APP_PREFERENCE_DECKOFCARDS_CARDS_DEISTVIE,
            APP_PREFERENCE_DECKOFCARDS_MASSIVE_DARKNESS, APP_PREFERENCE_DECKOFCARDS_MASSIVE_ELEMENT, APP_PREFERENCE_DECKOFCARDS_MASSIVE_ILLUSION, APP_PREFERENCE_DECKOFCARDS_MASSIVE_NATURE, APP_PREFERENCE_DECKOFCARDS_MASSIVE_SECRET;
    public static FrameLayout APP_PREFERENCE_DECKOFCARDS_FRAMELAYOUT_SPELLS, APP_PREFERENCE_DECKOFCARDS_FRAMELAYOUT_HAND;
    public static SoundPool APP_PREFERENCE_DECKOFCARDS_SOUNDPOOL;
    //LessonActivity
    public static TextView APP_PREFERENCE_LESSONACTIVITY_HINT_OPP, APP_PREFERENCE_LESSONACTIVITY_HINT_DICE, APP_PREFERENCE_LESSONACTIVITY_HINT_PLAYER,
            APP_PREFERENCE_LESSONACTIVITY_HINT_TABLE, APP_PREFERENCE_LESSONACTIVITY_HINT_CARD, APP_PREFERENCE_LESSONACTIVITY_HINT_CARD_HELP, APP_PREFERENCE_LESSONACTIVITY_HINT_FINAL, APP_PREFERENCE_LESSONACTIVITY_HINT_HELLO;
    public static int APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_HELLO, APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_HINTCARD, APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_HINTPLAYER, APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_FINALLY, APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_DICETABLE, APP_PREFERENCE_LESSONACTIVITY_SOUNDPOOL_CUSTOM;
    public static SoundPool APP_PREFERENCE_LESSONACTIVITY_LESS;
    public static ImageButton APP_PREFERENCE_LESSONACTIVITY_HINT_BUTTON;
    public static final int[] APP_PREFERENCE_LESSONACTIVITY_STREAM = new int[5];
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_FST_TEXT ="Добро пожаловать\n в обучение!\n Нажмите кнопку ниже, чтобы начать";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_SEC_TEXT ="Здесь находятся данные о Вашем персонаже:\n  Его здоровье - черным цветом, и оставшиеся жизни - красным";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_THRD_TEXT ="Здесь находятся данные о персонаже противника:\n  Его здоровье - черным цветом, и оставшиеся жизни - красным";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_FRTH_TEXT= "Чтобы раздать карты, нажмите на колоду";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_FIFTH_TEXT ="Эти кубики определят силу\n \"Могучего броска\" вашего заклинания";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_SIXTH_TEXT ="Это ваши заклинания. они раздаются\n случайно. Нажмите на карту и потяните, чтобы переместить:\n Карта \"Источник\" в желтый слот,\n  \"Качество\" - в Оранжевый,\n \"Действие\" - в Малиновый.";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_SEVENTH_TEXT = "Чтобы узнать больше информации, нажмите\n на карту.";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_EIGTH_TEXT = "Как только заклинание будет готово, нажмите ROLL. Дождитесь выпадения значений на кубиках\n и нажмите END OF TURN.\nВаша цель - убить противника заклинаниями. Удачи!";
    public static final String APP_PREFERENCE_LESSONACTIVITY_HINT_NINGTH_TEXT = "     Если вы хотите закончить обучение, перейдите в\n Customization и уберите галочку";
    //ListOfPlayer
    public static final String APP_PREFERENCE_LISTOFPLAYER_DEFAULT_PLAYER="default player";
    public static ListView APP_PREFERENCE_LISTOFPLAYER_PLAYER_LIST;
    public static Button createroom,chngacc,gotomenu;
    public static TextView nick;
    public static List<String> roomsList;
    public static String playerName= "", roomName= "";
    public static FirebaseDatabase APP_PREFERENCE_FIREBASE_DATABASE;
    public static DatabaseReference roomRef, roomsRef;
    //LogInActivity
    public static EditText APP_PREFERENCE_LOGINACTIVITY_INPUT_EMAIL, APP_PREFERENCE_LOGINACTIVITY_INPUT_PASSWORD;
    public static Button APP_PREFERENCE_LOGINACTIVITY_BUTTON_SIGN_UP, APP_PREFERENCE_LOGINACTIVITY_BUTTON_LOG_IN, APP_PREFERENCE_LOGINACTIVITY_BUTTON_RESET;
    //PlayActivity

    //ResPas
    public static EditText inputEmail;
    public static Button btnReset, btnBack;
    public static ProgressBar progressBar;
    //StartActivity

    //all Play
    public static ArrayList<Integer> MAIN_CARDS  = new ArrayList<>(Arrays.asList(R.drawable.d_darkness_1, R.drawable.d_darkness_2, R.drawable.d_darkness_3, R.drawable.d_darkness_4, R.drawable.d_element_1, R.drawable.d_element_2, R.drawable.d_element_3, R.drawable.d_element_4, R.drawable.d_illusion_4, R.drawable.d_nature_1, R.drawable.d_nature_2, R.drawable.d_nature_3, R.drawable.d_nature_4, R.drawable.d_nature_5, R.drawable.i_darkness_1, R.drawable.i_darkness_3, R.drawable.i_darkness_4, R.drawable.i_element_1, R.drawable.i_element_2, R.drawable.i_element_3, R.drawable.i_element_4, R.drawable.i_nature_1, R.drawable.i_nature_2, R.drawable.i_nature_4, R.drawable.i_nature_5, R.drawable.k_darkness_1, R.drawable.k_darkness_2, R.drawable.k_darkness_4, R.drawable.k_element_1, R.drawable.k_element_2, R.drawable.k_element_3, R.drawable.k_nature_1, R.drawable.k_nature_3, R.drawable.k_nature_4, R.drawable.k_nature_5, R.drawable.k_secret_1));

    public static HashSet<Integer> I_MAIN_CARDS = new HashSet<>(Arrays.asList(R.drawable.i_darkness_1, R.drawable.i_darkness_3, R.drawable.i_darkness_4, R.drawable.i_element_1, R.drawable.i_element_2, R.drawable.i_element_3, R.drawable.i_element_4, R.drawable.i_nature_1, R.drawable.i_nature_2, R.drawable.i_nature_4, R.drawable.i_nature_5));

    public static HashSet<Integer> K_MAIN_CARDS  = new HashSet<>(Arrays.asList(R.drawable.k_darkness_1, R.drawable.k_darkness_2, R.drawable.k_darkness_4, R.drawable.k_element_1, R.drawable.k_element_2, R.drawable.k_element_3, R.drawable.k_nature_1, R.drawable.k_nature_3, R.drawable.k_nature_4, R.drawable.k_nature_5, R.drawable.k_secret_1));

    public static HashSet<Integer> D_MAIN_CARDS  = new HashSet<>(Arrays.asList(R.drawable.d_darkness_1, R.drawable.d_darkness_2, R.drawable.d_darkness_3, R.drawable.d_darkness_4, R.drawable.d_element_1, R.drawable.d_element_2, R.drawable.d_element_3, R.drawable.d_element_4, R.drawable.d_illusion_4, R.drawable.d_nature_1, R.drawable.d_nature_2, R.drawable.d_nature_3, R.drawable.d_nature_4, R.drawable.d_nature_5));

    public static HashSet<Integer> DARKNESS_MAS  = new HashSet<>(Arrays.asList(R.drawable.d_darkness_1, R.drawable.d_darkness_2, R.drawable.d_darkness_3, R.drawable.d_darkness_4, R.drawable.i_darkness_1, R.drawable.i_darkness_2, R.drawable.i_darkness_3, R.drawable.i_darkness_4, R.drawable.k_darkness_1, R.drawable.k_darkness_2, R.drawable.k_darkness_3, R.drawable.k_darkness_4));

    public static HashSet<Integer> ELEMENT_MAS  = new HashSet<>(Arrays.asList(R.drawable.d_element_1, R.drawable.d_element_2, R.drawable.d_element_3, R.drawable.d_element_4, R.drawable.i_element_1, R.drawable.i_element_2, R.drawable.i_element_3, R.drawable.i_element_4, R.drawable.k_element_1, R.drawable.k_element_2, R.drawable.k_element_3, R.drawable.k_element_4));

    public static HashSet<Integer> ILLUSION_MAS  = new HashSet<>(Arrays.asList(R.drawable.d_illusion_1, R.drawable.d_illusion_2, R.drawable.d_illusion_3, R.drawable.d_illusion_4, R.drawable.i_illusion_1, R.drawable.i_illusion_2, R.drawable.i_illusion_3, R.drawable.i_illusion_4, R.drawable.k_illusion_1, R.drawable.k_illusion_2, R.drawable.k_illusion_3, R.drawable.k_illusion_4));

    public static HashSet<Integer> NATURE_MAS  = new HashSet<>(Arrays.asList(R.drawable.d_nature_1, R.drawable.d_nature_2, R.drawable.d_nature_3, R.drawable.d_nature_4, R.drawable.d_nature_5, R.drawable.i_nature_1, R.drawable.i_nature_2, R.drawable.i_nature_3, R.drawable.i_nature_4, R.drawable.i_nature_5, R.drawable.k_nature_1, R.drawable.k_nature_2, R.drawable.k_nature_3, R.drawable.k_nature_4, R.drawable.k_nature_5));

    public static HashSet<Integer> SECRET_MAS  = new HashSet<>(Arrays.asList(R.drawable.d_secret_1, R.drawable.d_secret_2, R.drawable.d_secret_3, R.drawable.d_secret_4, R.drawable.i_secret_1, R.drawable.i_secret_2, R.drawable.i_secret_3, R.drawable.i_secret_4, R.drawable.k_secret_1, R.drawable.k_secret_2, R.drawable.k_secret_3, R.drawable.k_secret_4));

    public static int I_CARD = R.drawable.i_card;
    public static int K_CARD = R.drawable.k_card;
    public static int D_CARD = R.drawable.d_card;
}

