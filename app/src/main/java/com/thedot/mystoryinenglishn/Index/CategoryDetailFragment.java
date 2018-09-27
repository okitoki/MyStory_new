package com.thedot.mystoryinenglishn.Index;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CategoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDetailFragment extends Fragment {

    private View root_view;

    public static CategoryDetailFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position",position);

        CategoryDetailFragment fragment = new CategoryDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_detailcategory, container, false);
        ImageButton btn_back = (ImageButton) root_view.findViewById(R.id.btn_detailcategory_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        ImageButton btn_setting = (ImageButton) root_view.findViewById(R.id.btn_detailcategory_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).endAnimationFirstContainer();
                ((MainActivity)getActivity()).setSettingFragment();
            }
        });
        int current_cate_position = getArguments().getInt("position");
        downloadButtonShow(current_cate_position);
        setCategoryName(current_cate_position);
        setViewpager();

        Button download_button = (Button) root_view.findViewById(R.id.btn_bottom_download);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).clickDownload(getArguments().getInt("position"));
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
    public void downloadButtonShow(int cate){

        RelativeLayout layout_download_button = (RelativeLayout) root_view.findViewById(R.id.layout_download);
        if(Preferences.getExgistVideoFile(getContext(),String.valueOf(cate))){
            layout_download_button.setVisibility(View.GONE);

        }else{
            layout_download_button.setVisibility(View.VISIBLE);

        }

    }
    public void setCategoryName(int pos){
        TextView category_name = (TextView) root_view.findViewById(R.id.category_name);
        String category_name_str = ((MainActivity)getActivity()).getCate_data().get(pos).getCaptionEng();
        category_name.setVisibility(View.INVISIBLE);
        category_name.setText(category_name_str);
        Animation fadein = AnimationUtils.loadAnimation(getActivity(),R.anim.fadein);
        category_name.setAnimation(fadein);
        category_name.setVisibility(View.VISIBLE);
    }
    public void setViewpager(){
        final ListPagerStateAdapter listpageAdaper = new ListPagerStateAdapter(getActivity().getSupportFragmentManager(),((MainActivity)getActivity()).getCate_data());
        final ViewPager viewpager = (ViewPager) root_view.findViewById(R.id.viewpager);
        viewpager.setAdapter(listpageAdaper);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setCurrentItem(getArguments().getInt("position"));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PistolLogger.LOGD( "position: " + position + " positionOffsetPixels: "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                setCategoryName(position);
                downloadButtonShow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                PistolLogger.LOGD( "position: ");

            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
