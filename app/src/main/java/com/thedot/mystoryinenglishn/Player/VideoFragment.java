package com.thedot.mystoryinenglishn.Player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Setting.Preferences;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

import java.io.IOException;


public class VideoFragment extends Fragment {

    private View view;
    public VideoView mVideoPlayer;
    private TextView currentTime;
    private boolean threadstate;
    private static PlayerCardFragment mParent = null;
    private SeekBar videoseekbar;
    private videoSyncEventListenr videoSyncEventLister;
    private RelativeLayout videohidewhite;
    public MediaPlayer recordingfilePlayer;
    private int playfileId;

    public interface videoSyncEventListenr{
        void onSyncEvent(int num);
    }
    public void setVideoSyncEventListenr(videoSyncEventListenr eventListenr){
        videoSyncEventLister = eventListenr;
    }

    public static VideoFragment newInstance(PlayerCardFragment parent) {

        VideoFragment fragment = new VideoFragment();
        mParent = parent;

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

        recordingfilePlayer = new MediaPlayer();

        view = inflater.inflate(R.layout.video_view, container, false);
        currentTime = (TextView) view.findViewById(R.id.currentTime);
        final ToggleButton continuReplay =(ToggleButton) view.findViewById(R.id.btn_video_replay);
        threadstate=true;
        videoseekbar = view.findViewById(R.id.video_seekBar);
        videohidewhite = (RelativeLayout) view.findViewById((R.id.videoViewWhite));
        videohidewhite.setVisibility(View.VISIBLE);
        Preferences.setContinuReplay(getContext(),false);
        LinearLayout video_seekbar_area = (LinearLayout) view.findViewById(R.id.video_seekbar_area);


        video_seekbar_area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        continuReplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
                if(ischeck){
                    Preferences.setContinuReplay(getContext(),true);
                }else{
                    Preferences.setContinuReplay(getContext(),false);
                }
            }
        });
        videoseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int v=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                v=i;
                seekBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)){
                    if(mVideoPlayer.isPlaying()) mVideoPlayer.pause();
                    if(recordingfilePlayer.isPlaying()) recordingfilePlayer.pause();
                }
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)){
                    if(mVideoPlayer.isPlaying()) mVideoPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)){
                    int current = mVideoPlayer.getDuration() * v;
                    double n = (double)current / 95.0;
                    mVideoPlayer.seekTo((int)n);
                    mVideoPlayer.start();
                }
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)){
                    int current = recordingfilePlayer.getDuration() * v;
                    double n = (double)current / 95.0;
                    if(current>mVideoPlayer.getDuration()-5) {
                        mVideoPlayer.seekTo(mVideoPlayer.getDuration()-5);
                    }else{
                        mVideoPlayer.seekTo((int)n);
                    }
                    recordingfilePlayer.seekTo((int)n);
                    mVideoPlayer.start();
                    recordingfilePlayer.start();
                }

            }
        });

        playfileId = mParent.getPlayfileId();
        PistolLogger.LOGD("PLAY RECORDING FILE ID:" + Integer.toString(playfileId));
        setVideo();
        if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)) recordingFileStart();
        recordingfilePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    if(Preferences.getContinuReplay(getContext())){
                        mVideoPlayer.seekTo(0);
                        mVideoPlayer.start();
                        recordingfilePlayer.seekTo(0);
                        recordingfilePlayer.start();
                    }else{
                        while(true){
                            ++playfileId;
                            if(mParent.getPlayrecordingfile().getplaycheckedarr().get(playfileId)) break;
                        }
                        if(mParent.getPlayrecordingfile().next(playfileId)) {
                            recordingfilePlayer.reset();
                            recordingFileStart();
                            mVideoPlayer.seekTo(0);
                            mVideoPlayer.start();
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void recordingFileStart(){
        PistolLogger.LOGD("PLAYFIRL ID RECORDINFFile Start:" + Integer.toString(playfileId));
        mParent.setPlayfileInfoText(playfileId);
        try{
            recordingfilePlayer.setDataSource(mParent.getPlayrecordingfile().getPlayfilenamearr().get(playfileId));
            recordingfilePlayer.prepare();
            recordingfilePlayer.start();

            setCurrentTimeText();
            setDurationTimeText();
        }catch(IOException e){

        }

    }

    public void setVideo(){
        if(mVideoPlayer!=null)  {
            mVideoPlayer.stopPlayback();
            mVideoPlayer=null;
        }
        mVideoPlayer = (VideoView) view.findViewById(R.id.videoView);
        String mp4 = ((PlayActivity)getActivity()).getContentData().getVideoURL();
       // String videopath = "android.resource://" + getActivity().getPackageName()+ "/" + getResources().getIdentifier(mp4,"raw",getActivity().getPackageName());
        String videofilename = ((PlayActivity)getActivity()).getContentData().getVideoURL();
        String thePackageName = getActivity().getPackageName();
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filepath = root + "/Android/obb/"+thePackageName;
        String videopath = filepath + "/" + videofilename +".mp4";
        PistolLogger.LOGD("videourl : "+videopath);
        mVideoPlayer.setVideoURI(Uri.parse(videopath));
        mVideoPlayer.requestFocus();
        mVideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setCurrentTimeText();
                setDurationTimeText();
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)) {
                    mediaPlayer.setVolume(1.0f,1.0f);
                }
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)) {
                    mediaPlayer.setVolume(0.0f, 0.0f);
//                    recordingFileStart();
                }
                mVideoPlayer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videohidewhite.setVisibility(View.INVISIBLE);
                    }
                }, 500);
                mVideoPlayer.start();
                setSyncValue();
                setThread();
            }
        });

        mVideoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PistolLogger.LOGD("touch!!!!!!");
                if(mVideoPlayer.isPlaying()) {
                    mVideoPlayer.pause();
                    btnAnimation(R.drawable.btn_media_pause);
                    if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME) && recordingfilePlayer.isPlaying()) recordingfilePlayer.pause();
                }else{
                    mVideoPlayer.start();
                    btnAnimation(R.drawable.btn_media_paly);
                    if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME) && !recordingfilePlayer.isPlaying()) recordingfilePlayer.start();
                }
                return false;
            }
        });

        mVideoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //TODO 동영상 완료시 DB에 value Set
                int alexNo;
                //TODO CATEGORY id or content id
                videoSyncEventLister.onSyncEvent(0);
                if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)){
                    ((PlayActivity)getActivity()).getBaseApplication().insertTotalgradeAlex();
                    alexNo = ((PlayActivity)getActivity()).getBaseApplication().getValueAlexGrage(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId());
                    if(alexNo<0) {
                        ((PlayActivity)getActivity()).getBaseApplication().insertGradeinfo(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId(),1,0);
                    } else{
                        ++alexNo;
                        ((PlayActivity)getActivity()).getBaseApplication().updateGradeAlex(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId(), alexNo);
                    }
                    PistolLogger.LOGD(Integer.toString(alexNo)+ ":getValue" +", cateId:" + Integer.toString(((PlayActivity)getActivity()).getCateId()) + ", contentId:" + Integer.toString(((PlayActivity)getActivity()).getContentId()));
                    ((PlayActivity)getActivity()).FRAGMENT_ME_FIRST_ENTER_CHECK=true;

                    if(Preferences.getContinuReplay(getContext())){
                        mVideoPlayer.seekTo(0);
                        mVideoPlayer.start();
                    }else {
                        if (Preferences.getAlexAutoPlay(getActivity()) && ((PlayActivity) getActivity()).isStateUnitPlay())
                            ((PlayActivity) getActivity()).nextUnitPlay();
                    }
                }
            }
        });

    }

    public void mediaStateChange(){
        if(mParent.PLAY_MODE.equals(mParent.ALEX)) {
            if(mVideoPlayer!=null && mVideoPlayer.isPlaying()) {
                mVideoPlayer.pause();
            }
        }

        if(mParent.PLAY_MODE.equals(mParent.ME)) {
            if(mVideoPlayer!=null && mVideoPlayer.isPlaying()) {
                    mVideoPlayer.pause();
            }
            if (recordingfilePlayer!=null && recordingfilePlayer.isPlaying()) {
                recordingfilePlayer.pause();
            }
        }
    }
    public void showVideoWhiteBackground(){
        if(videoseekbar!=null) {
            if (videohidewhite.getVisibility() == View.INVISIBLE) {
                videohidewhite.setVisibility(View.VISIBLE);
            }
        }
    }
    public void btnAnimation(int resource_id){
        final ImageView imageview = (ImageView) view.findViewById(R.id.imageview_btn);
        imageview.setVisibility(View.VISIBLE);
        imageview.setImageResource(resource_id);
        Animation btn_fade_out = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeout_exit);
        imageview.setAnimation(btn_fade_out);
        btn_fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public int[] sync;
    public void setSyncValue(){
        String syncc = ((PlayActivity)getActivity()).getContentData().getSyncInfo();
        String[] synstr = syncc.split(",");
        sync = new int[synstr.length];
        for(int i=0;i<synstr.length;++i) {
            sync[i] = Integer.parseInt(synstr[i]);
        }
    }

    public void setThread(){
        final Handler handler = new Handler();
        final Thread videoviewTimeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(threadstate){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            double d = 0.0;
                            PistolLogger.LOGD("Thread Handler!!!!!");
                            if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)) {
                                currentTime.setText(timeToString(mVideoPlayer.getCurrentPosition()));
                                d = ((double) mVideoPlayer.getCurrentPosition() / (double) mVideoPlayer.getDuration()) * 100.0;
                            }
                            if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)) {
                                currentTime.setText(timeToString(recordingfilePlayer.getCurrentPosition()));
                                d = ((double) recordingfilePlayer.getCurrentPosition() / (double) recordingfilePlayer.getDuration()) * 100.0;
                            }

                           if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)) {
                                int current_pos = mVideoPlayer.getCurrentPosition()/1000;
                                for (int i = 0; i < sync.length - 1; ++i) {
                                    if (current_pos >= sync[i] && current_pos < sync[i + 1]) {
                                        int v = i;
                                        PistolLogger.LOGD("ALEX MODE [V]:" + Integer.toString(v));
                                        if (threadstate) videoSyncEventLister.onSyncEvent(v);
                                    }
                                }
                            }

                            videoseekbar.setProgress((int)d);
                        }
                    });
                    try {
                        // 1초 씩 딜레이 부여
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        });
        videoviewTimeThread.start();
    }

    public String timeToString(int time)
    {
        int nTotal=(int)(time /1000.0D);
        int nHour = nTotal/3600;
        int nMin = (nTotal/60)%60;
        int nSec = nTotal%60;

        if (nHour > 0){
            return String.format("%02d:%02d:%02d", nHour,nMin,nSec);
        }
        return String.format("%02d:%02d", nMin,nSec);
    }

    public void setCurrentTimeText(){
        int duration = 0 ;
        if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)) duration = mVideoPlayer.getCurrentPosition();
        if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)) duration = recordingfilePlayer.getCurrentPosition();
        currentTime.setText(timeToString(duration));
    }

    public void setDurationTimeText(){
        int duration = 0 ;
        if(mParent.PLAY_MODE.equals(PlayerCardFragment.ALEX)) duration = mVideoPlayer.getDuration();
        if(mParent.PLAY_MODE.equals(PlayerCardFragment.ME)) duration = recordingfilePlayer.getDuration();
        TextView durationTime = (TextView) view.findViewById(R.id.totalTime);
        durationTime.setText(timeToString(duration));
    }

    @Override
    public void onStart() {
        super.onStart();
        PistolLogger.LOGD("Video Fragment onStart");

    }

    public int currenttime=0;
    @Override
    public void onResume() {
        super.onResume();
        if(mParent.PLAY_MODE.equals(mParent.ALEX)) {
            mVideoPlayer.seekTo(currenttime);
            mVideoPlayer.start();
        }
        if(mParent.PLAY_MODE.equals(mParent.ME)) {
            if (currenttime > mVideoPlayer.getDuration() - 5) {
                mVideoPlayer.seekTo(mVideoPlayer.getDuration() - 5);
            } else {
                mVideoPlayer.seekTo(currenttime);
            }
            mVideoPlayer.start();
            recordingfilePlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mParent.PLAY_MODE.equals(mParent.ALEX)) currenttime = mVideoPlayer.getCurrentPosition();
        if(mParent.PLAY_MODE.equals(mParent.ME)) currenttime = recordingfilePlayer.getCurrentPosition();
        if(mVideoPlayer.isPlaying()) mVideoPlayer.pause();
        if(recordingfilePlayer.isPlaying())recordingfilePlayer.pause();
        PistolLogger.LOGD("Video Fragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        PistolLogger.LOGD("Video Fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PistolLogger.LOGD("Video Fragment onDestroyView");
        threadstate=false;
        videoSyncEventLister.onSyncEvent(0);
        if(videohidewhite.getVisibility()==View.INVISIBLE) {
            videohidewhite.setVisibility(View.VISIBLE);
        }
        if(view !=null){
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if( null != parentViewGroup ) {
                parentViewGroup.removeView( view );
            }
        }
        if(recordingfilePlayer!=null){
            recordingfilePlayer.release();
        }

        mVideoPlayer.stopPlayback();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PistolLogger.LOGD("onDestroy");
    }
}
