package com.hoanmy.football.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hoanmy.football.R;
import com.hoanmy.football.fragments.HomeFragment;
import com.hoanmy.football.fragments.LiveFragment;
import com.hoanmy.football.fragments.SplashFragment;
import com.hoanmy.football.fragments.TomorrowFragment;
import com.hoanmy.football.fragments.YesterdayFragment;

public class MatchTabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_watch1, R.string.tab_text_watch2, R.string.tab_text_watch3, R.string.tab_text_watch4, R.string.tab_text_watch5, R.string.tab_text_watch6};
    private final Context mContext;

    public MatchTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new LiveFragment();
        else return new SplashFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}