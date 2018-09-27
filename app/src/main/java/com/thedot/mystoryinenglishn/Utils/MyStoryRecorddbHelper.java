package com.thedot.mystoryinenglishn.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thedot.mystoryinenglishn.Player.RecordingFile;

import java.util.ArrayList;

/**
 * Created by okitoki on 2018. 1. 25..
 */

public class MyStoryRecorddbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE =" INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_RECORDER_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.CATEGORU_ID + INT_TYPE + COMMA_SEP +
                    FeedEntry.CONTENT_ID + INT_TYPE + COMMA_SEP +
                    FeedEntry.CONTENT_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.CONTENT_DATA + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.CONTENT_PATH + TEXT_TYPE  + ")";

    private static final String SQL_MYGRADE_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME_GRADE + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.CATEGORU_ID + INT_TYPE + COMMA_SEP +
                    FeedEntry.CONTENT_ID + INT_TYPE + COMMA_SEP +
                    FeedEntry.NUMBER_ALEX + INT_TYPE + COMMA_SEP +
                    FeedEntry.NUMBER_ME + INT_TYPE + ")";

    private static final String SQL_MYTOTALGRADE_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME_TOTALGRADE + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.NUMBER_ALEX + INT_TYPE + COMMA_SEP +
                    FeedEntry.NUMBER_ME + INT_TYPE + ")";

    private static final String SQL_LIKE_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME_LIKE + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.CATEGORU_ID + INT_TYPE + COMMA_SEP +
                    FeedEntry.LIKE_VALUE + INT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    private static final String MYGRADESQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_GRADE;

    private static final String MYTOTALGRADESQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_TOTALGRADE;

    private static final String LIKE_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_LIKE;
    public MyStoryRecorddbHelper(Context context, String _DATABASE_NAME, int _DATABASE_VERSION) {
        super(context, _DATABASE_NAME, null, _DATABASE_VERSION);
        DATABASE_NAME = _DATABASE_NAME;
        DatabaseManager.initializeInstance(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(SQL_RECORDER_CREATE_ENTRIES);
      db.execSQL(SQL_MYGRADE_CREATE_ENTRIES);
      db.execSQL(SQL_MYTOTALGRADE_CREATE_ENTRIES);
      db.execSQL(SQL_LIKE_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(MYGRADESQL_DELETE_ENTRIES);
        db.execSQL(MYTOTALGRADESQL_DELETE_ENTRIES);
        db.execSQL(LIKE_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(int categoryid, int contentid, String name, String data, String path){
        ContentValues values = new ContentValues();
        values.put(FeedEntry.CATEGORU_ID, categoryid);
        values.put(FeedEntry.CONTENT_ID, contentid);
        values.put(FeedEntry.CONTENT_NAME, name);
        values.put(FeedEntry.CONTENT_DATA, data);
        values.put(FeedEntry.CONTENT_PATH, path);
        DatabaseManager.getInstance().openDatabase().insert(FeedEntry.TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert_grade(int categoryid, int contentid, int alex, int me){

        ContentValues values = new ContentValues();
        values.put(FeedEntry.CATEGORU_ID, categoryid);
        values.put(FeedEntry.CONTENT_ID, contentid);
        values.put(FeedEntry.NUMBER_ALEX, alex);
        values.put(FeedEntry.NUMBER_ME, me);
        DatabaseManager.getInstance().openDatabase().insert(FeedEntry.TABLE_NAME_GRADE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert_totalgrade(){
        ContentValues values = new ContentValues();
        values.put(FeedEntry.NUMBER_ALEX, 0);
        values.put(FeedEntry.NUMBER_ME, 0);
        DatabaseManager.getInstance().openDatabase().insert(FeedEntry.TABLE_NAME_TOTALGRADE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert_totalgrade_alex(){
        Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery("select * from " +FeedEntry.TABLE_NAME_TOTALGRADE, null);
        int cursor_size = cursor.getCount();
        int totalgrade;
        if(cursor_size>0) {
            cursor.moveToFirst();
            totalgrade = cursor.getInt(1);
        }else{
            insert_totalgrade();
            totalgrade=cursor_size;
        }

        ++totalgrade;
        String UPDATE = "UPDATE " + FeedEntry.TABLE_NAME_TOTALGRADE + " SET "+FeedEntry.NUMBER_ALEX+ "=" + totalgrade;
        DatabaseManager.getInstance().openDatabase().execSQL(UPDATE) ;
        DatabaseManager.getInstance().closeDatabase();

        PistolLogger.LOGD("insert_total_alex:" + Integer.toString(totalgrade));
    }

    public void insert_totalgrade_me(){

        Cursor cursor = DatabaseManager.getInstance().openDatabase().rawQuery("select * from " +FeedEntry.TABLE_NAME_TOTALGRADE, null);
        int cursor_size = cursor.getCount();
        int totalgrade;
        if(cursor_size>0) {
            cursor.moveToFirst();
            totalgrade = cursor.getInt(2);
        }else{
            insert_totalgrade();
            totalgrade=cursor_size;
        }
        ++totalgrade;
        String UPDATE = "UPDATE " + FeedEntry.TABLE_NAME_TOTALGRADE + " SET "+FeedEntry.NUMBER_ME+ "=" + totalgrade;
        DatabaseManager.getInstance().openDatabase().execSQL(UPDATE) ;
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert_like(int categoryid,int value){
//        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(get_like_cateid(categoryid).size()>0) {
            String UPDATE = "UPDATE " + FeedEntry.TABLE_NAME_LIKE + " SET "+FeedEntry.LIKE_VALUE+ "=" + value  + " WHERE " + FeedEntry.CATEGORU_ID + "=" + categoryid;
            DatabaseManager.getInstance().openDatabase().execSQL(UPDATE) ;
            PistolLogger.LOGD("updata:" +Integer.toString(categoryid)+", " + Integer.toString(value));
        }else{
            values.put(FeedEntry.CATEGORU_ID, categoryid);
            values.put(FeedEntry.LIKE_VALUE, value);
            DatabaseManager.getInstance().openDatabase().insert(FeedEntry.TABLE_NAME_LIKE,null,values);
            PistolLogger.LOGD("insert:"+Integer.toString(categoryid)+", "+ Integer.toString(value));
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public ArrayList<Integer>  get_like_cateid(int category_id, int value){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME_LIKE + " where " + FeedEntry.CATEGORU_ID + " = " + category_id + " AND "+ FeedEntry.LIKE_VALUE + " = " + value,null ) ;
        ArrayList<Integer> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(2));
        }

        return result;
    }
    public ArrayList<Integer>  get_like_cateid(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME_LIKE + " ORDER BY CAST("+ FeedEntry.CATEGORU_ID +" AS INTEGER) ASC",null ) ;
        ArrayList<Integer> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            if(cursor.getInt(2)==1) {
                result.add(cursor.getInt(1));
            }
        }
        return result;
    }
    public ArrayList<Integer>  get_like_cateid(int category_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME_LIKE + " where " + FeedEntry.CATEGORU_ID +" = " + category_id,null ) ;
        ArrayList<Integer> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            if(cursor.getInt(2)==1) {
                result.add(cursor.getInt(1));
            }
        }

        return result;
    }

    public void update_grade_alex(int categoryid, int contentid, int alexvalue){
        String UPDATE = "UPDATE " + FeedEntry.TABLE_NAME_GRADE +
                " SET "+FeedEntry.NUMBER_ALEX+ "=" + alexvalue + " WHERE " +
                FeedEntry.CATEGORU_ID + "=" + categoryid +" AND " + FeedEntry.CONTENT_ID + "=" + contentid;
        DatabaseManager.getInstance().openDatabase().execSQL(UPDATE) ;
        DatabaseManager.getInstance().closeDatabase();

    }

    public void update_grade_me(int categoryid, int contentid, int mevalue){
        String UPDATE = "UPDATE " + FeedEntry.TABLE_NAME_GRADE +
                " SET "+FeedEntry.NUMBER_ME+ "=" + mevalue  + " WHERE " +
                FeedEntry.CATEGORU_ID + "=" + categoryid +" AND " + FeedEntry.CONTENT_ID + "=" + contentid;
        DatabaseManager.getInstance().openDatabase().execSQL(UPDATE) ;
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteValue(String _name){
        String DEL = "DELETE FROM " + FeedEntry.TABLE_NAME +" WHERE "+FeedEntry.CONTENT_NAME+ "=" +"'" +_name +"'";
        DatabaseManager.getInstance().openDatabase().execSQL(DEL) ;
        DatabaseManager.getInstance().closeDatabase();
    }

    public int getValueAlexGrage(int categoryid, int contentid){
        int n = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +FeedEntry.TABLE_NAME_GRADE +  " where " + FeedEntry.CATEGORU_ID + "=" + "'" + categoryid + "'" + " AND " +
                FeedEntry.CONTENT_ID +"=" + "'" + contentid + "'", null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            n = cursor.getInt(3);
        }
        return n;
    }
    public int getValueMeGrage(int categoryid, int contentid){
        int n = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +FeedEntry.TABLE_NAME_GRADE +  " where " + FeedEntry.CATEGORU_ID + "="+ "'" + categoryid + "'" + " AND " +
                FeedEntry.CONTENT_ID +"=" + "'" + contentid + "'", null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            n = cursor.getInt(4);
        }

        return n;
    }
    public int getValueMeTotalGrage(){
        int n = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +FeedEntry.TABLE_NAME_TOTALGRADE, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            n = cursor.getInt(2);
        }

        return n;
    }
    public int getValueAlexTotalGrage(){
        int n = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " +FeedEntry.TABLE_NAME_TOTALGRADE, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            n = cursor.getInt(1);
        }

        return n;
    }
    public ArrayList<RecordingFile> getValueFromContentID(int _cateId, int _contentId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecordingFile> recordingfile = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " +FeedEntry.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            RecordingFile file = new RecordingFile(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            if(_cateId==cursor.getInt(1) && _contentId == cursor.getInt(2)) recordingfile.add(file);
        }

        return recordingfile;
    }

    public String getFilePathValueFromContentName(String _name){
        SQLiteDatabase db = getReadableDatabase() ;
        String select = "select * from " + FeedEntry.TABLE_NAME +" WHERE "+FeedEntry.CONTENT_NAME+ "=" +"'" +_name +"'";

        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        String filepath=cursor.getString(5);
        PistolLogger.LOGD(filepath);
        return filepath;
    }

    class FeedEntry{
        public static final String _ID = "id";
        public static final String TABLE_NAME = "record";
        public static final String CATEGORU_ID = "categoryid";
        public static final String CONTENT_ID ="contentid";
        public static final String CONTENT_NAME = "name";
        public static final String CONTENT_DATA = "data";
        public static final String CONTENT_PATH = "path";

        public static final String TABLE_NAME_GRADE = "grade";
        public static final String NUMBER_ALEX = "number_alex";
        public static final String NUMBER_ME = "number_me";

        public static final String TABLE_NAME_TOTALGRADE = "totalgrade";

        public static final String TABLE_NAME_LIKE = "like_state";
        public static final String LIKE_VALUE = "like_balue";
    }
}