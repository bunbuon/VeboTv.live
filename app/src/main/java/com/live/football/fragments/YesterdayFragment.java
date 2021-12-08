package com.hoanmy.football.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.football.R;
import com.hoanmy.football.adapters.AllHomeAdapter;
import com.hoanmy.football.adapters.YesterdayAdapter;
import com.hoanmy.football.api.RequestApi;
import com.hoanmy.football.commons.Constants;
import com.hoanmy.football.models.RootDataListMatch;
import com.hoanmy.football.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class YesterdayFragment extends BaseFragment {

    private LinearLayoutManager linearLayoutManager;
    private YesterdayAdapter yesterdayAdapter;
    private List<RootDataListMatch> rootMatchInfos = new ArrayList<>();
    @BindView(R.id.rcv_home)
    public RecyclerView recyclerView;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        getDataHome();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview_layout;
    }

    private void getDataHome() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        yesterdayAdapter = new YesterdayAdapter(getActivity(), rootMatchInfos);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(yesterdayAdapter);
        loadData();
    }

    private void loadData() {
        RequestApi.getInstance().getMatchList(Utils.getTime("yesterday")).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        List<RootDataListMatch> dataMatch = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<List<RootDataListMatch>>() {
                        }.getType());

                        rootMatchInfos.addAll(dataMatch);
                        yesterdayAdapter.notifyDataSetChanged();


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
