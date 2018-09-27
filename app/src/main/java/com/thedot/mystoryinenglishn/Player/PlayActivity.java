package com.thedot.mystoryinenglishn.Player;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thedot.mystoryinenglishn.BaseApplication;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    public boolean FRAGMENT_ME_FIRST_ENTER_CHECK;
    public boolean FRAGMENT_ALEX_FIRST_ENTER_CHECK;
    private BaseApplication mbaseapplication;
    private int contentId;
    private int cateId;
    private String cateName;
    private ContentData content_data=null;
    private ArrayList<String> content_jeson_url= new ArrayList<>();
    public boolean VIDEO_CONTINU_TAG=false; //연속재생
    public boolean VIDEO_FIRST_CHECK=true; //첫번째 비디오인지 체크한다.
    public String REPLACE_ALEX_START_ANIMATION_DERECTION = "none";
    private Handler handler = new Handler();
    public Button btn_next_unit;
    public Button btn_prev_unit;
    private PlayerCardFragment mPlayerCardFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        FRAGMENT_ME_FIRST_ENTER_CHECK=true;
        FRAGMENT_ALEX_FIRST_ENTER_CHECK=true;

        setCateId(getIntent().getExtras().getInt("cateId"));
        setContentId(getIntent().getExtras().getInt("contentId")-1);
        setCateName(getIntent().getExtras().getString("cateName"));
        content_jeson_url = (ArrayList<String>) getIntent().getStringArrayListExtra("content_Jeson_Urls");
        PistolLogger.LOGD("URL===========>" + content_jeson_url.get(contentId));
        setContent(content_jeson_url.get(contentId));

        btn_next_unit = (Button) findViewById(R.id.btn_next_unit);
        btn_prev_unit = (Button) findViewById(R.id.btn_prev_unit);

        btn_next_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unitNumber()<unitTotalNumber()) {
                    nextUnitPlay();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.playactivity_last_unit, Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_prev_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unitNumber()>1) {
                    prevUnitPlay();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.playactivity_first_unit, Toast.LENGTH_LONG).show();
                }
            }
        });

        final FrameLayout backnextcontainer = (FrameLayout) findViewById(R.id.backnextContainer);
        final Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout_exit);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backnextcontainer.startAnimation(fade_out);
                backnextcontainer.setVisibility(View.INVISIBLE);
            }
        },2000);
    }
    public int unitTotalNumber(){
        int unit_total_num =  getContentDataUrl().size();
        return unit_total_num;
    }
    public int unitNumber(){
        int unit_number = getContentData().getId();
        return unit_number;
    }
    public boolean isStateUnitPlay(){
        if(unitTotalNumber()>unitNumber() && unitNumber()>0) return true;
        else {
            return false;
        }
    }

    public void nextUnitPlay(){
        //VIDEO_FIRST_CHECK=false;
        REPLACE_ALEX_START_ANIMATION_DERECTION = "RIGHT";
        if(mPlayerCardFragment!=null) mPlayerCardFragment.showVidoWhiteBackground();
        PistolLogger.LOGD("contentId:" + getContentId());
        setContentId(getContentId()+1);
        String content_string_url = getContentDataUrl().get(getContentId());
        PistolLogger.LOGD("contentURL:"+content_string_url  +", contentId:" + getContentId());
        setContent(content_string_url);
    }
    public void prevUnitPlay(){
        //VIDEO_FIRST_CHECK=false;
        REPLACE_ALEX_START_ANIMATION_DERECTION = "LEFT";
        if(mPlayerCardFragment!=null) mPlayerCardFragment.showVidoWhiteBackground();
        setContentId(getContentId() - 1);
        String content_string_url = getContentDataUrl().get(getContentId());
        PistolLogger.LOGD("contentURL:"+content_string_url  +", contentId:" + getContentId());
        setContent(content_string_url);
    }

    public void setCateName(String catename){
        cateName=catename;
    }
    public void setCateId(int cateid){
        cateId=cateid;
    }
    public void setContentId(int contentid){
        contentId = contentid;
    }
    public void setContent(String url){
        new ContentJsonDataTask().execute(url);
    }
    public ArrayList<String> getContentDataUrl(){
        return content_jeson_url;
    }
    public ContentData getContentData(){
        return content_data;
    }
    public int getCateId(){
        return cateId;
    }

    public int getContentId(){

        if(contentId>=getContentDataUrl().size()){
            VIDEO_CONTINU_TAG = false;
        }else{
            VIDEO_CONTINU_TAG=true;
        }

        return contentId;
    }
    public String getCateName(){
        return cateName;
    }
    public BaseApplication getBaseApplication(){
          mbaseapplication = (BaseApplication) getApplicationContext();
        return mbaseapplication;
    }
    public void replayPlayerCardFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        mPlayerCardFragment = PlayerCardFragment.newInstance();

        if(!FRAGMENT_ALEX_FIRST_ENTER_CHECK && REPLACE_ALEX_START_ANIMATION_DERECTION.equals("RIGHT")){
            fragmentTrasaction.setCustomAnimations(R.anim.start_left_enter,R.anim.fadeout_exit);
        }
        if(!FRAGMENT_ALEX_FIRST_ENTER_CHECK && REPLACE_ALEX_START_ANIMATION_DERECTION.equals("LEFT")){
            fragmentTrasaction.setCustomAnimations(R.anim.start_right_enter,R.anim.fadeout_exit);
        }
        fragmentTrasaction.replace(R.id.fragmentContainer,mPlayerCardFragment,"playcardfragment");
        fragmentTrasaction.commit();

        FRAGMENT_ALEX_FIRST_ENTER_CHECK=false;
    }

    public String loadJsonFromAsset(String data){
        String str = null;
        try{
            InputStream is  = getAssets().open(data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return str;
    }
    public String getContnetTextValue(String[] con_str,int sync_num){
        String con = "";
        int i=0;
        for(String str:con_str) {
            ++i;
            //todo 2 활성화 number 연결
            if(i==sync_num) {
                con = con + "<font color='#FE5F5F'>" + str + "</font>";
            }else{
                con = con + "<font color='#000000'>" + str + "</font>";
            }
        }
        return con;
    }

    private class ContentJsonDataTask extends AsyncTask<String, String, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strings) {

            String json = loadJsonFromAsset(strings[0]);
            content_data = new Gson().fromJson(json, ContentData.class);
            PistolLogger.LOGD("dataccc" + content_data.getContent_subject());
            String[] engStr=null;
            String[] korStr=null;

            PistolLogger.LOGD("getCateId:" + getCateId()+ "getContentId:" + getContentId());
            if(getCateId()==13&&getContentId()==3){
                engStr = content_data.getEnglish_content_text().split("</>");
                korStr = content_data.getKorean_content_text().split("</>");
            }else {
                engStr = content_data.getEnglish_content_text().split("/");
                korStr = content_data.getKorean_content_text().split("/");
            }
            content_data.setEngContentStr(engStr);
            content_data.setKorContentStr(korStr);

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            replayPlayerCardFragment();
        }
    }

    public void finishActivity(){
        Intent intent = new Intent();
        intent.putExtra("cateId",getCateId());
        intent.putExtra("contentId",getContentId()+1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        PistolLogger.LOGD("onBackPressed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PistolLogger.LOGD("onDestroy");
    }
}
