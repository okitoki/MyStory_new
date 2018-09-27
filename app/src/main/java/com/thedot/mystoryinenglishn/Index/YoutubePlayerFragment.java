package com.thedot.mystoryinenglishn.Index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.thedot.mystoryinenglishn.Utils.PistolLogger;

public class YoutubePlayerFragment extends YouTubePlayerFragment implements YouTubePlayer.OnInitializedListener{

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        PistolLogger.LOGD("Initialization_Success");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        PistolLogger.LOGD("onInitialization_Failure");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void initialize(String key, YouTubePlayer.OnInitializedListener onInitializedListener) {
        //String key = "AIzaSyD1a83GdXAxj1IdzYaWWR2aXA2AfG22Df4";
        super.initialize(key, onInitializedListener);

    }

}
