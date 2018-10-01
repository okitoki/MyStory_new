package com.thedot.mystoryinenglishn.Index;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thedot.mystoryinenglishn.BaseApplication;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;
import com.thedot.mystoryinenglishn.Utils.Utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BaseApplication mbaseapplication;
    private String jsonData=null;
    private ArrayList<CategoryData> cate_data = new ArrayList<>();
    private ArrayList<String> catedetail_str_data = new ArrayList<>();
    private ArrayList<ArrayList<CategoryDetialData>> cate_detail_data = new ArrayList<ArrayList<CategoryDetialData>>();
//    private int contentId;
//    private int cateId;
    // Requesting permission to RECORD_AUDIO
    final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    public String zip_file_name[];
    public static ArrayList<Integer> zipId;

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // check whether the download-id is mine
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                // not our download id, ignore
                return;
            }
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            PistolLogger.LOGD("download ID: " + Long.toString(id) + ", " + downloadManager.getUriForDownloadedFile(mDownloadId).toString() );
            // make a query
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);

            // check the status
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                // when download completed
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                int status = cursor.getInt(columnIndex);
                int reason = cursor.getInt(columnReason);
                int statusColumn = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                String thePackageName = getPackageName();
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                String zipFilePath = root + "/Android/obb/"+thePackageName;
                String zipFilename =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + zip_file_name[zipId.get(0)]+".zip";

                switch (status){

                    case DownloadManager.STATUS_SUCCESSFUL :

                        Toast.makeText(getApplicationContext(), "다운로드 완료.", Toast.LENGTH_SHORT).show();
                        Preferences.setExgistVideoFile(getApplicationContext(),zipId.get(0).toString(),true);
                        Utility.extractZipFiles(zipFilename,zipFilePath);
                        File zipfile = new File(zipFilename);
                        boolean delete_result = zipfile.delete();
                        PistolLogger.LOGD("deletefilename: "+zipFilename +", result: " + delete_result);
                        zipId.remove(0);
                        break;

                    case DownloadManager.STATUS_PAUSED :

                        Toast.makeText(getApplicationContext(), "다운로드 중지 : " + reason, Toast.LENGTH_SHORT).show();
                        break;

                    case DownloadManager.STATUS_FAILED :

                        Toast.makeText(getApplicationContext(), "다운로드 취소 : " + reason, Toast.LENGTH_SHORT).show();
                        break;
                }
                //int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                // String downloadedPackageUriString = cursor.getString(uriIndex);

            }else{
                // when canceled
                return;
            }
        }
    };

    private void unregister(){
        unregisterReceiver(downloadCompleteReceiver);
        downloadCompleteReceiver = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //마쉬멜로 이후 녹음레코드 퍼미션 허용
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getBaseContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(getApplicationContext(),"이미 권한설정되어 있음",Toast.LENGTH_SHORT);
            }
        }
       AsyncTask datatask = new JsonDataTask().execute("category.json");

        zipId = new ArrayList<Integer>();
        zip_file_name = new String[] {"greeting","pronounciation","daily","doing_chores","food","weather","sickness","appearance",
                "personalities","timecalendar","numbermoney","direction","travel","aboutkorea1","aboutkorea2","lifestyle","environment","quotation"};

        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public BaseApplication getBaseApplication(){
        return  mbaseapplication = (BaseApplication) getApplicationContext();
    }
    public void downloadAlert(){

    }

    public void setMystationFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        MyStationFragment mystationfragment = MyStationFragment.newInstance();
        fragmentTrasaction.replace(R.id.first_depth_contatiner,mystationfragment,"mystationfragment");
        fragmentTrasaction.addToBackStack("mystationfragment");
        fragmentTrasaction.commit();
    }

    public void setSettingFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        SettingFragment settingfragment = SettingFragment.newInstance();
        fragmentTrasaction.add(R.id.first_depth_contatiner,settingfragment,"settingfragment");
        fragmentTrasaction.addToBackStack("settingfragment");
        fragmentTrasaction.commit();
    }

    public void endAnimationFirstContainer(){
        RelativeLayout category_container = (RelativeLayout) findViewById(R.id.first_depth_contatiner);
        Animation container_anim =  AnimationUtils.loadAnimation(this,R.anim.start_exit);
        category_container.setAnimation(container_anim);
    }
    public void enterAnimationFirstContainer(){
        RelativeLayout category_container = (RelativeLayout) findViewById(R.id.first_depth_contatiner);
        Animation container_anim =  AnimationUtils.loadAnimation(this,R.anim.start_enter);
        category_container.setAnimation(container_anim);
    }

    public void setCategoryView(ArrayList<CategoryData> cate){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        CategoryFragment mcategoryfragment = CategoryFragment.newInstance(cate);
        fragmentTrasaction.add(R.id.first_depth_contatiner,mcategoryfragment,"categoryfragment");
        fragmentTrasaction.commitAllowingStateLoss();
    }

    public void setDetailCategoryView(ArrayList<CategoryData> data,int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        CategoryDetailFragment mcategorydetailfragment = CategoryDetailFragment.newInstance(position);
        fragmentTrasaction.replace(R.id.first_depth_contatiner, mcategorydetailfragment,"detailcategoryfragment");
        fragmentTrasaction.addToBackStack("detailcategoryfragment");
        fragmentTrasaction.commit();
    }

    public void setDetailCategoryView_third_depth(ArrayList<CategoryData> data,int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        CategoryDetailFragment mcategorydetailfragment = CategoryDetailFragment.newInstance(position);
        fragmentTrasaction.replace(R.id.first_depth_contatiner, mcategorydetailfragment,"detailcategoryfragment");
        fragmentTrasaction.addToBackStack("detailcategoryfragment");
        fragmentTrasaction.commit();
    }

    public ArrayList<CategoryData> getCate_data() {
        return cate_data;
    }

    public void addCategoryDetaildata(ArrayList<CategoryDetialData> data){
        cate_detail_data.add(data);
    }

    public ArrayList<CategoryDetialData> getCategoryDetaildata(int position){
        return cate_detail_data.get(position);
    }

    public void setCatedetaildata(ArrayList<String> data){
        catedetail_str_data=data;
    }

    public String loadJsonFromAsset(String data){
        String json = null;
        try{
            InputStream is  = getAssets().open(data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }


    public long mDownloadId;
    public void downloadStart(String url){
        // url ="http://the-dot.co.kr/adult_english_obb/about_korea.obb";

        String newFilename =  url.substring(39);
        PistolLogger.LOGD("download save file name : "+newFilename);

        // Make a request
        DownloadManager.Request request
                = new DownloadManager.Request(Uri.parse(url))
                .setAllowedOverRoaming(false)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setTitle("어른의 영어")
                .setDescription("카테고리 파일 다운로드");


//        if (android.os.Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED)) {
//            request.setDestinationInExternalPublicDir(
//                    android.os.Environment.DIRECTORY_DOWNLOADS, newFilename);
//        }
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, newFilename);
//        String thePackageName = getActivity().getPackageName();
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String zipFilePath = root + "/Android/obb/"+thePackageName;
//
//        request.setDestinationInExternalPublicDir(
//                android.os.Environment.DIRECTORY_DOWNLOADS,newFilename);

        // you can hide download status
        // request.setVisibleInDownloadsUi(false);
        // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        // enqueue
        DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadId = dm.enqueue(request);
    }

    public void clickDownload(int lession_id){
        final String zip_file_path = "http://the-dot.co.kr/adult_english_zip/";
        String file_url = zip_file_path + zip_file_name[lession_id] +".zip";
        PistolLogger.LOGD("download_file_url:" + file_url);
        downloadStart(file_url);
        zipId.add(lession_id);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private class CategoryDetailJsonDataTask extends AsyncTask<String, String, Long> {
        ArrayList<String> jsonData = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strings) {

            for(int i=0;i<strings.length;++i) {
                String json =loadJsonFromAsset(strings[i]);
                Type listType = new TypeToken<ArrayList<CategoryDetialData>>() {}.getType();
                ArrayList<CategoryDetialData> category_detail_data = new Gson().fromJson(json, listType);
                addCategoryDetaildata(category_detail_data);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            setCatedetaildata(jsonData);

        }
    }

    private class JsonDataTask extends AsyncTask<String, String, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Long doInBackground(String... strings) {
            jsonData = loadJsonFromAsset(strings[0]);
            Type listType = new TypeToken<ArrayList<CategoryData>>() { }.getType();
            cate_data = new Gson().fromJson(jsonData, listType);
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            setCategoryView(cate_data);
            String[] url_str = new String[cate_data.size()];
            for(int i=0;i<cate_data.size();++i) {
                url_str[i] = cate_data.get(i).getCateDetailfileUrl();
            }
            AsyncTask detaildata = new CategoryDetailJsonDataTask().execute(url_str);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }
}
