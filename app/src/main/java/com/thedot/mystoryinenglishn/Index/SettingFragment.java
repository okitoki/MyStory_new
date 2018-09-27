package com.thedot.mystoryinenglishn.Index;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
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
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.start_enter1);
        }else{
            return  AnimationUtils.loadAnimation(getActivity(), R.anim.start_end1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_setting, container, false);
        Boolean alex_auto_play = Preferences.getAlexAutoPlay(getActivity());
        Boolean me_auto_play = Preferences.getMeAutoPlay(getActivity());
        Switch switch_alex = (Switch) root_view.findViewById(R.id.switch_alex_autoplay);
        Switch switch_me = (Switch) root_view.findViewById(R.id.switch_me_autoplay);
        switch_alex.setChecked(alex_auto_play);
        switch_me.setChecked(me_auto_play);

        TextView personal_info_click = root_view.findViewById(R.id.personal_info_click);
        personal_info_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.the-dot.co.kr/portfolio/our-story-privacy-policy/"));
                startActivity(intent);
            }
        });

        switch_alex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischange) {
                if(ischange) {
                    Preferences.setAlexAutoPlay(getActivity(),true);
                }else{
                    Preferences.setAlexAutoPlay(getActivity(),false);
                }
            }
        });

        switch_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischange) {
                if(ischange) {
                    Preferences.setMeAutoPlay(getActivity(),true);
                }else{
                    Preferences.setMeAutoPlay(getActivity(),false);
                }
            }
        });

//        RadioGroup avata_group = (RadioGroup) root_view.findViewById(R.id.radio_group);
//        avata_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
//                if(checkId==R.id.radio_avata1){
//                    Preferences.setMyavatar(getActivity(),0);
//                    PistolLogger.LOGD("preferences1");
//                }else if(checkId==R.id.radio_avata2){
//                    Preferences.setMyavatar(getActivity(),1);
//                    PistolLogger.LOGD("preferences2");
//                }else if(checkId==R.id.radio_avata3){
//                    Preferences.setMyavatar(getActivity(),2);
//                    PistolLogger.LOGD("preferences3");
//                }else if(checkId==R.id.radio_avata4){
//                    Preferences.setMyavatar(getActivity(),3);
//                    PistolLogger.LOGD("preferences4");
//                }
//
////                else if(checkId==R.id.radio_avata5){
////                    Preferences.setMyavatar(getActivity(),4);
////                    PistolLogger.LOGD("preferences5");
////                }else if(checkId==R.id.radio_avata6){
////                    Preferences.setMyavatar(getActivity(),5);
////                    PistolLogger.LOGD("preferences6");
////                }else if(checkId==R.id.radio_avata7){
////                    Preferences.setMyavatar(getActivity(),6);
////                    PistolLogger.LOGD("preferences7");
////                }else if(checkId==R.id.radio_avata8){
////                    Preferences.setMyavatar(getActivity(),7);
////                    PistolLogger.LOGD("preferences8");
////                }
//            }
//        });

        final RadioButton radio_avata1 = (RadioButton) root_view.findViewById(R.id.radio_avata1);
        final RadioButton radio_avata2 = (RadioButton) root_view.findViewById(R.id.radio_avata2);
        final RadioButton radio_avata3 = (RadioButton) root_view.findViewById(R.id.radio_avata3);
        final RadioButton radio_avata4 = (RadioButton) root_view.findViewById(R.id.radio_avata4);
        final RadioButton radio_avata5 = (RadioButton) root_view.findViewById(R.id.radio_avata5);
        final RadioButton radio_avata6 = (RadioButton) root_view.findViewById(R.id.radio_avata6);
        final RadioButton radio_avata7 = (RadioButton) root_view.findViewById(R.id.radio_avata7);
        final RadioButton radio_avata8 = (RadioButton) root_view.findViewById(R.id.radio_avata8);


        int radio_avata_id = Preferences.getMyavatar(getActivity());
        if(radio_avata_id==0) {
            radio_avata1.setChecked(true);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);

        }else if(radio_avata_id==1){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(true);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==2){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(true);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==3){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(true);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==4){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(true);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==5){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(true);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==6){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(true);
            radio_avata8.setChecked(false);
        }else if(radio_avata_id==7){
            radio_avata1.setChecked(false);
            radio_avata2.setChecked(false);
            radio_avata3.setChecked(false);
            radio_avata4.setChecked(false);
            radio_avata5.setChecked(false);
            radio_avata6.setChecked(false);
            radio_avata7.setChecked(false);
            radio_avata8.setChecked(true);
        }

        radio_avata1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check)  {
                    Preferences.setMyavatar(getActivity(),0);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });
        radio_avata2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check) {
                    Preferences.setMyavatar(getActivity(), 1);
                    radio_avata1.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });
        radio_avata3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    Preferences.setMyavatar(getActivity(),2);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });

        radio_avata4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check) {
                    Preferences.setMyavatar(getActivity(), 3);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });
        radio_avata5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    Preferences.setMyavatar(getActivity(),4);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });
        radio_avata6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    Preferences.setMyavatar(getActivity(),5);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata7.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });
        radio_avata7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    Preferences.setMyavatar(getActivity(),6);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata8.setChecked(false);
                }
            }
        });

        radio_avata8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check) {
                    Preferences.setMyavatar(getActivity(),7);
                    radio_avata1.setChecked(false);
                    radio_avata2.setChecked(false);
                    radio_avata3.setChecked(false);
                    radio_avata4.setChecked(false);
                    radio_avata5.setChecked(false);
                    radio_avata6.setChecked(false);
                    radio_avata7.setChecked(false);
                }
            }
        });

        ImageButton btn_back = (ImageButton) root_view.findViewById(R.id.btn_setting_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });



        return root_view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PistolLogger.LOGD("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
