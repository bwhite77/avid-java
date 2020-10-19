package com.example.avid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ImageDisplayFragment.ImageDisplayFragmentCallback, PictureSetFragment.PictureSetFragmentCallback {

    private long timeStartedListen = 0;

    private ImageDisplayFragment fragment;
    private GestureDetectorCompat gestureDetector;

    // constants
    final int MAX_IMAGE_COUNT = 2;
    final int ENLARGED_HEIGHT = 400;
    final int ENLARGED_WIDTH = 400;
    final int DOWNLOAD_VISIBLE = 1;

    // variables
    int imageIDs[];
    int imageCount = 0;
    int currentImage = 0;
    private int mX_Delta;
    private int mY_Delta;
    int selectedImgId;
    int prevSelectedImgId;
    private boolean flag = true;

    // layout
    RelativeLayout layout;
    Toolbar toolbar;

    // views
    private ImageView childButton;
    private ImageView caregiverButton;

    // toolbar buttons
    private ImageView audioInputWidget;
    private ImageView toolbarButton;

    // popup
    ImageView imgPop;
    TextView txtPop;
    ImageView childPop;
    ImageView caregiverPop;

    // speech
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;

    Dialog dialog;

    private HashMap<String, Picture> pictures;

    private Boolean isDisplayFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImageDisplayFragment(), "image_display_fragment").commit();
        getSupportFragmentManager().executePendingTransactions();
        fragment = (ImageDisplayFragment) getSupportFragmentManager().findFragmentByTag("image_display_fragment");

        fragment.setRetainInstance(true);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // reference to views
        layout = findViewById(R.id.main_layout);
        audioInputWidget = findViewById(R.id.audio_input_widget);
        toolbarButton = findViewById(R.id.toolbar_button);

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        audioInputWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "BUTTON CLICKED", Toast.LENGTH_SHORT);
                speak("Speak now for input.");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                timeStartedListen = System.currentTimeMillis();
                speechRecognizer.startListening(intent);
                ImageView imageView = new ImageView(getApplicationContext());
                //Drawable d = new BitmapDrawable(getResources(), pictures.get("Back Float").getImageBitmap());
                if(fragment.getPicture("Floaties") == null)
                {
                    Log.e("Error", "onClick: ");
                }
                fragment.addPictureToView(fragment.getPicture("Floaties"));
            }
        });

        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(v);
                fragmentChange();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
         //       if(resultCode == RESULT_OK && data != null)
           //         ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        }
    }

    public void showPopup(final Picture picture, boolean childButtonClicked)
    {
        Drawable drawable;
        drawable = new BitmapDrawable(getResources(), picture.getImageBitmap());

        dialog.setContentView(R.layout.activity_image_pop_up);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imgPop = dialog.findViewById(R.id.imageViewPop);
        txtPop = dialog.findViewById(R.id.textViewPop);
        childPop = dialog.findViewById(R.id.childPopButton);
        caregiverPop = dialog.findViewById(R.id.caregiverPopButton);

        imgPop.setImageDrawable(drawable);
        imgPop.setBackground(getDrawable(R.drawable.imgbackground));
        if(childButtonClicked)
            txtPop.setText(picture.getmChildMessage());
        else
            txtPop.setText(picture.getCaregiverMessage());

        dialog.show();
        childPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(picture.getmChildMessage());
                txtPop.setText(picture.getmChildMessage());
            }
        });
        caregiverPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(picture.getCaregiverMessage());
                txtPop.setText(picture.getCaregiverMessage());
            }
        });


    }

    private void initializeSpeechRecognizer() {
       // Toast.makeText(MainActivity.this, "Speech Recognizer initialize", Toast.LENGTH_SHORT).show();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            long duration = System.currentTimeMillis() - timeStartedListen;
            boolean isEndOfSpeech = false;
            @Override
            public void onReadyForSpeech(Bundle params) {
            //    Toast.makeText(MainActivity.this, "onReadyForSpeech", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {
            //    Toast.makeText(MainActivity.this, "onBeginning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
              //  Toast.makeText(MainActivity.this, "onEnd", Toast.LENGTH_SHORT).show();
                isEndOfSpeech = true;
            }

            @Override
            public void onError(int error) {
                if (duration < 500 && error == SpeechRecognizer.ERROR_NO_MATCH) {
                    return;
                }
                if(!isEndOfSpeech)
                {
                    return;
                }
            //    Toast.makeText(MainActivity.this, "onError " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
            //    Toast.makeText(MainActivity.this, "Speech Recognizer OnResults", Toast.LENGTH_SHORT).show();
                List<String> output = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                processResult(output.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    private void processResult(String s) {
        // back float, blow bubbles, bops, child, dive, floaties, front_crawl, jump, scoop, throw
        s = s.toLowerCase();
        /*
        if(pictures.containsKey(s))
            addPictureToView(pictures.get(s));
         */

        if(s.contains("back float"))
        {
            fragment.addPictureToView(pictures.get("Back Float"));
        }
        else if(s.contains("blow bubbles"))
        {
            fragment.addPictureToView(pictures.get("Blow Bubbles"));
        }
        else if(s.contains("bops"))
        {
            fragment.addPictureToView(pictures.get("Bops"));
        }
        else if(s.contains("dive"))
        {
            fragment.addPictureToView(pictures.get("Dive"));
        }
        else if(s.contains("floaties"))
        {
            fragment.addPictureToView(pictures.get("Floaties"));
        }
        else if(s.contains("front crawl"))
        {
            fragment.addPictureToView(pictures.get("Front Crawl"));
        }
        else if(s.contains("jump"))
        {
            fragment.addPictureToView(pictures.get("Jump"));
        }
        else if(s.contains("scoop"))
        {
            fragment.addPictureToView(pictures.get("Scoop"));
        }
        else if(s.contains("throw"))
        {
            fragment.addPictureToView(pictures.get("Throw"));
        }
        else
        {
            Toast.makeText(MainActivity.this, "ERROR: Unrecognized command.", Toast.LENGTH_SHORT).show();
        }
    }

    public void animate(View view) {
        ImageView v = (ImageView) view;
        if (flag) {
            v.setImageResource(R.drawable.down_to_x);
        }
        else {
            v.setImageResource(R.drawable.x_to_down);
            changeToolbar(2);
        }
        //v.setImageResource(flag ? R.drawable.down_to_x : R.drawable.x_to_down);
        Drawable d = v.getDrawable();
        flag = !flag;
        if(d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        }
        else if (d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        }
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(textToSpeech.getEngines().size() == 0) {
                    // ERROR
                    Toast.makeText(MainActivity.this, "A text-to-speech engine is required to use this feature", Toast.LENGTH_LONG).show();
                }
                else {
                    textToSpeech.setLanguage(Locale.US);
                    // textToSpeech.setPitch(.85f);
                    // textToSpeech.setSpeechRate(0.5f);
                }
            }
        });
    }

    public void speak(String messageToRead) {
        textToSpeech.speak(messageToRead, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void fragmentChange()
    {
        if(isDisplayFragment) {
            isDisplayFragment = false;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ImageAddFragment imageAddFragment = new ImageAddFragment();

            ft.add(R.id.fragment_container, imageAddFragment);
            ft.setCustomAnimations(R.anim.slide_down_in,0);
            ft.show(imageAddFragment);
            ft.commit();
            //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down_in, 0).replace(R.id.fragment_container, new ImageAddFragment()).commit();
        }
        else {
            isDisplayFragment = true;
            getSupportFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.slide_up_out).replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public void changeToolbar(int id) {
        if(id == DOWNLOAD_VISIBLE) {
            toolbar.findViewById(R.id.download_widget).setVisibility(View.VISIBLE);
        }
        else {
            toolbar.findViewById(R.id.download_widget).setVisibility(View.INVISIBLE);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
