package com.thedot.mystoryinenglishn.Player;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;
import com.thedot.mystoryinenglishn.Utils.Utility;

import java.io.File;
import java.util.ArrayList;

public class PlayerCardFragment extends Fragment {

    private View view;
    private int cateId;
    private int contentId;
    private RecordingRecyclerViewAdapter recordinglistviewadapter;
    private RelativeLayout mediafragmentcontainer_video;
    private TextView recordingfilename;
    private TextView content_kor;
    private TextView content_eng;
    private TextView contenttextWord;
    private RecyclerView recordingfilelistview;
    private RelativeLayout videocoverimg;
    private RelativeLayout content_text_wrap;
    private ImageButton btnMe;
    private ToggleButton btnAlex;
    public static final String ALEX = "alex";
    public static final String ME = "me";
    public String PLAY_MODE=ALEX;
    private PhoneStateListener phonestateListener;
    private float e1;
    private float e2;
    public static PlayerCardFragment newInstance() {
        PlayerCardFragment fragment = new PlayerCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        cateId = ((PlayActivity)getActivity()).getCateId();
        contentId = ((PlayActivity)getActivity()).getContentId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        phonestateListener = new PhoneStateListener(){
            @Override
            //레코딩 기능
            public void onCallStateChanged(int state, String incomingNumber) {
                if(state== TelephonyManager.CALL_STATE_IDLE){
                    PistolLogger.LOGD("CALL_STATE_IDLE 전화 끊었을때: phon number;" + incomingNumber);
//                    if(videofragment!=null){
//                        videofragment.mediaStateChange();
//                    }
                }else if(state== TelephonyManager.CALL_STATE_RINGING){
                    PistolLogger.LOGD("CALL_STATE_RINGING 전화벨 : phon number;" + incomingNumber);
                    if(recordfragment!=null){
                        recordfragment.recordStateChange();
                    }
                    if(videofragment!=null){
                        videofragment.mediaStateChange();
                    }
                }else if(state== TelephonyManager.CALL_STATE_OFFHOOK){
                    PistolLogger.LOGD("CALL_STATE_OFFHOOK 전화받았을때: phon number;" + incomingNumber);
                    if(recordfragment!=null){
                      //  recordfragment.recordStateChange();
                    }
                }
            }
        };

        TelephonyManager telephonyManager  = (TelephonyManager)((PlayActivity)getActivity()).getSystemService(((PlayActivity)getActivity()).TELEPHONY_SERVICE);
        telephonyManager.listen(phonestateListener, PhoneStateListener.LISTEN_CALL_STATE);

        if(view == null) {
            container.removeAllViews();
            try {
                view = inflater.inflate(R.layout.fragment_player_card, container, false);
            } catch (InflateException e) {
                e.printStackTrace();
            }
        }else{
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (null != parentViewGroup) {
                parentViewGroup.removeView(view);
            }
        }
        content_text_wrap = (RelativeLayout) view.findViewById(R.id.content_text_wrap);
        videocoverimg = (RelativeLayout) view.findViewById(R.id.video_bg_img);
        mediafragmentcontainer_video = (RelativeLayout) view.findViewById(R.id.mediafragmentcontainer_video);
        recordingfilelistview = (RecyclerView) view.findViewById(R.id.recordingfilelistview);
        contenttextWord = (TextView) view.findViewById(R.id.content_text_etc);
        recordingfilename=(TextView)view.findViewById(R.id.recording_play_filename);
        btnMe = (ImageButton) view.findViewById(R.id.button_me);
        btnAlex = (ToggleButton) view.findViewById(R.id.button_alex);
        TextView unit_content_subject = (TextView) view.findViewById(R.id.unit_content_subject);
        TextView player_unit_number = (TextView) view.findViewById(R.id.player_unit_number);

        ImageButton btn_back =(ImageButton) view.findViewById(R.id.btn_player_back);
        final ImageButton btnVideoPlay = (ImageButton) view.findViewById(R.id.btn_video_play);
        final ToggleButton word = (ToggleButton) view.findViewById(R.id.button_word);
        final ToggleButton qoutor = (ToggleButton) view.findViewById(R.id.button_qoutor);
        final ToggleButton btnRecorder = (ToggleButton) view.findViewById(R.id.button_mic);

        final Animation btn_fade_out = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout_exit);
        final Animation btn_fade_in = AnimationUtils.loadAnimation(getActivity(),R.anim.fadein);
        final Animation list_down = AnimationUtils.loadAnimation(getActivity(),R.anim.list_down);
        final Animation list_up = AnimationUtils.loadAnimation(getActivity(),R.anim.list_up);

        recordinglistviewadapter = new RecordingRecyclerViewAdapter();
        recordingfilelistview.setAdapter(recordinglistviewadapter);
        recordingfilelistview.setLayoutManager(new LinearLayoutManager(getContext()));


        String conEngStr = ((PlayActivity)getActivity()).getContnetTextValue(((PlayActivity)getActivity()).getContentData().getEngContentStr(),0);
        String conKorStr = ((PlayActivity)getActivity()).getContnetTextValue(((PlayActivity)getActivity()).getContentData().getKorContentStr(),0);
        String unit_content_subject_str=((PlayActivity)getActivity()).getContentData().getContent_subject();
        String conWord = ((PlayActivity)getActivity()).getContentData().getWord();
        content_eng = (TextView) view.findViewById(R.id.content_text_eng);
        content_kor = (TextView) view.findViewById(R.id.content_text_kor);
        content_eng.setMovementMethod(new ScrollingMovementMethod());
        content_kor.setMovementMethod(new ScrollingMovementMethod());
        contenttextWord.setMovementMethod(new ScrollingMovementMethod());

        unit_content_subject.setText(unit_content_subject_str);
        content_eng.setText(Html.fromHtml(conEngStr));
        content_kor.setText(Html.fromHtml(conKorStr));
        contenttextWord.setText(Html.fromHtml(conWord));

        String cate_name = ((PlayActivity)getActivity()).getCateName();
        String unit_total_num_str =  Integer.toString(((PlayActivity)getActivity()).getContentDataUrl().size());
        String player_unit_number_str = Integer.toString(((PlayActivity)getActivity()).getContentData().getId());
        player_unit_number.setText(Html.fromHtml(cate_name + "&nbsp;&nbsp;"+player_unit_number_str+"<font color='#9B9B9B'> / "+ unit_total_num_str+"</font>"));

        content_eng.setVisibility(View.VISIBLE);
        content_kor.setVisibility(View.INVISIBLE);
        contenttextWord.setVisibility(View.INVISIBLE);
        contentTextViewInit();
        setRecordingFiletotalnum(false);

        btnVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVideoFragment();
                ((PlayActivity)getActivity()).VIDEO_FIRST_CHECK=false;
                mediafragmentcontainer_video.setVisibility(View.VISIBLE);
                btnVideoPlay.setVisibility(View.GONE);
            }
        });

        if(!((PlayActivity) getActivity()).VIDEO_FIRST_CHECK) btnVideoPlay.callOnClick();
        btnRecorder.setChecked(false);
        btnRecorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
                PistolLogger.LOGD("btnRecorder !!!!");
                if(ischeck==true){
                    if(videofragment!=null){
                        removeVideoFragment();
                    }
                    addRecordFragment();
                    mediafragmentcontainer_video.setVisibility(View.VISIBLE);
                    if(PLAY_MODE.equals(ME) && recordingfilelistview.getVisibility()==View.VISIBLE) {
                            recordingfilelistview.startAnimation(list_down);
                            recordingfilelistview.setVisibility(View.GONE);
                            content_text_wrap.setVisibility(View.VISIBLE);
                    }
                        btnAlex.setEnabled(true);
                        btnAlex.setChecked(true);
                        btnMe.setEnabled(true);
                        word.setEnabled(true);
                        qoutor.setEnabled(true);

                }else{
                    btnAlex.callOnClick();
                    removeRecordFragment();
                    setRecordToggleOff();
                }
            }
        });

        btnMe.setImageDrawable(getAvataaDrawble());
        btnAlex.setEnabled(false);
        btnAlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PLAY_MODE=ALEX;
                PistolLogger.LOGD("PLAY MODE:" + PLAY_MODE);
                recordingfilename.setVisibility(View.INVISIBLE);

                btnAlex.setEnabled(false);
                btnAlex.setChecked(false);
                btnMe.setEnabled(true);
                word.setEnabled(true);
                qoutor.setEnabled(true);

                if(videofragment!=null) removeVideoFragment();
                if(recordfragment!=null) {
                    removeRecordFragment();
                    setRecordToggleOff();
                }
                btnVideoPlay.startAnimation(btn_fade_in);
                btnVideoPlay.setVisibility(View.VISIBLE);

                videocoverimg.setVisibility(View.VISIBLE);
                showVidoWhiteBackground();
                if(View.VISIBLE == recordingfilelistview.getVisibility()) {
                    recordingfilelistview.startAnimation(list_down);
                    recordingfilelistview.setVisibility(View.GONE);
                    content_text_wrap.setVisibility(View.VISIBLE);
                }
            }
        });
        btnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExgistRecordingPlayfile()) {
                    PLAY_MODE = ME;
                    if(View.GONE == recordingfilelistview.getVisibility()) {
                        PistolLogger.LOGD("PLAY MODE:" + PLAY_MODE);
                        btnAlex.setEnabled(true);
                        btnAlex.setChecked(true);
                        word.setEnabled(false);
                        qoutor.setEnabled(false);

                        if(videofragment!=null) removeVideoFragment();
                        if(recordfragment!=null) {
                            removeRecordFragment();
                            setRecordToggleOff();
                        }

                        recordingfilelistview.startAnimation(list_up);
                        recordingfilelistview.setVisibility(View.VISIBLE);
//                        content_text_wrap.setVisibility(View.INVISIBLE);
                        if (btnVideoPlay.getVisibility() == View.VISIBLE) {
                            btnVideoPlay.startAnimation(btn_fade_out);
                            btnVideoPlay.setVisibility(View.GONE);
                        }
                        videocoverimg.setVisibility(View.VISIBLE);
                    }else if(View.VISIBLE == recordingfilelistview.getVisibility()){
                        btnAlex.callOnClick();
                    }
                }else{
                    Toast.makeText(getContext(),R.string.playcardfragment_norecordingfile,Toast.LENGTH_SHORT).show();
                }
            }
        });

        qoutor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    contenttextWord.setVisibility(View.VISIBLE);
                }else{
                    contenttextWord.setVisibility(View.INVISIBLE);
                }
            }
        });

        word.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
                if(ischeck==true){
                    if(contenttextWord.getVisibility()==View.VISIBLE){
                        contenttextWord.setVisibility(View.INVISIBLE);
                        qoutor.setChecked(false);
                    }
                    content_eng.setVisibility(View.INVISIBLE);
                    content_kor.setVisibility(View.VISIBLE);
                }else{
                    if(contenttextWord.getVisibility()==View.VISIBLE){
                        contenttextWord.setVisibility(View.INVISIBLE);
                        qoutor.setChecked(false);
                    }
                    content_eng.setVisibility(View.VISIBLE);
                    content_kor.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PlayActivity)getActivity()).onBackPressed();
            }
        });

        return view;
    }

    public void contentTextViewInit(){
        if(PLAY_MODE.equals(ALEX)) {
            recordingfilename.setVisibility(View.INVISIBLE);
            recordingfilelistview.setVisibility(View.GONE);
        }
        if(PLAY_MODE.equals(ME)) {
            recordingfilename.setVisibility(View.VISIBLE);
            recordingfilelistview.setVisibility(View.VISIBLE);
        }
    }

    public void showVidoWhiteBackground(){
        if(videofragment!=null) videofragment.showVideoWhiteBackground();
    }
    public Drawable getAvataaDrawble(){
        Drawable avata_img = null;
        if(Preferences.getMyavatar(getActivity())==0){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar1);
        }else if(Preferences.getMyavatar(getActivity())==1){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar2);
        }else if(Preferences.getMyavatar(getActivity())==2) {
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar3);
        }else if(Preferences.getMyavatar(getActivity())==3) {
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar4);
        }else if(Preferences.getMyavatar(getActivity())==4){
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar5);
        }else if(Preferences.getMyavatar(getActivity())==5) {
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar6);
        }else if(Preferences.getMyavatar(getActivity())==6) {
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar7);
        }else if(Preferences.getMyavatar(getActivity())==7) {
            avata_img = ContextCompat.getDrawable(getActivity(), R.drawable.avataaar8);
        }
        return avata_img;
    }
    public boolean isExgistRecordingPlayfile(){
        if(getRecordingFiletotalnum()>0){
            return true;
        }else{
            return false;
        }
    }
    private RecordFragment recordfragment=null;
    private VideoFragment videofragment=null;
    public void addRecordFragment(){
        if(recordfragment!=null) recordfragment = null;
        videocoverimg.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        recordfragment = RecordFragment.newInstance();
        fragmentTrasaction.setCustomAnimations(R.anim.start_up_enter,R.anim.start_down_exit);
        fragmentTrasaction.add(R.id.mediafragmentcontainer_recorder,recordfragment,"recordfragment");
        fragmentTrasaction.commit();

        RecordFragment.CloseEventListner closeListenr = new RecordFragment.CloseEventListner() {
            @Override
            public void onClickEvent() {
                removeRecordFragment();
                setRecordToggleOff();
            }
        };
        recordfragment.setCloseEventListenr(closeListenr);

        RecordFragment.SaveRecordingFileEventListenr saveRecordingFileEventListenr = new RecordFragment.SaveRecordingFileEventListenr() {
            @Override
            public void onClickEvnet() {
                setRecordingFiletotalnum(true);
                updateRecordingFileListView();
//                btnMe.setEnabled(true);
//                btnAlex.setEnabled(true);
                btnMe.callOnClick();
//                btnAlex.setEnabled(true);
//                btnAlex.setChecked(true);

            }
        };

        recordfragment.setSaveRecordingFileEventListner(saveRecordingFileEventListenr);
    }

    public void addVideoFragment(){
        videocoverimg.setVisibility(View.INVISIBLE);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        videofragment = VideoFragment.newInstance(this);
        fragmentTrasaction.add(R.id.mediafragmentcontainer_video,videofragment,"videofragment");
        videofragment.setVideoSyncEventListenr(new VideoFragment.videoSyncEventListenr() {
            @Override
            public void onSyncEvent(int num) {
                String conEngStr = ((PlayActivity)getActivity()).getContnetTextValue(((PlayActivity)getActivity()).getContentData().getEngContentStr(),num);
                content_eng.setText(Html.fromHtml(conEngStr));
                String conKorStr = ((PlayActivity)getActivity()).getContnetTextValue(((PlayActivity)getActivity()).getContentData().getKorContentStr(),num);
                content_kor.setText(Html.fromHtml(conKorStr));
            }
        });
        fragmentTrasaction.commit();
    }

    public void removeRecordFragment(){
        PistolLogger.LOGD("removeRecordFragment!!!!");
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        fragmentTrasaction.setCustomAnimations(R.anim.start_up_enter,R.anim.start_down_exit);
        fragmentTrasaction.remove(recordfragment);
        fragmentTrasaction.commit();
    }

    public void removeVideoFragment(){
        showVidoWhiteBackground();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTrasaction = fragmentManager.beginTransaction();
        fragmentTrasaction.remove(videofragment);
        fragmentTrasaction.commit();
        videofragment=null;
    }

    private int playfile_id = 0;
    private PlayRecordingFile playrecordingfile = null;

    public PlayRecordingFile getPlayrecordingfile(){
        return playrecordingfile;
    }
    public int getPlayfileId(){
     return playfile_id;
    }
    public void setPlayfileId(int id){ playfile_id = id;}
    public void setPlayfileInfoText(int _id){
        recordingfilename.setText(playrecordingfile.getContentnamearr().get(_id));
        recordingfilename.setVisibility(View.VISIBLE);
        content_eng.setVisibility(View.VISIBLE);
    }

    public Intent getShareIntent(String filepath) {
        File file = new File(filepath);
        final Intent shareIntent = new Intent(Intent.ACTION_SEND );
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("application/*");

        startActivity(Intent.createChooser(shareIntent, "공유합니다."));
        return shareIntent;
    }

    public RecordingRecyclerViewAdapter getRecyclerViewAapter(){
        return recordinglistviewadapter;
    }

    private String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(path);
        if(Utility.isFileExist(file)) {
            try {
                retriever.setDataSource(path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int timeInmillisec = Integer.parseInt(time);

            return Utility.timeToString(timeInmillisec);
        }else{
            return "file err";
        }
    }

    public void setRecordingFiletotalnum(boolean anim){
        TextView textview = (TextView) view.findViewById(R.id.recordingfiletotalnum);
        textview.setText(Integer.toString(getRecordingFiletotalnum()));
        if(anim) {
            Animation ani = AnimationUtils.loadAnimation((PlayActivity) getActivity(), R.anim.heart_scale);
            textview.startAnimation(ani);
        }
    }
    public void updateRecordingFileListView(){
        recordinglistviewadapter.setRecordingfileItem();
        recordinglistviewadapter.setPlayrecordingData();
        recordinglistviewadapter.notifyDataSetChanged();
    }
    public int getRecordingFiletotalnum(){
        ArrayList<RecordingFile> recordingfilelist = ((PlayActivity)getActivity()).getBaseApplication().getValue(cateId,contentId);
        return recordingfilelist.size();
    }

    public void setRecordToggleOff(){
        ToggleButton recorder = (ToggleButton) view.findViewById(R.id.button_mic);
        recorder.setChecked(false);

        if(PLAY_MODE.equals(ALEX)){
            btnMe.setEnabled(true);
        }
        if(PLAY_MODE.equals(ME)){
            btnAlex.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PistolLogger.LOGD("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PistolLogger.LOGD("onDestroy");
    }
    @Override
    public void onDetach() {
        super.onDetach();
//        if (recordingfilePlayer != null) recordingfilePlayer.release();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public class RecordingRecyclerViewAdapter extends RecyclerView.Adapter<RecordingRecyclerViewAdapter.ViewHolder>{
        private ArrayList<RecordingFile> recordingfileItem = new ArrayList<>();
        private RecordingRecyclerViewAdapter.ViewHolder mHolder;

        public RecordingRecyclerViewAdapter(){
            setRecordingfileItem();
            setPlayrecordingData();
        }

        public void setRecordingfileItem(){
            recordingfileItem = ((PlayActivity)getActivity()).getBaseApplication().getValue(cateId,contentId);
            PistolLogger.LOGD("recordingfileItem size:" + Integer.toString(recordingfileItem.size()));
        }
        public ArrayList<RecordingFile> getRecordingfileItem(){
            return recordingfileItem;
        }
        public class ViewHolder extends RecyclerView.ViewHolder{

            private TextView filename;
            private TextView fileduration;
            private TextView filedata;
            private ImageButton btnFiledelete;
            private ImageButton btnfileshare;
            private LinearLayout delcontent_wrap;
            private Button file_del_cancel;
            private Button file_del_delete;
            private TextView file_del_text;
            private ToggleButton btn_playcheck;
            private View view;

            public ViewHolder(View itemView){
                super(itemView);
                view = itemView;
                filename = (TextView) itemView.findViewById(R.id.recordingfilename);
                fileduration = (TextView) itemView.findViewById(R.id.recordingfileduration);
                filedata = (TextView) itemView.findViewById(R.id.recordingfiledata);
                btnFiledelete = (ImageButton) itemView.findViewById(R.id.btnrecodingdel);
                btnfileshare=(ImageButton) itemView.findViewById(R.id.btnfileshare);
                delcontent_wrap = (LinearLayout) itemView.findViewById(R.id.recordingfile_wrap_del);
                file_del_delete = (Button) itemView.findViewById(R.id.recordingfile_delete);
                file_del_cancel = (Button) itemView.findViewById(R.id.recordingfile_cancel);
                file_del_text = (TextView) itemView.findViewById(R.id.recordingfile_text);
                btn_playcheck = (ToggleButton) itemView.findViewById(R.id.recordingfile_btncheck);
            }
        }

        public void onDialogOkEvent(int position){
            String _name = recordingfileItem.get(position).getContentname();
            PistolLogger.LOGD("POSITION [" + Integer.toString(position) + "]" + ", NAME : " +_name);
            File filepath = new File(((PlayActivity)getActivity()).getBaseApplication().getFilePathValue(_name));
            Utility.deleteFile(filepath);

            ((PlayActivity)getActivity()).getBaseApplication().deleteRecordingfileByContentname(_name);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            setRecordingfileItem();
            setRecordingFiletotalnum(true);
        }

        public void setPlayrecordingData(){
            PistolLogger.LOGD("set Playerecording data!!!!");
            final ArrayList<String> playingfilename = new ArrayList<>();
            final ArrayList<String> contentname = new ArrayList<>();
            final ArrayList<Boolean> playchecked = new ArrayList<>();
            playrecordingfile = new PlayRecordingFile();
            for (RecordingFile i : recordingfileItem) {
                playingfilename.add(i.getContentPath());
                contentname.add(i.getContentname());
                if(Preferences.getMeAutoPlay(getActivity())){
                    playchecked.add(true);
                }else{
                    playchecked.add(false);
                }
            }

            playrecordingfile.setContentnamearr(contentname);
            playrecordingfile.setPlayfilenamearr(playingfilename);
            playrecordingfile.setPlaycheckedarr(playchecked);
        }
        @Override
        public RecordingRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = getActivity();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.recordingfile_listview, parent,false);

            PistolLogger.LOGD("onCreateViewHolder!!!!");
            return new RecordingRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecordingRecyclerViewAdapter.ViewHolder holder, int position) {
            mHolder = holder;
            final int pos = position;
            final String _name = getRecordingfileItem().get(pos).getContentname();
            holder.filename.setText(getRecordingfileItem().get(pos).getContentname());
            holder.fileduration.setText(getPlayTime(((PlayActivity)getActivity()).getBaseApplication().getFilePathValue(getRecordingfileItem().get(pos).getContentname())));
            holder.filedata.setText(getRecordingfileItem().get(pos).getContentdata());

            holder.delcontent_wrap.setVisibility(View.GONE);

            holder.btn_playcheck.setOnCheckedChangeListener(null);
            holder.btn_playcheck.setChecked(getPlayrecordingfile().getplaycheckedarr().get(pos));
            //PistolLogger.LOGD("position: " + Integer.toString(pos));
            holder.btn_playcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                    if(check){
                        playrecordingfile.getplaycheckedarr().set(pos,true);
                        PistolLogger.LOGD("onCheckedChanged[true] : " + Integer.toString(pos));
                    }
                    else{
                        playrecordingfile.getplaycheckedarr().set(pos,false);
                        PistolLogger.LOGD("onCheckedChanged[false] : " + Integer.toString(pos));
                    }
                }
            });

            holder.btnFiledelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showDeleteRecordingFileAlert(pos);
                    Animation ani = AnimationUtils.loadAnimation(getContext(),R.anim.start_up_enter);
                    holder.delcontent_wrap.startAnimation(ani);
                    holder.delcontent_wrap.setVisibility(View.VISIBLE);
                    holder.file_del_text.setText(_name+"를 삭제하겠습니까?");
                }
            });

            holder.btnfileshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PistolLogger.LOGD(recordingfileItem.get(pos).getContentPath());
                    getShareIntent(recordingfileItem.get(pos).getContentPath());
                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PLAY_MODE=ME;
                    if(holder.btn_playcheck.isChecked()) {
                        addVideoFragment();
                        recordingfilelistview.setVisibility(View.GONE);
                        mediafragmentcontainer_video.setVisibility(View.VISIBLE);
                        //content_text_wrap.setVisibility(View.VISIBLE);
                        setPlayfileId(pos); //set playId
                        setPlayfileInfoText(pos);
                        PistolLogger.LOGD("CLICK FILE POSITION : "+Integer.toString(pos));
                        btnAlex.setEnabled(true);
                        btnAlex.setChecked(true);
                        btnMe.setEnabled(true);
                    }else{
                        PistolLogger.LOGD("파일이 선택되어있지 않습니다.");
                    }
                }
            });

            holder.file_del_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation ani = AnimationUtils.loadAnimation(getContext(),R.anim.start_down_exit);
                    holder.delcontent_wrap.startAnimation(ani);
                    holder.delcontent_wrap.setVisibility(View.GONE);
                }
            });

            holder.file_del_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PistolLogger.LOGD("POSITION [" + Integer.toString(pos) + "]" + ", NAME : " +_name);
                    File filepath = new File(((PlayActivity)getActivity()).getBaseApplication().getFilePathValue(_name));
                    Utility.deleteFile(filepath);

                    ((PlayActivity)getActivity()).getBaseApplication().deleteRecordingfileByContentname(_name);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                    setRecordingfileItem();
                    setRecordingFiletotalnum(false);

                    playrecordingfile.getplaycheckedarr().remove(pos);
                    playrecordingfile.getContentnamearr().remove(pos);
                    playrecordingfile.getPlayfilenamearr().remove(pos);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recordingfileItem.size();
        }
    }

}