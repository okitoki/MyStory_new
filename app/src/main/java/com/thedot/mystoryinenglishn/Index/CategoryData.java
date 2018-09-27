package com.thedot.mystoryinenglishn.Index;

        import java.io.Serializable;

/**
 * Created by okitoki on 2018. 2. 1..
 */

public class CategoryData implements Serializable {

    private int cate_id;
    private String caption_eng;
    private String caption_kor;
    private String image_url;
    private String image_big_url;
    private String cate_detailfile_url;
    private int unit_number;


    public int getCateId(){
        return cate_id;
    }
    public String getCaptionEng(){
        return caption_eng;
    }
    public String getCaptionKor(){
        return caption_kor;
    }
    public String getImageUrl(){
        return image_url;
    }
    public String getImage_big_url() {
        return image_big_url;
    }
    public String getCateDetailfileUrl(){
        return cate_detailfile_url;
    }
    public int getunitNumber(){
        return unit_number;
    }
}
