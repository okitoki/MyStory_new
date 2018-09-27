package com.thedot.mystoryinenglishn.Index;

import java.io.Serializable;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class CategoryDetialData implements Serializable {
    private int content_id;
    private String caption_eng;
    private String caption_kor;
    private String cate_content_url;

    public int getContentId(){
        return content_id;
    }
    public String  getTitleEng(){
        return caption_eng;
    }
    public String  getTitleKor(){
        return caption_kor;
    }
    public String  getContnetUrl(){
        return cate_content_url;
    }

}
