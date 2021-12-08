package com.hoanmy.football.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.football.R;
import com.hoanmy.football.VideoPlayerNew;
import com.hoanmy.football.adapters.AllHomeAdapter;
import com.hoanmy.football.adapters.ProgressAdapter;
import com.hoanmy.football.api.RequestApi;
import com.hoanmy.football.commons.Constants;
import com.hoanmy.football.models.ListMatchInfo;
import com.hoanmy.football.models.ProgressLiveMatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LiveFragment extends BaseFragment {
    private List<ProgressLiveMatch> progressLiveMatches = new ArrayList<>();
    private ProgressAdapter progressAdapter;
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_home)
    public RecyclerView recyclerView;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        progressAdapter = new ProgressAdapter(getContext(), progressLiveMatches);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(progressAdapter);
        loadDataProgress(VideoPlayerNew.matchId());
    }

    private void loadDataProgress(String matchId) {
        RequestApi.getInstance().getMatchProgress(matchId).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        List<ProgressLiveMatch> _progressLiveMatches = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<List<ProgressLiveMatch>>() {
                        }.getType());
                        progressLiveMatches.addAll(_progressLiveMatches);
                        progressAdapter.notifyDataSetChanged();


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview_layout;
    }
}
