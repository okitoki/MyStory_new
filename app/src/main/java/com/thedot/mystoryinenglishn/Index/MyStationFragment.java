package com.thedot.mystoryinenglishn.Index;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

import java.util.ArrayList;
import java.util.Random;

public class MyStationFragment extends Fragment {

    public RequestManager mGlideRequestManager;
    public View root_view;
    public static MyStationFragment newInstance() {
        Bundle args = new Bundle();
        MyStationFragment fragment = new MyStationFragment();
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
        root_view = inflater.inflate(R.layout.fragment_mystation, container, false);
        String total_number_alex = String.format("%03d",((MainActivity)getActivity()).getBaseApplication().getValueAlexTotalGrage());
        String total_number_me = String.format("%03d",((MainActivity)getActivity()).getBaseApplication().getValueMeTotalGrage());
        TextView total_grage_alex = (TextView) root_view.findViewById(R.id.total_grage_alex);
        TextView total_grage_me = (TextView) root_view.findViewById(R.id.total_grage_me);
        total_grage_alex.setText(total_number_alex);
        total_grage_me.setText(total_number_me);

        ArrayList<CategoryData> categorydata = ((MainActivity)getActivity()).getCate_data();
        ArrayList<Integer> like_cate_id = ((MainActivity)getActivity()).getBaseApplication().getLikeCateIdVale();
        ArrayList<CategoryData> like_categorydata = new ArrayList<CategoryData>();
        for(int cateid:like_cate_id){
            for(int i = 0; i<categorydata.size();++i) {
                if (cateid == categorydata.get(i).getCateId()) {
                    like_categorydata.add(categorydata.get(i));
                }
            }
        }
//        for(CategoryData cateid:like_categorydata) {
//            PistolLogger.LOGD("like cate Id:" + cateid.getCaptionEng());
//        }
        RecyclerView mycate_recyclerview = (RecyclerView) root_view.findViewById(R.id.mycate_recyclerview);
        MyLikeCateRecyclerViewAdapter categoryviewAdapter = new MyLikeCateRecyclerViewAdapter(like_categorydata);
        mycate_recyclerview.setAdapter(categoryviewAdapter);
        mycate_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        ImageButton btn_back = (ImageButton) root_view.findViewById(R.id.btn_mystation_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).onBackPressed();
                ((MainActivity)getActivity()).enterAnimationFirstContainer();
            }
        });

        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mGlideRequestManager = Glide.with(this);
        setFightingData();
        return root_view;
    }

    public void setFightingData(){

        ImageView img_gage = (ImageView) root_view.findViewById(R.id.img_fighting);
        TextView fighting_txt_left = (TextView) root_view.findViewById(R.id.text_fighting_left);
        TextView fighting_txt_right = (TextView) root_view.findViewById(R.id.text_fighting_right);

        Drawable avata_img = null;
        int alexno = ((MainActivity)getActivity()).getBaseApplication().getValueAlexTotalGrage();
        int meno = ((MainActivity)getActivity()).getBaseApplication().getValueMeTotalGrage();
        int gnum = alexno + meno ;

        int[] reco = {5,10,30,40,60,80,100,100000};

        if(gnum == 0){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img1);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt1);
            fighting_txt_right.setText(R.string.fighting_right_txt1);
        }else if(gnum>0 && gnum <= reco[0]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img2);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt2);
            fighting_txt_right.setText(R.string.fighting_right_txt2);
        }else if(gnum>reco[0] && gnum <= reco[1]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img3);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt3);
            fighting_txt_right.setText(R.string.fighting_right_txt3);
        }else if(gnum>reco[1] && gnum <= reco[2]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img4);
            fighting_txt_left.setText(R.string.fighting_left_txt4);
            fighting_txt_right.setText(R.string.fighting_right_txt4);
            img_gage.setImageDrawable(avata_img);
        }else if(gnum>reco[2] && gnum <= reco[3]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img5);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt5);
            fighting_txt_right.setText(R.string.fighting_right_txt5);
        }else if(gnum>reco[3] && gnum <= reco[4]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img6);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt6);
            fighting_txt_right.setText(R.string.fighting_right_txt6);
        }else if(gnum>reco[4] && gnum < reco[5]){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img7);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt7);
            fighting_txt_right.setText(R.string.fighting_right_txt7);
        }else{
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img7);
            img_gage.setImageDrawable(avata_img);
            fighting_txt_left.setText(R.string.fighting_left_txt8);
            fighting_txt_right.setText(R.string.fighting_right_txt8);
        }
    }

    public Drawable getRandomGageDrawable(){
        Drawable avata_img = null;
        Random random = new Random();
        int num = random.nextInt(7);

        if(num==0){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img1);
        }else if(num==1){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img2);
        }else if(num==2){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img3);
        }else if(num==3){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img4);
        }else if(num==4){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img5);
        }else if(num==5){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img6);
        }else if(num==6){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.fighting_img7);
        }

        return avata_img;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class MyLikeCateRecyclerViewAdapter extends RecyclerView.Adapter<MyLikeCateRecyclerViewAdapter.ViewHolder>{

        private ArrayList<CategoryData> like_categorydata;
        private View root_view;

        public MyLikeCateRecyclerViewAdapter( ArrayList<CategoryData> data){
            like_categorydata=data;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            private RecyclerView listrecyclerview;
            private ImageView title_img;
            private TextView title_cate_eng;
            private TextView title_cate_kor;
            private LinearLayout wrap_layout;
            public ViewHolder(View itemView) {
                super(itemView);
                title_cate_eng = (TextView) itemView.findViewById(R.id.title_cate_eng);
                title_cate_kor = (TextView) itemView.findViewById(R.id.title_cate_kor);
                wrap_layout = (LinearLayout) itemView.findViewById(R.id.mystation_wrap);
                title_img = (ImageView) itemView.findViewById(R.id.cateimg_mystation);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = getActivity();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            root_view = inflater.inflate(R.layout.recyclerview_mystation_header, parent, false);

            return new MyLikeCateRecyclerViewAdapter.ViewHolder(root_view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String engtitlestr = "<font color='red'>" + String.format("%02d",like_categorydata.get(position).getCateId()) +"</font>&nbsp;"+like_categorydata.get(position).getCaptionEng();

            final int pos = position;
            holder.title_cate_eng.setText(Html.fromHtml(engtitlestr));
            holder.title_cate_kor.setText(like_categorydata.get(position).getCaptionKor());
            mGlideRequestManager.load(getResources().getIdentifier(like_categorydata.get(pos).getImageUrl(), "drawable", ((MainActivity)getActivity()).getPackageName())).into(holder.title_img);
            root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<CategoryData> data = ((MainActivity)getActivity()).getCate_data();
                    ((MainActivity)getActivity()).setDetailCategoryView_third_depth(data,like_categorydata.get(pos).getCateId()-1);
                }
            });
            if(position%2==0) {
                holder.wrap_layout.setBackgroundColor(getResources().getColor(R.color.listview_gray_background));
            }else{
                holder.wrap_layout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        }

        @Override
        public int getItemViewType(int position) {
            int viewType=0;
            if(position%2==0) {
                viewType = 0;
            }else{
                viewType = 1;
            }

            return viewType;
        }

        @Override
        public int getItemCount() {
            return like_categorydata.size();
        }
    }

   class ItemListViewAdapter extends RecyclerView.Adapter<ItemListViewAdapter.ViewHolder>{
        private int cateid;
        private ArrayList<CategoryDetialData> data = new ArrayList<CategoryDetialData>();
       public ItemListViewAdapter(int id){
           cateid=id;
           data = ((MainActivity)getActivity()).getCategoryDetaildata(cateid-1);
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
           public TextView title_unit;
           public ViewHolder(View itemView) {
               super(itemView);
               title_unit = (TextView) itemView.findViewById(R.id.title_unit);
           }
       }

       @Override
       public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           final Context context = getActivity();
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
          View view = inflater.inflate(R.layout.recyclerview_mystation, parent, false);

          return new ItemListViewAdapter.ViewHolder(view);
       }

       @Override
       public void onBindViewHolder(ViewHolder holder, int position) {

             PistolLogger.LOGD("data:" + data.get(position).getTitleEng());
             String title_unit_str = data.get(position).getTitleEng();
             holder.title_unit.setText(title_unit_str);
       }

       @Override
       public int getItemCount() {
           return data.size();
       }
   }
}
