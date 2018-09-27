package com.thedot.mystoryinenglishn.Setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 1. FileNae  :
 * 2. Package  :
 * 3. Comment  :
 * 4. 작성자   :
 * 5. 작성일   :
 **/

public class Preferences {
    private static String ALEXAUTOPLAY = "ALEXTAUTOPLAY";
    private static String MEAUTOPLAY = "MEAUTOPLAY";
    private static String MYAVATAR = "MYAVATAR";
    private static String CONTINUREPLAY = "CONTINUREPLAY";
    private static String PERSNALINFO = "PERSNALINFO";
    private static String RESOURSE = "RESOURSE";

    /************************************************************************
     * RESOURSE FILE EXIGST
     ************************************************************************/

    public static Boolean getExgistVideoFile(Context context, String lession){
        SharedPreferences pref = context.getSharedPreferences(RESOURSE, Context.MODE_PRIVATE);
        return pref.getBoolean(lession, false);
    }

    public static void setExgistVideoFile(Context context, String lession, Boolean value){
        SharedPreferences pref = context.getSharedPreferences(RESOURSE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(lession, value);
        edit.commit();
    }

    /************************************************************************
     * ALEX AUTO PLAY
     ************************************************************************/

    public static Boolean getAlexAutoPlay(Context context){
    SharedPreferences pref = context.getSharedPreferences(ALEXAUTOPLAY, Context.MODE_PRIVATE);
    return pref.getBoolean(ALEXAUTOPLAY, false);
    }

    public static void setAlexAutoPlay(Context context, Boolean value){
        SharedPreferences pref = context.getSharedPreferences(ALEXAUTOPLAY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(ALEXAUTOPLAY, value);
        edit.commit();
    }

    /************************************************************************
     * ME AUTO PLAY
     ************************************************************************/

    public static Boolean getMeAutoPlay(Context context){
        SharedPreferences pref = context.getSharedPreferences(MEAUTOPLAY, Context.MODE_PRIVATE);
        return pref.getBoolean(MEAUTOPLAY, true);
    }

    public static void setMeAutoPlay(Context context, Boolean value){
        SharedPreferences pref = context.getSharedPreferences(MEAUTOPLAY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(MEAUTOPLAY, value);
        edit.commit();
    }
    /************************************************************************
     * ONE REPLAY
     ************************************************************************/

    public static Boolean getContinuReplay(Context context){
        SharedPreferences pref = context.getSharedPreferences(CONTINUREPLAY, Context.MODE_PRIVATE);
        return pref.getBoolean(MEAUTOPLAY, false);
    }

    public static void setContinuReplay(Context context, Boolean value){
        SharedPreferences pref = context.getSharedPreferences(CONTINUREPLAY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(MEAUTOPLAY, value);
        edit.commit();
    }
    /************************************************************************
     * MY AVATAR
     ************************************************************************/

    public static int getMyavatar(Context context){
        SharedPreferences pref = context.getSharedPreferences(MYAVATAR, Context.MODE_PRIVATE);
        return pref.getInt(MYAVATAR, 0);
    }

    public static void setMyavatar(Context context, int value){
        SharedPreferences pref = context.getSharedPreferences(MYAVATAR, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(MYAVATAR, value);
        edit.commit();
    }

    /************************************************************************
     * persnal enter
     ************************************************************************/

    public static Boolean getPersonalInfo(Context context){
        SharedPreferences pref = context.getSharedPreferences(PERSNALINFO, Context.MODE_PRIVATE);
        return pref.getBoolean(PERSNALINFO, false);
    }

    public static void setPersonalInfo(Context context, Boolean value){
        SharedPreferences pref = context.getSharedPreferences(PERSNALINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(PERSNALINFO, value);
        edit.commit();
    }
}
