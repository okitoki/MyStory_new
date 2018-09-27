package com.thedot.mystoryinenglishn.Index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.thedot.mystoryinenglishn.Player.PlayActivity;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

import java.util.ArrayList;


public class PageOneListFragment extends Fragment {

    private CateListViewAdapter cateListViewAdapter;
    private ListView cateListView;
    private View root_view;
    private CategoryData cate_data;
    private ArrayList<CategoryDetialData> category_detail_data = new ArrayList<>();
    public TextView contenttitle;
    public RequestManager mGlideRequestManager;

    public static PageOneListFragment newInstance(ArrayList<CategoryData> categroydata, int position) {
        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putSerializable("categorydata", categroydata.get(position));

        PageOneListFragment fragment = new PageOneListFragment();
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
        mGlideRequestManager = Glide.with(this);
        root_view = inflater.inflate(R.layout.fragment_page_one_list, container, false);
        final View header = inflater.inflate(R.layout.fragment_page_one_list_header, null, false);
        cateListView = (ListView) root_view.findViewById(R.id.cateListView);
        cateListViewAdapter = new CateListViewAdapter();
        cateListView.setAdapter(cateListViewAdapter);
        cateListView.addHeaderView(header);

        cate_data = (CategoryData) getArguments().getSerializable("categorydata");

        final ToggleButton icon_like_heart = (ToggleButton) header.findViewById(R.id.btn_detail_like);
        ArrayList<Integer> like_cateId = ((MainActivity)getActivity()).getBaseApplication().getLikeCateIdVale();

        for(int id : like_cateId){
            if(id == cate_data.getCateId()) {
                icon_like_heart.setChecked(true);
            }

            PistolLogger.LOGD("HEAD Like Cate Id :" +Integer.toString(id));
        }

        icon_like_heart.setOnCheckedChangeListener(null);
        icon_like_heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    ((MainActivity)getActivity()).getBaseApplication().insertLikeValue(cate_data.getCateId(),1);
                    //animationDrawable.start();
                    PistolLogger.LOGD("Like Cate Id " +Integer.toString(cate_data.getCateId()));
                }else{
                    ((MainActivity)getActivity()).getBaseApplication().insertLikeValue(cate_data.getCateId(),0);
                    PistolLogger.LOGD("Like Cate Id " +Integer.toString(cate_data.getCateId()));
                    //animationDrawable.stop();
                }
            }
        });

        ImageView image_big_category = header.findViewById(R.id.image_big_category);
        mGlideRequestManager.load(getResources().getIdentifier(cate_data.getImage_big_url(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(image_big_category);
        cateListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (absListView.getFirstVisiblePosition() == 0 && absListView.getChildAt(0)!=null) {
//                    double listviewposition = (double) Math.abs(absListView.getChildAt(0).getTop()) / 600;
//                    RelativeLayout blackBlank = (RelativeLayout) view.findViewById(R.id.mainimg_blank);
//                    blackBlank.setVisibility(View.VISIBLE);
//                    blackBlank.setAlpha((float) listviewposition);
//                    PistolLogger.LOGD("scroll Y position : " + absListView.getChildAt(0).getTop() + " firstvisibleposition: " + absListView.getFirstVisiblePosition() + "listviewposition : " + Double.toString(listviewposition));
                }
            }
        });
//버튼 영역 확장
//        final View heart_btn_parent = (View) icon_like_heart.getParent();
//        heart_btn_parent.post(new Runnable() {
//            @Override
//            public void run() {
//                final Rect r = new Rect();
//                heart_btn_parent.getHitRect(r);
//                r.right += 1100;
//                r.left += 1100;
//                r.top += 1100;
//                r.bottom +=1100;
//                heart_btn_parent.setTouchDelegate(new TouchDelegate(r,icon_like_heart));
//
//            }
//        });

        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO 카테고리데이타
        category_detail_data = ((MainActivity) getActivity()).getCategoryDetaildata(getArguments().getInt("position"));
        setListView();
    }

    public void setListView(){
        int cateId = cate_data.getCateId();
        int contentId;
        int megrage = 0;
        int alexgrage = 0;

        for(int i=0;i<category_detail_data.size();i++){
            contentId = category_detail_data.get(i).getContentId()-1;
//            megrage =((MainActivity)getActivity()).getBaseApplication().getValueMeGrage(cateId,contentId);
            megrage =((MainActivity)getActivity()).getBaseApplication().getValue(cateId,contentId).size();
            alexgrage =((MainActivity)getActivity()).getBaseApplication().getValueAlexGrage(cateId,contentId);
            if(megrage==-1) megrage = 0;
            if(alexgrage == -1) alexgrage = 0;
            cateListViewAdapter.addItem(category_detail_data.get(i).getTitleEng(),category_detail_data.get(i).getTitleKor(), alexgrage, megrage);
        }
        cateListViewAdapter.notifyDataSetChanged();
        PistolLogger.LOGD("pageOnlistfrag setListView");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class CateListViewAdapter extends BaseAdapter{
        private ArrayList<CateListViewItem> listViewItem = new ArrayList<>();

        @Override
        public int getCount() {
            return listViewItem.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final int pos = position;
            final Context context = getActivity();

            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.cate_listview,viewGroup,false);
            }

            contenttitle = (TextView) view.findViewById(R.id.titleText);
            TextView contenttitle_kor = (TextView) view.findViewById(R.id.titleTextKor);
            TextView alexno = (TextView) view.findViewById(R.id.alexCount);
            TextView meno = (TextView) view.findViewById(R.id.meCount);

            final LinearLayout listWrap = (LinearLayout) view.findViewById(R.id.catelisWrap);
            if(pos%2==0) {
               listWrap.setBackgroundColor(getResources().getColor(R.color.listview_white_background));
            }else{
                listWrap.setBackgroundColor(getResources().getColor(R.color.listview_gray_background));
            }

            CateListViewItem l_viewItem = listViewItem.get(position);
            String contenttitle_str = Integer.toString(position+1) +". "+l_viewItem.getContentTitle();
            contenttitle.setText(contenttitle_str);
            contenttitle_kor.setText(l_viewItem.getContentTitleKor());
            alexno.setText(Integer.toString(l_viewItem.getAlexGrage()));
            meno.setText(Integer.toString(l_viewItem.getMeGrage()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Preferences.getExgistVideoFile(getContext(),String.valueOf(cate_data.getCateId()-1))) {
                        contenttitle.setTextColor(getResources().getColor(R.color.pager_list_title));
                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        intent.putExtra("cateId", cate_data.getCateId());
                        intent.putExtra("cateName", cate_data.getCaptionEng());
                        intent.putExtra("contentId", category_detail_data.get(pos).getContentId());
                        intent.putExtra("content_Jeson_Urls", getContentJesonUrl());
//                    intent.putExtra("contentJesonUrl",category_detail_data.get(pos).getContnetUrl());

                        PistolLogger.LOGD("ID ===== > cate:" + Integer.toString(cate_data.getCateId()) + ", contentID:" + Integer.toString(category_detail_data.get(pos).getContentId()));
                        int START_PLAYERACTIVITY_REQUEST_CODE = 100;
                        startActivityForResult(intent, START_PLAYERACTIVITY_REQUEST_CODE);

                    }else{
                        Toast.makeText(getContext(),"다운로드하면 에피소드를 이용할 수 있습니다.",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return view;
        }
        public ArrayList<String> getContentJesonUrl(){
            ArrayList<String> content_url = new ArrayList<>();
            for(CategoryDetialData data:category_detail_data){
                content_url.add(data.getContnetUrl());
            }
            return content_url;
        }
        @Override
        public Object getItem(int position) {
            return listViewItem.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String title,String kortitle, int alexno, int meno){
            CateListViewItem item = new CateListViewItem();
            item.setContentTitle(title);
            item.setContentTitleKor(kortitle);
            item.setAlexGrage(alexno);
            item.setMeGrage(meno);

            listViewItem.add(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100) {
            if (resultCode == Activity.RESULT_OK) {
                int resultCatId = data.getExtras().getInt("cateId");
                //int resultContentId = data.getExtras().getInt("contentId");
                for (int i = 0; i < cateListViewAdapter.listViewItem.size(); ++i) {
                    int megrage = ((MainActivity) getActivity()).getBaseApplication().getValue(resultCatId, i).size();
                    int alexgrage = ((MainActivity) getActivity()).getBaseApplication().getValueAlexGrage(resultCatId, i);
                    PistolLogger.LOGD("ID:[" + Integer.toString(i) + "], megrage value: " + Integer.toString(megrage) + ", alexgrage value: " + Integer.toString(alexgrage));
                    if (megrage > 0)
                        cateListViewAdapter.listViewItem.get(i).setMeGrage(megrage);
                    if (alexgrage > 0)
                        cateListViewAdapter.listViewItem.get(i).setAlexGrage(alexgrage);
                }
                cateListViewAdapter.notifyDataSetChanged();
                PistolLogger.LOGD("onActivityResult: [resultcode]-> " + Integer.toString(resultCode) + ", [requestCode] ->" + Integer.toString(requestCode) + ", [resultCatId] ->" + Integer.toString(resultCatId));
            }
        }
        }

    @Override
    public void onStart() {
        super.onStart();
        PistolLogger.LOGD("PageOneListFragment onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        PistolLogger.LOGD("PageOneListFragment onPause");
    }
}
