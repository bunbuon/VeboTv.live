package com.hoanmy.football;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hoanmy.football.commons.Action;
import com.hoanmy.football.fragments.LoginFragment;
import com.hoanmy.football.fragments.LossPassFragment;
import com.hoanmy.football.fragments.RegisterFragment;
import com.hoanmy.football.fragments.SplashFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment(new SplashFragment());

    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void changeFragmentEvents(Action action) {
        if (action == Action.HAVE_ACCOUNT)
            addFragment(new LoginFragment());
        else if (action == Action.REGISTER)
            addFragment(new RegisterFragment());
        else if (action == Action.LOSS_PASSWORD)
            addFragment(new LossPassFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        } else
            finish();

    }
}
