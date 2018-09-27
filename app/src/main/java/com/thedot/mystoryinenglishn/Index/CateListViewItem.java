package com.thedot.mystoryinenglishn.Index;

import java.io.Serializable;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class CateListViewItem implements Serializable {

    private int contentId;
    private String contentTitle;
    private  String contentTitleKor;
    private int alexGrage;
    private int meGrage;
    private int rest_number;

    public void setContentId(int _contentId){
        contentId = _contentId;
    }
    public void setContentTitle(String _title){
        contentTitle = _title;
    }
    public void setContentTitleKor(String _title){
        contentTitleKor = _title;
    }
    public void setAlexGrage(int _alexgrade){
        alexGrage = _alexgrade;
    }
    public void setMeGrage(int _megrade){
        meGrage = _megrade;
    }

    public int getContentId(){
        return contentId;
    }
    public String getContentTitle(){
        return contentTitle;
    }
    public String getContentTitleKor(){
        return contentTitleKor;
    }
    public int getAlexGrage(){
        return alexGrage;
    }
    public int getMeGrage(){
        return meGrage;
    }

}
