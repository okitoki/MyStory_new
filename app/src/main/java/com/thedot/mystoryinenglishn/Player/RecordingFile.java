package com.thedot.mystoryinenglishn.Player;

import java.io.Serializable;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class RecordingFile implements Serializable {
    public int id;
    public int categoryid;
    public int contentid;
    public String contentname;
    public String contentdata;
    public String contentpath;

    public RecordingFile(int _id, int _categoryid, int _contentid, String _contentname, String _contentdata, String _contentpath){
        id = _id;
        categoryid = _categoryid;
        contentid = _contentid;
        contentname = _contentname;
        contentdata = _contentdata;
        contentpath = _contentpath;
    }

    public int getCategoryid(){

        return categoryid;
    }
    public int getContentid(){
        return contentid;
    }
    public String getContentname(){
        return contentname;

    }
    public String getContentdata(){
        return contentdata;

    }
    public String getContentPath(){
        return contentpath;
    }
}
