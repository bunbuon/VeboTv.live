package com.hoanmy.football.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.hoanmy.football.IOnBackPressed;
import com.hoanmy.football.R;
import com.hoanmy.football.fragments.BaseFragment;

import butterknife.OnClick;

public class RegisterFragment extends BaseFragment implements IOnBackPressed {
    @OnClick(R.id.image_back)
    void onBack(){
        getActivity().onBackPressed();
    }
    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.register_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
