package com.hoanmy.football.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.football.R;
import com.hoanmy.football.adapters.EndHomeAdapter;
import com.hoanmy.football.models.ListMatchInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EndMatchFragment extends BaseFragment {
    private LinearLayoutManager linearLayoutManager;
    private EndHomeAdapter _EndAdapter;
    private List<ListMatchInfo> rootMatchInfos = new ArrayList<>();
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
        _EndAdapter = new EndHomeAdapter(getActivity(), rootMatchInfos);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(_EndAdapter);
        loadData();
    }

    private void loadData() {
//        RequestApi.getInstance().getMatchList(Utils.getTimeNow()).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<JsonElement>() {
//                    @Override
//                    public void call(JsonElement jsonElement) {
//                        List<RootMatchInfo> dataMatch = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<List<RootMatchInfo>>() {
//                        }.getType());
//
//                        rootMatchInfos.addAll(dataMatch);
//                        _EndAdapter.notifyDataSetChanged();
//
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                });
    }
}
