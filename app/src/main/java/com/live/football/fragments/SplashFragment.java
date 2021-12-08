package com.hoanmy.football.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.football.IOnBackPressed;
import com.hoanmy.football.MainActivity;
import com.hoanmy.football.R;
import com.hoanmy.football.VideoPlayerNew;
import com.hoanmy.football.commons.Action;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashFragment extends BaseFragment implements IOnBackPressed {
    @OnClick(R.id.watch_now)
    void clickWatch() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.have_account)
    void clickHaveAcount() {
        EventBus.getDefault().post(Action.HAVE_ACCOUNT);
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.splash_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
