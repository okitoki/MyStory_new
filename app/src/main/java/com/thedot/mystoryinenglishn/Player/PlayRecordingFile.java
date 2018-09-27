package com.thedot.mystoryinenglishn.Player;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class PlayRecordingFile  {
    private int playid;
    private ArrayList<String> contentnamearr = new ArrayList<>();
    private ArrayList<String> playfilenamearr = new ArrayList<>();
    private ArrayList<Boolean> playcheckedarr = new ArrayList<>();

    public int getPlayid() {
        return playid;
    }
    public ArrayList<Boolean> getplaycheckedarr() {
        return playcheckedarr;
    }

    public ArrayList<String> getContentnamearr() {
        return contentnamearr;
    }

    public ArrayList<String> getPlayfilenamearr() {
        return playfilenamearr;
    }

    public void setPlayid(int playid) {
        this.playid = playid;
    }

    public void setContentnamearr(ArrayList<String> contentnamearr) {
        this.contentnamearr = contentnamearr;
    }
    public void setPlaycheckedarr(ArrayList<Boolean> contentnamearr) {
        this.playcheckedarr = contentnamearr;
    }

    public void setPlayfilenamearr(ArrayList<String> playfilenamearr) {
        this.playfilenamearr = playfilenamearr;
    }

    public void removePlayfilenamearr(String filename){
        Iterator<String> iter = getPlayfilenamearr().iterator();
        while(iter.hasNext()){
            String str = iter.next();
            if(str.equals(filename)) iter.remove();
        }
    }

    public void removeContentnamearr(String contentname){
        Iterator<String> iter = getPlayfilenamearr().iterator();
        while(iter.hasNext()){
            String str = iter.next();
            if(str.equals(contentname)) iter.remove();
        }
    }

    public int getArrSize(){
        return getPlayfilenamearr().size();
    }

    public Boolean next(int id){
        if(id < getArrSize() && getplaycheckedarr().get(id)){
            return true;
        }
        return false;
    }
    public Boolean prev(int id){
        if(id>0) {
            return true;
        }
        return false;
    }

}
