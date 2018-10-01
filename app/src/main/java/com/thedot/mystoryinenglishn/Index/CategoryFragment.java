package com.thedot.mystoryinenglishn.Index;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CategoryFragment extends Fragment {
    private static ArrayList<CategoryData> cate;
    public RequestManager mGlideRequestManager;

    public static CategoryFragment newInstance(ArrayList<CategoryData> _cate) {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        cate = _cate;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGlideRequestManager = Glide.with(this);
        View root_view = inflater.inflate(R.layout.fragment_category_new, container, false);
        Typeface typefaceserif = Typeface.createFromAsset(getActivity().getAssets(), "fonts/serifkregular.otf");
        final TextView basic = root_view.findViewById(R.id.categorytitle_basic);
        basic.setTypeface(typefaceserif);
//        basic.setIncludeFontPadding(false);
        TextView daily = root_view.findViewById(R.id.categorytitle_daily);
        daily.setTypeface(typefaceserif);
        TextView people = root_view.findViewById(R.id.categorytitle_people);
        people.setTypeface(typefaceserif);
        TextView travel = root_view.findViewById(R.id.categorytitle_travel);
        travel.setTypeface(typefaceserif);
        TextView issue = root_view.findViewById(R.id.categorytitle_issue);
        issue.setTypeface(typefaceserif);
        TextView about_korea = root_view.findViewById(R.id.categorytitle_country);
        about_korea.setTypeface(typefaceserif);

//        YouTubePlayer.OnInitializedListener playerListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if(youTubePlayer == null) return;
//                if(!b) youTubePlayer.cueVideo("j2IclHOmyaM");
//
//                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//            }
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };
//
//        YouTubePlayerSupportFragment myoutubefragment = YouTubePlayerSupportFragment.newInstance();
//        myoutubefragment.initialize("AIzaSyDziM4htwupGzV1UXIwVDNjKNpe0RWlqh8",playerListener);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.youtubeLayout, myoutubefragment);
//        fragmentTransaction.commit();

// basic 필수 카테고리
        LinearLayout cate_basic_con_detail = root_view.findViewById(R.id.cate_basic);
        HashMap<Integer,ImageButton> basic_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> basic_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> basic_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> basic_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> basic_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> basic_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> basic_title = new HashMap<Integer, String>();

        for(int i=0;i<=1;++i){
            basic_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_basic_con_detail.addView(basic_con_detail.get(i));
            basic_download.put(i,(ImageButton) basic_con_detail.get(i).findViewById(R.id.btn_download));
            basic_detail_engtitle.put(i,(TextView) basic_con_detail.get(i).findViewById(R.id.eng_title));
            basic_detail_kortitle.put(i,(TextView) basic_con_detail.get(i).findViewById(R.id.kor_title));
            basic_detail_img.put(i,(ImageView) basic_con_detail.get(i).findViewById(R.id.cate_img));
            basic_heart.put(i,(ToggleButton) basic_con_detail.get(i).findViewById(R.id.index_heart_icon));
            basic_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            basic_detail_engtitle.get(i).setText(basic_title.get(i));
            basic_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(basic_detail_img.get(i));
            basic_detail_img.get(i).setId(Integer.valueOf(i));
            basic_download.get(i).setTag(Integer.valueOf(i));
            basic_detail_img.get(i).setOnClickListener(bigimgClickListener);
            basic_download.get(i).setOnClickListener(downloadClickListener);
            //basic_heart.get(i).setId(Integer.valueOf(i));
//            basic_heart.get(i).setChecked(getCheckState(i));
            //basic_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }
// dairy 일상 카테고리
        LinearLayout cate_daily_con_detail = root_view.findViewById(R.id.cate_daily);

        HashMap<Integer,ImageButton> daily_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> daily_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> daily_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> daily_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> daily_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> daily_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> daily_title = new HashMap<Integer, String>();

        for(int i=2;i<=5;++i){
            daily_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_daily_con_detail.addView(daily_con_detail.get(i));
            daily_download.put(i,(ImageButton) daily_con_detail.get(i).findViewById(R.id.btn_download));
            daily_detail_engtitle.put(i,(TextView) daily_con_detail.get(i).findViewById(R.id.eng_title));
            daily_detail_kortitle.put(i,(TextView) daily_con_detail.get(i).findViewById(R.id.kor_title));
            daily_detail_img.put(i,(ImageView) daily_con_detail.get(i).findViewById(R.id.cate_img));
            daily_heart.put(i,(ToggleButton) daily_con_detail.get(i).findViewById(R.id.index_heart_icon));
            daily_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            daily_detail_engtitle.get(i).setText(daily_title.get(i));
            daily_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(daily_detail_img.get(i));
            daily_detail_img.get(i).setId(Integer.valueOf(i));
            daily_download.get(i).setTag(Integer.valueOf(i));
            daily_detail_img.get(i).setOnClickListener(bigimgClickListener);
            daily_download.get(i).setOnClickListener(downloadClickListener);
//            daily_heart.get(i).setId(Integer.valueOf(i));
//            daily_heart.get(i).setChecked(getCheckState(i));
//            daily_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }

        daily_detail_img.get(5).setPadding(0,0,50,0);

// people 사람 카테고리
        LinearLayout cate_people_con_detail = root_view.findViewById(R.id.cate_people);

        HashMap<Integer,ImageButton> people_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> people_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> people_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> people_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> people_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> people_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> people_title = new HashMap<Integer, String>();

        for(int i=6;i<=8;++i){
            people_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_people_con_detail.addView(people_con_detail.get(i));
            people_download.put(i,(ImageButton) people_con_detail.get(i).findViewById(R.id.btn_download));
            people_detail_engtitle.put(i,(TextView) people_con_detail.get(i).findViewById(R.id.eng_title));
            people_detail_kortitle.put(i,(TextView) people_con_detail.get(i).findViewById(R.id.kor_title));
            people_detail_img.put(i,(ImageView) people_con_detail.get(i).findViewById(R.id.cate_img));
            people_heart.put(i,(ToggleButton) people_con_detail.get(i).findViewById(R.id.index_heart_icon));
            people_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            people_detail_engtitle.get(i).setText(people_title.get(i));
            people_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(people_detail_img.get(i));
            people_detail_img.get(i).setId(Integer.valueOf(i));
            people_download.get(i).setTag(Integer.valueOf(i));
            people_detail_img.get(i).setOnClickListener(bigimgClickListener);
            people_download.get(i).setOnClickListener(downloadClickListener);
//            people_heart.get(i).setId(Integer.valueOf(i));
//            people_heart.get(i).setChecked(getCheckState(i));
//            people_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }

        people_detail_img.get(8).setPadding(0,0,50,0);

// travel 여행 카테고리
        LinearLayout cate_travel_con_detail = root_view.findViewById(R.id.cate_travel);
        HashMap<Integer,ImageButton> travel_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> travel_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> travel_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> travel_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> travel_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> travel_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> travel_title = new HashMap<Integer, String>();

        for(int i=9;i<=12;++i){
            travel_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_travel_con_detail.addView(travel_con_detail.get(i));
            travel_download.put(i,(ImageButton) travel_con_detail.get(i).findViewById(R.id.btn_download));
            travel_detail_engtitle.put(i,(TextView) travel_con_detail.get(i).findViewById(R.id.eng_title));
            travel_detail_kortitle.put(i,(TextView) travel_con_detail.get(i).findViewById(R.id.kor_title));
            travel_detail_img.put(i,(ImageView) travel_con_detail.get(i).findViewById(R.id.cate_img));
            travel_heart.put(i,(ToggleButton) travel_con_detail.get(i).findViewById(R.id.index_heart_icon));
            travel_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            travel_detail_engtitle.get(i).setText(travel_title.get(i));
            travel_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(travel_detail_img.get(i));
            travel_detail_img.get(i).setId(Integer.valueOf(i));
            travel_download.get(i).setTag(Integer.valueOf(i));
            travel_detail_img.get(i).setOnClickListener(bigimgClickListener);
            travel_download.get(i).setOnClickListener(downloadClickListener);
//            travel_heart.get(i).setId(Integer.valueOf(i));
//            travel_heart.get(i).setChecked(getCheckState(i));
//            travel_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }

        travel_detail_img.get(12).setPadding(0,0,50,0);

// issue 이슈 카테고리
        LinearLayout cate_issue_con_detail = root_view.findViewById(R.id.cate_issue);

        HashMap<Integer,ImageButton> issue_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> issue_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> issue_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> issue_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> issue_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> issue_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> issue_title = new HashMap<Integer, String>();

        for(int i=15;i<=16;++i){
            issue_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_issue_con_detail.addView(issue_con_detail.get(i));
            issue_download.put(i,(ImageButton) issue_con_detail.get(i).findViewById(R.id.btn_download));
            issue_detail_engtitle.put(i,(TextView) issue_con_detail.get(i).findViewById(R.id.eng_title));
            issue_detail_kortitle.put(i,(TextView) issue_con_detail.get(i).findViewById(R.id.kor_title));
            issue_detail_img.put(i,(ImageView) issue_con_detail.get(i).findViewById(R.id.cate_img));
            issue_heart.put(i,(ToggleButton) issue_con_detail.get(i).findViewById(R.id.index_heart_icon));
            issue_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            issue_detail_engtitle.get(i).setText(issue_title.get(i));
            issue_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(issue_detail_img.get(i));
            issue_detail_img.get(i).setId(Integer.valueOf(i));
            issue_download.get(i).setTag(Integer.valueOf(i));
            issue_detail_img.get(i).setOnClickListener(bigimgClickListener);
            issue_download.get(i).setOnClickListener(downloadClickListener);
//            issue_heart.get(i).setId(Integer.valueOf(i));
//            issue_heart.get(i).setChecked(getCheckState(i));
//            issue_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }

// country 국가 카테고리
        LinearLayout cate_country_con_detail = root_view.findViewById(R.id.cate_country);

        HashMap<Integer,ImageButton> country_download = new HashMap<Integer, ImageButton>();
        HashMap<Integer,View> country_con_detail = new HashMap<Integer, View>();
        HashMap<Integer,TextView> country_detail_engtitle = new HashMap<Integer, TextView>();
        HashMap<Integer,TextView> country_detail_kortitle = new HashMap<Integer, TextView>();
        HashMap<Integer,ImageView> country_detail_img = new HashMap<Integer, ImageView>();
        HashMap<Integer,ToggleButton> country_heart = new HashMap<Integer, ToggleButton>();
        HashMap<Integer,String> country_title = new HashMap<Integer, String>();

        for(int i=13;i<=14;++i){
            country_con_detail.put(i,(View)inflater.inflate(R.layout.cate_content_detail,null));
            cate_country_con_detail.addView(country_con_detail.get(i));
            country_download.put(i,(ImageButton) country_con_detail.get(i).findViewById(R.id.btn_download));
            country_detail_engtitle.put(i,(TextView) country_con_detail.get(i).findViewById(R.id.eng_title));
            country_detail_kortitle.put(i,(TextView) country_con_detail.get(i).findViewById(R.id.kor_title));
            country_detail_img.put(i,(ImageView) country_con_detail.get(i).findViewById(R.id.cate_img));
            country_heart.put(i,(ToggleButton) country_con_detail.get(i).findViewById(R.id.index_heart_icon));
            country_title.put(i,cate.get(i).getCaptionEng() +"(" + cate.get(i).getunitNumber() +")");

            country_detail_engtitle.get(i).setText(country_title.get(i));
            country_detail_kortitle.get(i).setText(cate.get(i).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(cate.get(i).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(country_detail_img.get(i));
            country_detail_img.get(i).setId(Integer.valueOf(i));
            country_download.get(i).setTag(Integer.valueOf(i));
            country_detail_img.get(i).setOnClickListener(bigimgClickListener);
            country_download.get(i).setOnClickListener(downloadClickListener);
//            country_heart.get(i).setId(Integer.valueOf(i));
//            country_heart.get(i).setChecked(getCheckState(i));
//            country_heart.get(i).setOnCheckedChangeListener(heartCheckListener);
        }
//top setting button and mystation button

        ImageButton btn_mystation = (ImageButton) root_view.findViewById(R.id.btn_category_mystation);
        btn_mystation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).endAnimationFirstContainer();
                ((MainActivity)getActivity()).setMystationFragment();
            }
        });

        ImageButton btn_setting = (ImageButton) root_view.findViewById(R.id.btn_category_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).endAnimationFirstContainer();
                ((MainActivity)getActivity()).setSettingFragment();
            }
        });

        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

//        ArrayList<String> aa = Preferences.getExgistVideoFile(getContext());
//        PistolLogger.LOGD("download id: ====>" + aa.get(0));

        return root_view;
    }

    View.OnClickListener bigimgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PistolLogger.LOGD("view.getTag().toString(): " + String.valueOf(view.getId()));
            ((MainActivity) getActivity()).setDetailCategoryView(cate, view.getId());
        }
    };

    View.OnClickListener downloadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PistolLogger.LOGD("view.getTag().toString(): " + String.valueOf(view.getTag()));
            ((MainActivity)getActivity()).clickDownload(Integer.parseInt(view.getTag().toString()));
        }
    };

    CompoundButton.OnCheckedChangeListener heartCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            int cateId = compoundButton.getId();
            if(checked) {
                ((MainActivity)getActivity()).getBaseApplication().insertLikeValue(cateId+1,1);
                PistolLogger.LOGD("Like Cate Id true: " +Integer.toString(cateId));
            }else{
                ((MainActivity)getActivity()).getBaseApplication().insertLikeValue(cateId+1,0);
                PistolLogger.LOGD("Like Cate Id false: " +Integer.toString(cateId));
            }
        }
    };

    public boolean getCheckState(int pos){
        ArrayList<Integer> like_cateId = ((MainActivity)getActivity()).getBaseApplication().getLikeCateIdVale();
        int current_pos = pos + 1;
        for(int id : like_cateId) {
            PistolLogger.LOGD("LIKE ID: " + Integer.toString(id) + ", POS : " + Integer.toString(current_pos));
            if (id == current_pos) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
