package com.thedot.mystoryinenglishn.Player;

import java.io.Serializable;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class ContentData implements Serializable {
    private int id;
    private String videoURL;
    private String sampleImages;
    private String word;
    private String syncInfo;
    private String content_subject;
    private String english_content_text;
    private String korean_content_text;
    private String[] eng_content_str;
    private String[] kor_content_str;

    public void setEngContentStr(String[] str){
        eng_content_str = str;
    }

    public void setKorContentStr(String[] str){
        kor_content_str = str;
    }

    public String[] getEngContentStr(){
        return eng_content_str;
    }
    public String[] getKorContentStr(){
        return kor_content_str;
    }
    public int getId(){
        return id;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getSampleImages() {
        return sampleImages;
    }

    public String getWord() {
        return word;
    }

    public String getContent_subject() {
        return content_subject;
    }

    public String getSyncInfo() {

        return syncInfo;
    }
    public String getEnglish_content_text() {
        return english_content_text;
    }
    public String getKorean_content_text() {
        return korean_content_text;
    }
}
