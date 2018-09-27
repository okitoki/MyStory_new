package com.thedot.mystoryinenglishn.Index;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class MenuSlideView extends HorizontalScrollView {

    private Context mContext;
    private int mScreenWidth;
    private static final String TAG = "MiniApp";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private int mCurPosX = 0;
    ImageView iv_left;
    private int mMoveDefault = -1;

    private GestureDetector mGesture;
    private GestureDetector.OnGestureListener mGesturesListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            PistolLogger.LOGD("onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            PistolLogger.LOGD("onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
            smoothScrollBy((int)distanceX,0);  // 스크롤할때 따라오기
            int nScrollX = getScrollX(); // 스크롤 현위치
            int nScrollY = getScrollY();

            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
            // TODO
            int nScrollX = getScrollX();
            int nScrollY = getScrollY();
            // 지정된 위치로 이동, 부드럽게 이동(smoothScrollTo)
            moveThisPos(nScrollX, nScrollY);

            return false;
        }
    };

    public MenuSlideView(Context context) {
        super(context);
        mContext = context;
        createSubView();
}

    public MenuSlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        createSubView();
    }

    public MenuSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        createSubView();
    }

    private void createSubView() {
        mGesture = new GestureDetector(mContext, mGesturesListener);
        //scroll view setting
        setHorizontalScrollBarEnabled(false);

        // get screen size
        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mScreenWidth = display.getWidth(); Toast.makeText(mContext, "mSW="+mScreenWidth, Toast.LENGTH_SHORT).show();
        //set sub layout

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.menu_layout, null);
        iv_left = (ImageView)v.findViewById(R.id.iv_left);
        iv_left.setVisibility(View.GONE); // 자리 안차지
       // iv_left.setVisibility(View.INVISIBLE); // 자리 차지
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 500); // 높이 100
        addView(v,params);

    }

    public void moveDefault(int x, int count){
        if(count != 0){
            mMoveDefault = mScreenWidth/count;
        }else{
            mMoveDefault = x;
    }// 스크롤바 초기 이동
       post(new Runnable(){
           public void run() {
               //get screen size, move default scroll position
               scrollTo(mMoveDefault, 0);
           }});

        mCurPosX = mMoveDefault;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int nScrollX = getScrollX();
        int nScrollY = getScrollY();
        float nMoveX = ev.getX();
        float nMoveY = ev.getY();
        mGesture.onTouchEvent(ev);
        int action = ev.getAction();

        switch(action) {
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: getScrollX()=" + getScrollX());
                break;
            case MotionEvent.ACTION_UP:
                 //TODO // 지정된 위치로 이동, 부드럽게 이동(smoothScrollTo) moveThisPos(nScrollX, nScrollY);
                 Log.d(TAG, "ACTION_UP: Scroll("+nScrollX+","+nScrollY+")"+" POS("+nMoveX+","+nMoveY+")");
                break;
        }
            return true;
    }

    // 지정된 위치로 이동
    public void moveThisPos(int nScrollX, int nScrollY){
        if(nScrollX < mScreenWidth/6){
            smoothScrollTo(0,0);
        }else if(nScrollX >= mScreenWidth/6 && nScrollX < mScreenWidth/3){
            smoothScrollTo(mScreenWidth/3,0);
        }else if(nScrollX >= mScreenWidth/3 && nScrollX < mScreenWidth/2){
            smoothScrollTo(mScreenWidth/3,0);
        }else if(nScrollX >= (mScreenWidth/2) && nScrollX < (mScreenWidth/3*2)){
            smoothScrollTo(mScreenWidth/3*2,0);
        }else if(nScrollX >= (mScreenWidth/3+mScreenWidth/3) && nScrollX < (mScreenWidth/6*5)){
            smoothScrollTo(mScreenWidth/3*2,0);
        }else if(nScrollX >= (mScreenWidth/6*5) && nScrollX < (mScreenWidth)){
            smoothScrollTo(mScreenWidth,0);
        }
    }

    }