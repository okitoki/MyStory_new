package com.thedot.mystoryinenglishn;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.thedot.mystoryinenglishn.Player.RecordingFile;
import com.thedot.mystoryinenglishn.Utils.MyStoryRecorddbHelper;

import java.util.ArrayList;

/**
 * Created by okitoki on 2017. 8. 16..
 */

public class BaseApplication extends Application {
    public static boolean DEBUG = false;
    public static MyStoryRecorddbHelper myrecordfiledb;

    @Override
    public void onCreate() {
        super.onCreate();
        this.DEBUG = isDebuggable(this);
        myrecordfiledb = new MyStoryRecorddbHelper(getApplicationContext(), "recordingfile.db",1);
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/recordingfile.db";
//        myrecordfiledb = new MyStoryRecorddbHelper(getApplicationContext(), path,1);
    }
    public void insertRecordinginfo(int cateid, int contentid, String name, String data, String path){
        myrecordfiledb.insert(cateid,contentid,name,data,path);
    }
    public void insertGradeinfo(int categoryid, int contentid, int alex, int me){
        myrecordfiledb.insert_grade(categoryid,contentid,alex,me);
    }
    public void insertTotalRecord(){
            myrecordfiledb.insert_totalgrade();
    }
    public int getValueAlexGrage(int categoryid, int contentid){
        return myrecordfiledb.getValueAlexGrage(categoryid,contentid);
    }
    public int getValueMeGrage(int categoryid, int contentid){
        return myrecordfiledb.getValueMeGrage(categoryid,contentid);
    }
    public int getValueAlexTotalGrage(){
        return myrecordfiledb.getValueAlexTotalGrage();
    }
    public int getValueMeTotalGrage(){
        return myrecordfiledb.getValueMeTotalGrage();
    }

    public void updateGradeAlex(int categoryid, int contentid, int alex){
        myrecordfiledb.update_grade_alex(categoryid,contentid,alex);
    }
    public void updateGradeMe(int categoryid, int contentid, int me){
        myrecordfiledb.update_grade_me(categoryid,contentid,me);
    }
    public void deleteRecordingfileByContentname(String _name){
        myrecordfiledb.deleteValue(_name);
    }
    public String getFilePathValue(String _name){
       return myrecordfiledb.getFilePathValueFromContentName(_name);
    }
    public ArrayList<RecordingFile> getValue(int cateId, int contentId){
        return myrecordfiledb.getValueFromContentID(cateId, contentId);
    }
    public void insertLikeValue(int cateId,int value){
        myrecordfiledb.insert_like(cateId,value);
    }
    public ArrayList<Integer> getLikeCateIdVale(int cateId, int value){
       return myrecordfiledb.get_like_cateid(cateId, value);
    }
    public ArrayList<Integer> getLikeCateIdVale(){
        return myrecordfiledb.get_like_cateid();
    }
    public void insertTotalgradeAlex(){
        myrecordfiledb.insert_totalgrade_alex();
    }
    public void insertTotalgradeMe(){
        myrecordfiledb.insert_totalgrade_me();
    }
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
			/* debuggable variable will remain false */
        }
        return debuggable;
    }
}
