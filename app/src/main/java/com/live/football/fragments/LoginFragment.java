package com.hoanmy.football.fragments;

import android.os.Bundle;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.football.IOnBackPressed;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.Action;
import com.hoanmy.football.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements IOnBackPressed {
    @OnClick(R.id.image_back)
    void onBack(){
        getActivity().onBackPressed();
    }
    @OnClick(R.id.tv_loss_password)
    void clickLossPass(){
        EventBus.getDefault().post(Action.LOSS_PASSWORD);
    }
    @OnClick(R.id.tv_create_new_account)
    void clickCreateAccount(){
        EventBus.getDefault().post(Action.REGISTER);
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
