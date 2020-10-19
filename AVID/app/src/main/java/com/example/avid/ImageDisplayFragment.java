package com.example.avid;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

public class ImageDisplayFragment extends Fragment implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    //    https://stackoverflow.com/questions/19726697/calling-activity-method-from-inside-a-fragment
    public interface ImageDisplayFragmentCallback {
        void speak(String messageToRead);
        void showPopup(Picture pictureToSend, boolean childButtonClicked);
    }

    @Override
    public void onAttach(Context context) {
        callback = (ImageDisplayFragmentCallback) context;
        super.onAttach(context);
    }

    private ImageDisplayFragmentCallback callback;

    // max images that can be displayed on page
    final int MAX_IMAGE_COUNT = 2;

    // size of image when enlarged on dialog
    final int ENLARGED_HEIGHT = 400;
    final int ENLARGED_WIDTH = 400;

    // fragment layout
    RelativeLayout layout;

    // imageviews
    private ImageView childButton;
    private ImageView caregiverButton;

    // stores pictures to be used in display
    private HashMap<String, Picture> pictures;

    // stores data related to images on display
    int imageIDs[];
    int imageCount = 0;
    int currentImage = 0;

    private float mX_Delta;
    private float mY_Delta;
    private int lastAction;

    int selectedImgId;
    int prevSelectedImgId;

    DisplayMetrics displayMetrics;
    int layoutHeight;
    int layoutWidth;

    private GestureDetector gestureDetector;



    float parentCenterX;
    float parentCenterY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_display, container, false);

        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        pictures = new HashMap<>();

        Picture picture1 = new Picture("Back Float", BitmapFactory.decodeResource(getResources(), R.drawable.back_float), "I want you to do a back float", "I want to do a back float");
        Picture picture2 = new Picture("Blow Bubbles", BitmapFactory.decodeResource(getResources(), R.drawable.blow_bubbles), "I want you to blow bubbles", "I want to blow bubbles");
        Picture picture3 = new Picture("Bops", BitmapFactory.decodeResource(getResources(), R.drawable.bops), "I want you to do bops", "I want to do bops");
        Picture picture4 = new Picture("Dive", BitmapFactory.decodeResource(getResources(), R.drawable.dive), "I want you to dive", "I want to dive");
        Picture picture5 = new Picture("Floaties", BitmapFactory.decodeResource(getResources(), R.drawable.floaties), "I want you to use floaties", "I want to use floaties");
        Picture picture6 = new Picture("Front Crawl", BitmapFactory.decodeResource(getResources(), R.drawable.front_crawl), "I want you to do a front crawl", "I want to do a front crawl");
        Picture picture7 = new Picture("Jump", BitmapFactory.decodeResource(getResources(), R.drawable.jump), "I want you to jump", "I want to jump");
        Picture picture8 = new Picture("Scoop", BitmapFactory.decodeResource(getResources(), R.drawable.back_float), "I want you to scoop", "I want to scoop");
        Picture picture9 = new Picture("Throw", BitmapFactory.decodeResource(getResources(), R.drawable.back_float), "I want you to catch the toy", "I want to catch the toy");

        pictures.put(picture1.getKey(), picture1);
        pictures.put(picture2.getKey(), picture2);
        pictures.put(picture3.getKey(), picture3);
        pictures.put(picture4.getKey(), picture4);
        pictures.put(picture5.getKey(), picture5);
        pictures.put(picture6.getKey(), picture6);
        pictures.put(picture7.getKey(), picture7);
        pictures.put(picture8.getKey(), picture8);
        pictures.put(picture9.getKey(), picture9);

        imageIDs = new int[MAX_IMAGE_COUNT];

        layout = v.findViewById(R.id.fragment_image_display);
        childButton = v.findViewById(R.id.childButton);
        caregiverButton = v.findViewById(R.id.caregiverButton);

        Log.e(Integer.toString(layoutHeight), "onCreateView: ");
        Log.e(Integer.toString(layoutWidth), "onCreateView: ");

        childButton.setVisibility(View.GONE);
        caregiverButton.setVisibility(View.GONE);

        caregiverButton.setOnClickListener(caregiverBtnListener);

        layout.setOnClickListener(relLayoutListener);

        gestureDetector = new GestureDetector(getContext(), this);

        parentCenterX = displayMetrics.heightPixels / 2.0f;
        parentCenterY = displayMetrics.widthPixels / 2.0f;

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    RelativeLayout.OnClickListener relLayoutListener =
            new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childButton.setVisibility(View.GONE);
                    caregiverButton.setVisibility(View.GONE);
                }
            };

    Button.OnClickListener caregiverBtnListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imgToSend = layout.findViewById(selectedImgId);
                    Picture pictureToSend = (Picture) imgToSend.getTag();
                    callback.speak(pictureToSend.getCaregiverMessage());
                    callback.showPopup(pictureToSend, false);
                }
            };

    Button.OnClickListener childBtnListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imgToSend = layout.findViewById(selectedImgId);
                    Picture pictureToSend = (Picture) imgToSend.getTag();
                    callback.speak(pictureToSend.getmChildMessage());
                    callback.showPopup(pictureToSend, false);
                }
            };

    public void addPictureToView(Picture picture) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(new BitmapDrawable(getResources(), picture.getImageBitmap()));
        if(imageCount == MAX_IMAGE_COUNT)
            layout.removeView(layout.findViewById(imageIDs[currentImage]));
        imageIDs[currentImage] = View.generateViewId();
        Log.e("id", Integer.toString(imageIDs[currentImage]));
        imageView.setId(imageIDs[currentImage]);
        imageView.setTag(picture);

//        ConstraintSet constraintSet = new ConstraintSet();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 500);
        layoutParams.setMargins(10, 10, 0, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        imageView.setLayoutParams(layoutParams);
        layout.addView(imageView);
        Log.e("layout_id", Integer.toString(layout.getId()));
//        constraintSet.clone(layout);
//        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT,
//                ConstraintSet.PARENT_ID, ConstraintSet.LEFT , 100);
//        constraintSet.connect(imageView.getId(), ConstraintSet.TOP,
//                ConstraintSet.PARENT_ID, ConstraintSet.TOP , 100);
//        constraintSet.applyTo(layout);

        imageView.setOnTouchListener(this);

        currentImage++;
        if(currentImage == MAX_IMAGE_COUNT)
            currentImage = 0;
        if(imageCount < MAX_IMAGE_COUNT)
            imageCount++;
    }

    public Picture getPicture(String key)
    {
        return pictures.get(key);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("TouchDetector", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("TouchDetector", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("TouchDetector", "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("TouchDetector", "onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("TouchDetector", "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("TouchDetector", "onFling");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("TouchDetector", "onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // https://stackoverflow.com/questions/20989640/how-to-move-view-to-the-center-of-screen-using-animations-in-android
        // https://stackoverflow.com/questions/3882826/android-animation-position-resets-after-complete
        Log.d("TouchDetector", "onDoubleTap");
        ImageView imageView = layout.findViewById(selectedImgId);
        Float tX = parentCenterX - (imageView.getWidth() / 2.0f) - imageView.getX();
        Float tY = parentCenterY - (imageView.getHeight() / 2.0f) - imageView.getY();
        imageView.animate().translationX(tX).translationY(tY);
       // imageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_to_fit));
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("TouchDetector", "onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        Log.d("TouchDetector", "onTouch");
        childButton.setVisibility(View.VISIBLE);
        caregiverButton.setVisibility(View.VISIBLE);
        selectedImgId = v.getId();
        event.setSource(selectedImgId);

        float X, Y;

        layoutHeight = layout.getHeight();
        layoutWidth = layout.getWidth();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mX_Delta = v.getX() - event.getRawX();
                mY_Delta = v.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                X = event.getRawX() + mX_Delta;
                Y = event.getRawY() + mY_Delta;

                lastAction = MotionEvent.ACTION_MOVE;

                if((X < 0 || X > layoutWidth - v.getWidth()) || (Y < 0 || Y > layoutHeight - v.getHeight()))
                {
                    break;
                }

                v.setX(X);
                v.setY(Y);

                break;
        }
//            layout.invalidate();
        return true;
    }
}


