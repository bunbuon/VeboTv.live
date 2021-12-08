package com.hoanmy.football;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrlplusz.anytextview.AnyTextView;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.football.adapters.CustomSpinnerAdapter;
import com.hoanmy.football.adapters.MatchTabsPagerAdapter;
import com.hoanmy.football.adapters.ServerAdapter;
import com.hoanmy.football.api.RequestApi;
import com.hoanmy.football.commons.Constants;
import com.hoanmy.football.models.LinkStream;
import com.hoanmy.football.models.ListMatchInfo;
import com.hoanmy.football.ui.main.SectionsPagerAdapter;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VideoPlayerNew extends BaseActivity implements OnPreparedListener, AdapterView.OnItemSelectedListener, OnErrorListener {

    @BindView(R.id.videoView)
    VideoView playerView;
    @BindView(R.id.loadingBar)
    ProgressBar mProgressBar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.item_blow_video)
    LinearLayout linearLayoutItem;
    @BindView(R.id.fullscreen)
    AppCompatImageView imgFullScreen;

    @BindView(R.id.id_spinner)
    Spinner spinnerServer;

    @BindView(R.id.txt_nameBLV)
    AnyTextView txtNameBLV;
    @BindView(R.id.logo_home)
    AppCompatImageView imgLogoHome;
    @BindView(R.id.logo_away)
    AppCompatImageView imgLogoAway;
    @BindView(R.id.img_avata)
    AppCompatImageView imgAvt;
    @BindView(R.id.name_home)
    AnyTextView txtNameHome;
    @BindView(R.id.name_away)
    AnyTextView txtNameAway;
    @BindView(R.id.tv_score_match)
    AnyTextView txtScore;
    @BindView(R.id.time_match)
    AnyTextView txtTime;
    @BindView(R.id.web_view)
    WebView webView;
    private ServerAdapter serverAdapter;
    private List<LinkStream> linkStreams;
    private ListMatchInfo matchInfo;
    private List<String> nameServer = new ArrayList<>();
    private CustomSpinnerAdapter arrayAdapter;
    private static CookieManager DEFAULT_COOKIE_MANAGER;
    static String idStream;

    @OnClick(R.id.fullscreen)
    void onClickFullscreen() {
        if (linearLayoutItem.getVisibility() == View.VISIBLE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            linearLayoutItem.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ic_baseline_fullscreen_exit_24).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgFullScreen);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            linearLayoutItem.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.ic_baseline_fullscreen_24).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgFullScreen);
        }
    }

    @OnClick(R.id.btn_open_chat)
    void OnClickOpenChat() {
        findViewById(R.id.btn_open_chat).setVisibility(View.GONE);
        findViewById(R.id.btn_close_chat).setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        findViewById(R.id.viewBottom).setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_close_chat)
    void OnClickCloseChat() {
        findViewById(R.id.btn_open_chat).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_close_chat).setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        findViewById(R.id.viewBottom).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video_player_test);
        ButterKnife.bind(this);

        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        idStream = getIntent().getStringExtra(getString(R.string.idStream));
        setupToolbar();
        loadComment();
        spinnerServer.setOnItemSelectedListener(this);
        loadData(idStream);

        MatchTabsPagerAdapter sectionsPagerAdapter = new MatchTabsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    public static String matchId() {
        return idStream;
    }

    private void loadComment() {
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

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        ActionBar actionBar = this.getSupportActionBar();
        ViewGroup.LayoutParams params = playerView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            if (actionBar.isShowing())
//                actionBar.hide();


            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;

            playerView.requestLayout();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            if (!actionBar.isShowing())
//                actionBar.show();

            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;

            playerView.requestLayout();
        }
        super.onConfigurationChanged(newConfig);

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
                            for (int i = 0; i < linkStreams.size(); i++) {
                                nameServer.add(linkStreams.get(i).getName());
                            }

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

        RequestApi.getInstance().getMatchDetails(id).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        matchInfo = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ListMatchInfo>() {
                        }.getType());
                        initDataMatch(matchInfo);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void initDataMatch(ListMatchInfo matchInfo) {
        txtNameBLV.setText(matchInfo.getCommentators().getName());
        txtNameHome.setText(matchInfo.getHome().name);
        Glide.with(this).load(matchInfo.getHome().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoHome);
        txtNameAway.setText(matchInfo.getAway().name);
        Glide.with(this).load(matchInfo.getAway().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoAway);
        Glide.with(this).load(matchInfo.getCommentators().image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgAvt);
        txtScore.setText(matchInfo.getScores().home + " - " + matchInfo.getScores().away);
        txtTime.setText(matchInfo.getParse_data().getTime());
    }


    private void initializePlayer(List<LinkStream> _LinkStreams, int position) {
        String link = _LinkStreams.get(position).getUrl();

        playerView.setOnPreparedListener(this);
        playerView.setVideoURI(Uri.parse(link), mediaSourceForUri(link));
        if (arrayAdapter == null) {
            arrayAdapter = new CustomSpinnerAdapter(this, R.layout.item_list_server, nameServer);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerServer.setAdapter(arrayAdapter);
        }
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

    private void setupToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_menu_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    @Override
    public void onPrepared() {
        mProgressBar.setVisibility(View.GONE);
        playerView.start();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        playerView.pause();
        mProgressBar.setVisibility(View.VISIBLE);
        initializePlayer(linkStreams, position);
        Toast.makeText(getApplicationContext(), "Selected Employee: " + linkStreams.get(position).getUrl(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onError(Exception e) {
        Toast.makeText(getApplicationContext(), "Server hiện đang quá tải! Vui lòng chọn Server khác để trải nghiệm tốt hơn.", Toast.LENGTH_LONG).show();
        return false;
    }


    @Override
    public void onBackPressed() {
        if (linearLayoutItem.getVisibility() == View.VISIBLE)
            super.onBackPressed();
    }
}
