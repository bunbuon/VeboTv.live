package com.hoanmy.football.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hoanmy.football.R;
import com.hoanmy.football.fragments.AllMatchFragment;
import com.hoanmy.football.fragments.EndMatchFragment;
import com.hoanmy.football.fragments.HomeFragment;
import com.hoanmy.football.fragments.LiveMatchFragment;
import com.hoanmy.football.ui.main.PlaceholderFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.text_tab_all, R.string.text_tab_live, R.string.text_tab_end};
    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new AllMatchFragment();
        else if (position == 1)
            return new LiveMatchFragment();
        else return new EndMatchFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}