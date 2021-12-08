package com.hoanmy.football.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.Action;
import com.hoanmy.football.models.Ads;
import com.hoanmy.football.models.Config;
import com.hoanmy.football.widget.HandleGesturePlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.paperdb.Paper;

public class AdsFragment extends BaseFragment implements OnCompletionListener, OnPreparedListener, OnErrorListener, OnBufferUpdateListener, OnSeekCompletionListener {
    @BindView(R.id.image_ads)
    AppCompatImageView imageAd;

    @BindView(R.id.videoView)
    VideoView videoViewAd;

    @BindView(R.id.close_ads)
    AppCompatImageView closeAd;

    @OnClick(R.id.root_ad)
    void onClickView() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkDirect));
        startActivity(browserIntent);
    }

    @OnClick(R.id.close_ads)
    void onClickClose() {
        if (videoViewAd.isPlaying())
            videoViewAd.pause();
        getView().setVisibility(View.GONE);

        if (typeScreen.equals(Action.MAIN_AD.toString()))
            EventBus.getDefault().post(Action.CLOSE_AD_MAIN);
    }

    private Config config;
    static String typeScreen;
    private long timeStart;
    private String linkDirect;


    public static AdsFragment newInstant(String type) {
        typeScreen = type;
        return new AdsFragment();
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {

        config = Paper.book().read("ads");

        videoViewAd.setOnCompletionListener(this);
        videoViewAd.setOnPreparedListener(this);
        videoViewAd.setOnErrorListener(this);
        if (typeScreen.equals(Action.SPLASH_AD.toString())) {
            if (config.getAds().getSplash().getType().equals("video")) {
                linkDirect = config.getAds().getSplash().getClick_url();
                videoViewAd.setVisibility(View.VISIBLE);
                videoViewAd.setVideoURI(Uri.parse(config.getAds().getSplash().getUrl()));
                long timeStop = config.getAds().getSplash().getSkip_time() * 1000;
                if (config.getAds().getSplash().isSkip()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeAd.setVisibility(View.VISIBLE);
                        }
                    }, timeStop);
                } else
                    closeAd.setVisibility(View.GONE);
            } else {
                linkDirect = config.getAds().getSplash().getClick_url();
                imageAd.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(config.getAds().getSplash().getUrl()).into(imageAd);
            }
        } else {
            int position = new Random().nextInt(config.getAds().getFull_screen().size());
            if (config.getAds().getFull_screen().get(position).getType().equals("video")) {
                linkDirect = config.getAds().getFull_screen().get(position).getClick_url();
                videoViewAd.setVisibility(View.VISIBLE);
                videoViewAd.setVideoURI(Uri.parse(config.getAds().getFull_screen().get(position).getUrl()));
                long timeStop = config.getAds().getFull_screen().get(position).getSkip_time() * 1000;
                if (config.getAds().getFull_screen().get(position).isSkip()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeAd.setVisibility(View.VISIBLE);
                        }
                    }, timeStop);
                }else
                    closeAd.setVisibility(View.GONE);
            } else {
                linkDirect = config.getAds().getFull_screen().get(position).getClick_url();
                imageAd.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(config.getAds().getFull_screen().get(position).getUrl()).into(imageAd);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ads_fragment;
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public boolean onError(Exception e) {
        return false;
    }

    @Override
    public void onPrepared() {
        videoViewAd.start();
    }

    @Override
    public void onSeekComplete() {

    }
}
