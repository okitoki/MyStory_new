package com.thedot.mystoryinenglishn.Player;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.thedot.mystoryinenglishn.R;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;
import com.thedot.mystoryinenglishn.Utils.PlayerAlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RecordFragment extends Fragment {
    public interface CloseEventListner{
        void onClickEvent();
    }
    public interface SaveRecordingFileEventListenr{
        void onClickEvnet();
    }

    private MediaRecorder myRecorder;
    private CloseEventListner closeEventListner=null;
    private SaveRecordingFileEventListenr saveRecordingFileEventListner=null;
    private View view;
    private TextView recordingTime;
    private ToggleButton btnRecording;
    private ToggleButton btnRecordingfilePlay;
    private Button btnSaveRecordingfile;
    private ImageButton btnClose;
    public MediaPlayer recordingfilePlayer=null;

    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    public void setCloseEventListenr(CloseEventListner listner){
        closeEventListner = listner;
    }
    public void setSaveRecordingFileEventListner(SaveRecordingFileEventListenr listner){
        saveRecordingFileEventListner = listner;
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

        if(view == null) {
            container.removeAllViews();
            try {
                view = inflater.inflate(R.layout.recorder_view, container, false);
            } catch (InflateException e) {
                e.printStackTrace();
            }
        }else{
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (null != parentViewGroup) {
                parentViewGroup.removeView(view);
            }
        }

        btnClose = (ImageButton) view.findViewById(R.id.btnRecordWindowClose);
        btnSaveRecordingfile= (Button) view.findViewById(R.id.btnRecordDisk);
        btnRecordingfilePlay = (ToggleButton) view.findViewById(R.id.btnRecordPlay);
        recordingTime = (TextView) view.findViewById(R.id.txtRecordingTime);
        btnRecording = (ToggleButton) view.findViewById(R.id.btnRecordRcording);

        btnRecording.setEnabled(true);
        btnRecording.getBackground().setAlpha(255);

        LinearLayout recordWindow=(LinearLayout) view.findViewById(R.id.recordWindow) ;

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeEventListner.onClickEvent();
                deletTempRecordFile();
                btnRecording.setChecked(false);
                recordTimeTextViewInit();
                if(recordingfilePlayer!=null) recordingfilePlayer.stop();
            }
        });
        recordWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClose.callOnClick();
            }
        });
        btnRecording.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    recorder();
                    startRecordTimer();
                    btnRecordingfilePlay.setEnabled(false);
                    btnRecordingfilePlay.getBackground().setAlpha(100);
                    btnSaveRecordingfile.setEnabled(false);
                    btnSaveRecordingfile.getBackground().setAlpha(100);
                }else{
                    recoderState=false;
                    if(myRecorder!=null) {
                        try {
                            myRecorder.stop();
                            startTempFileMerge();
                            btnRecordingfilePlay.setEnabled(true);
                            btnRecordingfilePlay.getBackground().setAlpha(255);
                            btnSaveRecordingfile.setEnabled(true);
                            btnSaveRecordingfile.getBackground().setAlpha(255);
                        }catch (RuntimeException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btnRecordingfilePlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    playPlayRecodeFile();
                    btnSaveRecordingfile.setEnabled(false);
                    btnSaveRecordingfile.getBackground().setAlpha(100);
                    btnRecording.setEnabled(false);
                    btnRecording.getBackground().setAlpha(100);
                }else{
                    recordingfilePlayer.pause();
                    btnSaveRecordingfile.setEnabled(true);
                    btnSaveRecordingfile.getBackground().setAlpha(255);
                    btnRecording.setEnabled(true);
                    btnRecording.getBackground().setAlpha(255);
                }
            }
        });

        btnSaveRecordingfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showSaveRecordingFileAlert();
            }
        });

        btnRecordingfilePlay.setEnabled(false);
        btnRecordingfilePlay.getBackground().setAlpha(100);
        btnSaveRecordingfile.setEnabled(false);
        btnSaveRecordingfile.getBackground().setAlpha(100);
        setLayoutRecordingNameText();
        recordTimeTextViewInit();

        myRecorder = new MediaRecorder();

        return view;
    }
    public void recordStateChange(){
        if(btnRecording.isChecked()) {
            btnRecording.setChecked(false);
        }
    }
    public void showSaveRecordingFileAlert(){
        String subject = ((PlayActivity)getActivity()).getContentData().getContent_subject();
        String record_file_name = getSaveRecordingFileName();
        final PlayerAlertDialog newDialogFragment = PlayerAlertDialog.newInstance(
                R.string.recording_save,subject,record_file_name);

        FragmentManager fragmentManager = getChildFragmentManager();
        newDialogFragment.show(fragmentManager,getTag());
        PlayerAlertDialog.DialogOkEventListener okListener = new PlayerAlertDialog.DialogOkEventListener() {
            @Override
            public void onOkEvent() {

                    ((PlayActivity)getActivity()).getBaseApplication().insertTotalgradeMe();
                    updateRecordGrageValue();
                    changeFinalRecordFileName();
                    ((PlayActivity)getActivity()).getBaseApplication().insertRecordinginfo(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId(), getSaveRecordingFileName(), getCurrentData(), getRecordingFileName());
                    //deletTempRecordFile();

                    recordTimeTextViewInit();
                    if(m_timer!=null) m_timer.cancel();

                    ArrayList<RecordingFile> recordingfilelist = ((PlayActivity)getActivity()).getBaseApplication().getValue(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId());
                    setLayoutRecordingNameText();
                    saveRecordingFileEventListner.onClickEvnet();

                    btnSaveRecordingfile.setEnabled(false);
                    btnSaveRecordingfile.getBackground().setAlpha(100);
                    btnRecordingfilePlay.setEnabled(false);
                    btnRecordingfilePlay.getBackground().setAlpha(100);

                    Toast.makeText(getContext(),R.string.record_file_save,Toast.LENGTH_SHORT).show();

                    btnClose.callOnClick();
                    newDialogFragment.dismiss();
            }
        };
        newDialogFragment.setOkDialogEventListner(okListener);

        PlayerAlertDialog.DialogcancelEventListener cancelListener = new PlayerAlertDialog.DialogcancelEventListener() {
            @Override
            public void onCancelEvent() {

                deletTempRecordFile();
                recordTimeTextViewInit();

                btnSaveRecordingfile.setEnabled(false);
                btnSaveRecordingfile.getBackground().setAlpha(100);
                btnRecordingfilePlay.setEnabled(false);
                btnRecordingfilePlay.getBackground().setAlpha(100);

                Toast.makeText(getContext(),R.string.record_file_del,Toast.LENGTH_SHORT).show();
                newDialogFragment.dismiss();
            }
        };
        newDialogFragment.setCanceleventListener(cancelListener);
    }

    public void updateRecordGrageValue(){
        int megragevalue = ((PlayActivity)getActivity()).getBaseApplication().getValueMeGrage(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId());
        PistolLogger.LOGD("me grage value : " + Integer.toString(megragevalue));
        if(megragevalue<0){
            ((PlayActivity)getActivity()).getBaseApplication().insertGradeinfo(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId(),0,1);
        }else{
            ++megragevalue;
            ((PlayActivity)getActivity()).getBaseApplication().updateGradeMe(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId(),megragevalue);
        }
    }
    public void updateRecordTotalGrageValue(){
        int totalrecordgrage = ((PlayActivity)getActivity()).getBaseApplication().getValueMeTotalGrage();
        PistolLogger.LOGD("me total grage value : " + Integer.toString(totalrecordgrage));
        if( totalrecordgrage<0){
            ((PlayActivity)getActivity()).getBaseApplication().insertTotalRecord();
        }else{
            ++totalrecordgrage;
           //((PlayActivity)getActivity()).getBaseApplication().updateGradeAlex(totalrecordgrage);
        }
    }
    public void recordTimeTextViewInit(){
        timeThread_time_num=0;//타이머 스레드의 Time;
        String text = timeToString(timeThread_time_num*1000);
        recordingTime.setText(text);
    }

    public void playPlayRecodeFile(){

        String mFilePath = getFinalRecordFileName();
        recordingfilePlayer = MediaPlayer.create((PlayActivity)getActivity(), Uri.parse(mFilePath));
        recordingfilePlayer.start();
        recordingfilePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnRecordingfilePlay.setChecked(false);
                btnSaveRecordingfile.setEnabled(true);
                btnSaveRecordingfile.getBackground().setAlpha(255);
                btnRecording.setEnabled(true);
                btnRecording.getBackground().setAlpha(255);
            }
        });
    }

    public String getRecordFilePath(){

        File catefolder = new File(getCateFolderPath());
        if(!catefolder.exists()){
            catefolder.mkdir();
        }else{
            PistolLogger.LOGD("폴더가 존재합니다");
        }

        File folder = new File(getFilePath());
        if(!folder.exists()){
            folder.mkdir();
        }else{
            PistolLogger.LOGD("폴더가 존재합니다");
        }

        File newFile = new File(getFilePath());
        FilenameFilter listTest = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.contains("_temp");
            }
        };

        String[] ff = newFile.list(listTest);
        int prefix = 0;
        if(ff!=null) {
            PistolLogger.LOGD("ff의 길이는 : " + Integer.toString(ff.length));
            prefix =ff.length;
        }

        String mFilePath = getFilePath()+"recorder_"+((PlayActivity)getActivity()).getContentId()+"_temp"+prefix+".wav";
        return mFilePath;
    }

    public void recorder(){

        String filepath = getRecordFilePath();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        myRecorder.setOutputFile(filepath);
        PistolLogger.LOGD(filepath);

        try {
        myRecorder.prepare();
        myRecorder.start();
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void startTempFileMerge() {
        PistolLogger.LOGD("파일병합시작");
        File mFile = new File(getFilePath());

        FilenameFilter tempFileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.contains("_temp");
            }
        };

        String[] tempFileList = mFile.list(tempFileFilter);
        if(tempFileFilter!=null) {
            String outputFile = getFinalRecordFileName();
            PistolLogger.LOGD("outputFile name:" + outputFile);
            Movie[] inMovies = new Movie[tempFileList.length];

            try {
                for (int i = 0; i < tempFileList.length; ++i) {
                    inMovies[i] = MovieCreator.build(getFilePath() + tempFileList[i]);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Track> audioTracks = new LinkedList<Track>();
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                }
            }
            Movie output = new Movie();
            if (audioTracks.size() > 0) {
                try {
                    output.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Container out = new DefaultMp4Builder().build(output);

            FileChannel fc = null;
            try {
                fc = new FileOutputStream(new File(outputFile)).getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                out.writeContainer(fc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletTempRecordFile(){
        File tempFile = new File(getFilePath());
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {

                return name.contains("_temp");
            }
        };
        String[] tempFileList = tempFile.list(fileFilter);
        if(tempFileList!=null) {
            if (tempFileList.length > 0) {
                PistolLogger.LOGD("Temp 파일삭제");
                for (int i = 0; i < tempFileList.length; ++i) {
                    File deletFile = new File(getFilePath() + tempFileList[i]);
                    deletFile.delete();
                }
            }
        }
    }

    public void deletFinalRecordFile(){
        File finalRecordFile = new File(getFinalRecordFileName());
        if(finalRecordFile.exists()) finalRecordFile.delete();
    }

    private String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int timeInmillisec = Integer.parseInt( time );

        return timeToString(timeInmillisec);
    }
    public Timer m_timer;
    public TimerTask m_task;
    public Handler recordTimerHandler;
    public int timeThread_time_num = 0;
    private boolean recoderState=false;

    public void startRecordTimer(){
        if(m_timer!=null) {
            m_timer.cancel();
            m_timer=null;
        }
        if(m_task!=null) {
            m_task = null;
        }
        recoderState = true;
        recordTimerHandler = new Handler();
        m_timer = new Timer();
        m_task = new TimerTask() {

            @Override
            public void run() {
                if(recoderState) ++timeThread_time_num;
                PistolLogger.LOGD("TimerTask:num=>"+Integer.toString(timeThread_time_num*1000));
                recordTimerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String text = timeToString(timeThread_time_num*1000);
                        recordingTime.setText(text);
                    }
                });
            }
        };
        m_timer.schedule(m_task,1000,1000);
    }
    public String getCateFolderPath(){

       // String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filedir = getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        String folderName ="/MYSTORY_RECORDER/";
       // String catefolderName = Integer.toString(cateId)+"/";

        return filedir+folderName;
    }
    public String getFilePath(){

//        String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String folderName ="/MYSTORY_RECORDER/";

        String catefolderName = Integer.toString(((PlayActivity)getActivity()).getCateId())+"/";

        return getCateFolderPath()+catefolderName;
    }
//    public String[] getRecordFileList(){
//
//        File mfile = new File(getFilePath());
//        FilenameFilter fileFilter = new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String name) {
//                return name.contains("EP"+((PlayActivity)getActivity()).getContentId());
//            }
//        };
//        String[] recordingFileList = mfile.list(fileFilter);
//        return recordingFileList;
//    }
//    public String getRecordFileTotalNumber(){
//
//        File newFile = new File(getFilePath());
//        FilenameFilter fileList = new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String name) {
//                return name.contains("recorder_EP"+((PlayActivity)getActivity()).getContentId());
//            }
//        };
//
//        String[] recordFileList = newFile.list(fileList);
//        String totalRecordingFileNumber="0";
//        if(recordFileList!=null) {
//            totalRecordingFileNumber=Integer.toString(recordFileList.length);
//        }
//
//        return totalRecordingFileNumber;
//    }

    public String getFinalRecordFileName(){
        return  getFilePath() + "recorder_EP_final.wav";
    }

    public void setLayoutRecordingNameText(){
         String recordFileNameStr = "Recording EP "+getSaveRecordingFileName();

        TextView recordingfilename = (TextView) view.findViewById(R.id.txtFileName);
        recordingfilename.setText(recordFileNameStr);
    }

    public String getRecordingFileName(){
       // String recordFileNameStr = "recorder_EP"+String.format("%02d",contentId)+"-"+String.format("%03d",Integer.parseInt(getRecordFileTotalNumber())+1);
        return getFilePath() + getSaveRecordingFileName() + ".wav";
    }
    public String getSaveRecordingFileName(){
        ArrayList<RecordingFile> recordFileNameStr=((PlayActivity)getActivity()).getBaseApplication().getValue(((PlayActivity)getActivity()).getCateId(),((PlayActivity)getActivity()).getContentId());
        String _name = "";
        if(recordFileNameStr.size()==0){
            _name = "Rec-"+String.format("%02d",((PlayActivity)getActivity()).getContentId())+"-"+String.format("%03d",1);
            PistolLogger.LOGD("FILE NUMBER 000");
        }else{
            int fileNumber = Integer.parseInt(recordFileNameStr.get(recordFileNameStr.size()-1).getContentname().substring(7,10));
            _name =  "Rec-"+String.format("%02d",((PlayActivity)getActivity()).getContentId())+"-"+String.format("%03d",fileNumber+1);
            PistolLogger.LOGD("FILE NUMBER :   ["+Integer.toString(fileNumber)+"]" + recordFileNameStr.get(recordFileNameStr.size()-1).getContentname().substring(7,10)  );
        }
        return _name;
    }

    public void changeFinalRecordFileName(){
        File file = new File(getFinalRecordFileName());
        File newfile = new File(getRecordingFileName());

        if(file.exists()) file.renameTo(newfile);
    }

    public String[] sortFileList(String[] files, final int compareType){

        Arrays.sort(files, new Comparator<String>() {
            @Override
            public int compare(String object1, String object2) {
                String s1="";
                String s2="";

                s1 = (String)object1;
                s2 = (String)object2;

                return s2.compareTo(s1);
            }
        });
        for(String mfile:files){
            PistolLogger.LOGD("compareTEST:"+mfile);
        }
        return files;
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

    public String getCurrentData(){
        Calendar cal = Calendar.getInstance();
        String dateString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        return dateString;
    }

    @Override
    public void onPause() {
        super.onPause();
        recordStateChange();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeEventListner=null;
        saveRecordingFileEventListner=null;

        deletTempRecordFile();
        if(myRecorder!=null) {
            myRecorder.release();
            myRecorder=null;
        }
        if(m_timer!=null)  m_timer.cancel();
        recoderState = false;
        if(recordingfilePlayer!=null){
            recordingfilePlayer.release();
        }
        PistolLogger.LOGD(": onDestroyView");
    }

}
