package com.hoanmy.football;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Rational;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.football.adapters.ServerAdapter;
import com.hoanmy.football.api.RequestApi;
import com.hoanmy.football.commons.Action;
import com.hoanmy.football.commons.Constants;
import com.hoanmy.football.commons.ItemClickListener;
import com.hoanmy.football.models.LinkStream;
import com.hoanmy.football.models.ListMatchInfo;
import com.hoanmy.football.widget.HandleGesturePlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VideoPlayerActivity extends BaseActivity implements
        HandleGesturePlayer.GestureListener, OnCompletionListener, OnPreparedListener, OnErrorListener, OnBufferUpdateListener, OnSeekCompletionListener {
    private static CookieManager DEFAULT_COOKIE_MANAGER;
    @BindView(R.id.videoView)
    VideoView playerView;
    @BindView(R.id.loadingBar)
    ProgressBar mProgressBar;
    @BindView(R.id.time_seek_to)
    TextView mTimeSeekTo;
    @BindView(R.id.time_seek)
    TextView mTimeSeek;
    @BindView(R.id.seekBar2)
    SeekBar mSeekbar;
    @BindView(R.id.info_swipe)
    TextView mInfoSwipe;
    @BindView(R.id.audio_player_controls_container)
    View mBottom;
    @BindView(R.id.currentPositionView)
    TextView mCurrent;
    @BindView(R.id.layout_controller)
    View mViewController;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.touch_view)
    View mTouchView;
    @BindView(R.id.title_film)
    TextView titleMatch;
    @BindView(R.id.list_server)
    RecyclerView listServer;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.close_webview)
    ImageView imgCloseComment;

    @OnClick(R.id.currentPositionView)
    public void OnClick() {
        webView.setVisibility(View.VISIBLE);
    }



    @OnClick(R.id.close_webview)
    public void OnClickMini() {
        if (webView.getVisibility() == View.VISIBLE) {
            imgCloseComment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_comment_24));
            webView.setVisibility(View.GONE);
        } else {
            imgCloseComment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_close_24));
            webView.setVisibility(View.VISIBLE);
        }
    }

    private HandleGesturePlayer mHandleGesture;
    private Handler mHideMediaController = new Handler();
    private static final long TIME_SHOW_PLAY_CONTROLER = 5000;
    private long mDuration;
    private long seektime = 0;
    private boolean seekPending = false;
    private long mCurrentPositionPlay;
    private BroadcastReceiver mReceiver;
    private ServerAdapter serverAdapter;
    private List<LinkStream> linkStreams;

    /**
     * Intent action for media controls from Picture-in-Picture mode.
     */
    private static final String ACTION_MEDIA_CONTROL = "media_control";
    private static final String EXTRA_CONTROL_TYPE = "control_type";
    private static final int CONTROL_TYPE_PLAY = 1;
    private static final int CONTROL_TYPE_PAUSE = 2;

    public static VideoPlayerActivity newInstant() {
        return new VideoPlayerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        imgCloseComment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_close_24));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView.setWebViewClient(new MyBrowser());
        webView.setPadding(0, 0, 0, 0);
//        webView.setInitialScale(getScale());
        webView.loadUrl("https://www6.cbox.ws/box/?boxid=827198&boxtag=wskql9&nme=Nguy%E1%BB%85n%20Thu%E1%BA%ADn%20%5B0545413%5D&lnk=&pic=https%3A%2F%2Fgraph.facebook.com%2F3528933970545413%2Fpicture%3Ftype%3Dsmall&sig=XT%2F6UPl18L%2BEK3ydTBCygDOdTRZYDBlOj4RtI0sAXVM%3D");


        String idStream = getIntent().getStringExtra(getString(R.string.idStream));
        loadData(idStream);
        setupToolbar();
//        setupSeekBar();
        setupVideoView();
        setupGesture();
    }


    @Subscribe
    public void onEventClick(Action action) {
        if (action == Action.CHANGE_SERVER) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }


    private void initializePlayer(List<LinkStream> _LinkStreams, int position) {
        String link = _LinkStreams.get(position).getUrl();
//        titleMatch.setText(_LinkStreams.get(position).getName());
        playerView.setOnPreparedListener(this);
        playerView.setVideoURI(Uri.parse(link), mediaSourceForUri(link));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        serverAdapter = new ServerAdapter(this, _LinkStreams, new ItemClickListener() {
            @Override
            public void onPositionClicked(int position) {
                for (int i = 0; i < _LinkStreams.size(); i++) {
                    if (i == position)
                        _LinkStreams.get(i).isActive = true;
                    else _LinkStreams.get(i).isActive = false;
                }
                initializePlayer(_LinkStreams, position);
                serverAdapter.notifyDataSetChanged();
            }
        });
        listServer.setLayoutManager(linearLayoutManager);
        listServer.setAdapter(serverAdapter);

    }

    private void loadData(String id) {
        RequestApi.getInstance().getMatchLiveStream(id).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        linkStreams = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<List<LinkStream>>() {
                        }.getType());
                        if (linkStreams.size() > 0) {
                            linkStreams.get(0).setActive(true);
                            initializePlayer(linkStreams, 0);
                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.error_empty_match), Toast.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    private void setupToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_menu_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private MediaSource mediaSourceForUri(String linkPlay) {
        DefaultHttpDataSourceFactory defaultHttpDataSource = new DefaultHttpDataSourceFactory(System.getProperty("http.agent"), BANDWIDTH_METER);
        defaultHttpDataSource.setDefaultRequestProperty("Referer", "https://embed.megaxfer.ru");
        MediaSource mediaSource = null;
        if (linkPlay.contains("m3u8")) {
            mediaSource = new HlsMediaSource(Uri.parse(linkPlay), new DefaultDataSourceFactory(getApplicationContext(), BANDWIDTH_METER, defaultHttpDataSource),
                    new Handler(), null);
        } else {
            String userAgent = System.getProperty("http.agent");
            MediaSource videoSource = new ExtractorMediaSource(
                    Uri.parse(linkPlay),
                    new DefaultDataSourceFactory(getApplicationContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            return videoSource;
        }

        return mediaSource;

    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onPrepared() {
        playerView.setOnBufferUpdateListener(this);
        playerView.setOnSeekCompletionListener(this);
        mDuration = playerView.getDuration();
        mProgressBar.setVisibility(View.GONE);
//        mProgressPlayer.post(mRunnablePlayer);
        playerView.start();

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onChangeVolumn(int volumn) {
        mInfoSwipe.setVisibility(View.VISIBLE);
        mInfoSwipe.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_volume_up_white_96dp), null, null, null);
        mInfoSwipe.setText(volumn + "");
    }

    @Override
    public void onChangeBrightness(int brightness) {
        mInfoSwipe.setVisibility(View.VISIBLE);
        mInfoSwipe.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_brightness_high_white_96dp), null, null, null);
        mInfoSwipe.setText(brightness + "");
    }

    @Override
    public void onStartGestureSeek() {
        seekPending = true;
    }

    @Override
    public void onSeek(long startTimeSeek, int second) {
        mTimeSeek.setVisibility(View.VISIBLE);
        seektime = second;
        showTimeSeek(second);
    }

    @Override
    public void onSingleTapUp() {
        if (mBottom.getAlpha() == 1f) {
            mHideMediaController.removeCallbacks(mRunnableHideController);

            hideMediaController();
            hideStatusBarAndNavigation();
        } else {
            showStatusBarAndNavigation();
            showMediaController();
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
//        mSeekbar.setSecondaryProgress(percent);
    }

    @Override
    public boolean onError(Exception e) {
        Toast.makeText(getApplicationContext(), "Server hiện đang quá tải! Vui lòng chọn Server khác để trải nghiệm tốt hơn.", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onSeekComplete() {
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        playerView.start();
    }

    private void setupVideoView() {
        mCurrent.setText("Live");
        mHideMediaController.postDelayed(mRunnableHideController, TIME_SHOW_PLAY_CONTROLER);
        playerView.setOnCompletionListener(this);
        playerView.setOnPreparedListener(this);
        playerView.setOnErrorListener(this);
    }

    private void setupGesture() {
        mHandleGesture = new HandleGesturePlayer(this, this);
        mTouchView.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (seekPending) {
                    playerView.seekTo((int) (seektime + playerView.getCurrentPosition()));
                    seekPending = false;
                }
                delayActionAfterSwipe();
                mHandleGesture.restartGesture();
            }

            if (Build.VERSION.SDK_INT >= 18) {
                return mHandleGesture.setTouchEvent(motionEvent);
            } else {
                hideMediaController();
                hideStatusBarAndNavigation();
            }

            return true;
        });
    }

    private void delayActionAfterSwipe() {
        mHideAfterSwipeAction.postDelayed(mRunnableHideAfterSwipeAction, 2000);
    }

    private Handler mHideAfterSwipeAction = new Handler();
    private Runnable mRunnableHideAfterSwipeAction = new Runnable() {
        @Override
        public void run() {
            mInfoSwipe.setVisibility(View.GONE);
            mTimeSeek.setVisibility(View.GONE);
            mTimeSeekTo.setVisibility(View.GONE);
        }
    };

//    private void setupSeekBar() {
//        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    int seekTo = (int) ((mDuration * progress) / 100);
//
//                    mProgressBar.setVisibility(View.VISIBLE);
////                    imgPlayPause.setImageResource(R.drawable.ic_play_arrow_white);
//
//                    playerView.seekTo(seekTo);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                mHideMediaController.removeCallbacks(mRunnableHideController);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mHideMediaController.postDelayed(mRunnableHideController, TIME_SHOW_PLAY_CONTROLER);
//            }
//        });
//    }


    private void showTimeSeek(long second) {
        int currentPosition = (int) playerView.getCurrentPosition();

        if (currentPosition >= 0) {
            long seekTo = currentPosition + second;

            seekTo = seekTo <= mDuration ? seekTo : mDuration;

            mTimeSeek.setText(getLabelTime(second) + "(" + getLabelTime(seekTo).replace("+", "") + ")");
        }
    }

    private String getTime(long time) {
        int minute = (int) ((time / 1000) / 60);
        int second = (int) ((time / 1000) - minute * 60);

        if (minute < 10 && second < 10) {
            return "0" + minute + ":" + "0" + second;
        } else if (minute < 10) {
            return "0" + minute + ":" + second;
        } else if (second < 10) {
            return minute + ":" + "0" + second;
        } else {
            return minute + ":" + second;
        }
    }

    private String getLabelTime(long time) {
        boolean isNegative = time < 0 ? true : false;

        time = Math.abs(time);

        time /= 1000;

        int second = (int) time % 60;
        int minute = (int) time / 60;

        String strSecond = second < 10 ? "0" + second : second + "";
        String strMinute = minute < 10 ? "0" + minute : minute + "";

        return (isNegative ? "-" : "+") + strMinute + " : " + strSecond;
    }

    private Runnable mRunnableHideController = new Runnable() {
        @Override
        public void run() {
            hideMediaController();
            hideStatusBarAndNavigation();
        }
    };
//    private Handler mProgressPlayer = new Handler();
//    private Runnable mRunnablePlayer = new Runnable() {
//        @Override
//        public void run() {
//            if (playerView != null && playerView.isPlaying()) {
//                mCurrentPositionPlay = playerView.getCurrentPosition();
//
//                mCurrent.setText("Live");
//
//                int percent = (int) (100 * (((float) mCurrentPositionPlay) / mDuration));
//                mSeekbar.setProgress(percent);
//
//                if (seekPending) showTimeSeek(seektime);
//            }
//
//            mProgressPlayer.postDelayed(this, 1000);
//        }
//    };

    private void hideMediaController() {
        mToolbar.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mToolbar != null) mToolbar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBottom.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mBottom != null) mBottom.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mViewController.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mViewController != null) mViewController.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void hideStatusBarAndNavigation() {
        int uiFlags;
        uiFlags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= 16)
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        if (Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    private void showMediaController() {
        mHideMediaController.removeCallbacks(mRunnableHideController);
        mToolbar.animate().alpha(1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBottom.animate().alpha(1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mViewController.animate().alpha(1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mViewController.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        mHideMediaController.postDelayed(mRunnableHideController, TIME_SHOW_PLAY_CONTROLER);
    }

    private void showStatusBarAndNavigation() {
        int uiFlags = getWindow().getDecorView().getSystemUiVisibility();
        uiFlags ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiFlags ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        uiFlags ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        if (Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * Enters Picture-in-Picture mode.
     */
    void minimize() {
        if (playerView == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        PictureInPictureParams.Builder mPictureInPictureParamsBuilder =
                new PictureInPictureParams.Builder();
        // Calculate the aspect ratio of the PiP screen.
        Rational aspectRatio = new Rational(playerView.getWidth(), playerView.getHeight());
        mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
        enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        minimize();
    }


    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration configuration) {

        super.onPictureInPictureModeChanged(isInPictureInPictureMode, configuration);
        if (isInPictureInPictureMode) {
            // Starts receiving events from action items in PiP mode.
            playerView.start();
            mReceiver =
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent == null
                                    || !ACTION_MEDIA_CONTROL.equals(intent.getAction())) {
                                return;
                            }

                            // This is where we are called back from Picture-in-Picture action
                            // items.
                            final int controlType = intent.getIntExtra(EXTRA_CONTROL_TYPE, 0);
                            switch (controlType) {
                                case CONTROL_TYPE_PLAY:
                                    playerView.start();
                                    break;
                                case CONTROL_TYPE_PAUSE:
                                    playerView.pause();
                                    break;
                            }
                        }
                    };
            registerReceiver(mReceiver, new IntentFilter(ACTION_MEDIA_CONTROL));
        } else {
            // We are out of PiP mode. We can stop receiving events from it.
            unregisterReceiver(mReceiver);
            mReceiver = null;
            // Show the video controls if the video is not playing
            if (playerView != null && !playerView.isPlaying()) {
                Log.d("", "onPictureInPictureModeChanged: ");
                playerView.start();
//                showControls();
            }

            onPrepared();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustFullScreen(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            adjustFullScreen(getResources().getConfiguration());
        }
    }

    private void adjustFullScreen(Configuration config) {
        final View decorView = getWindow().getDecorView();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isInPictureInPictureMode()) {
                // Show the video controls so the video can be easily resumed.

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        playerView.pause();
        EventBus.getDefault().unregister(this);
    }
}
