package com.hoanmy.football.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hoanmy.football.R;
import com.hoanmy.football.adapters.AdsPagerAdapter;
import com.hoanmy.football.adapters.TabsPagerAdapter;
import com.hoanmy.football.commons.DeactivatedViewPager;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static android.view.View.VISIBLE;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.viewPagerImages)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_group)
    LinearLayout viewGr;
    @BindView(R.id.view_pager_content)
    DeactivatedViewPager viewPagerContent;
    private AdsPagerAdapter adsPagerAdapter;
    int[] images = {R.drawable.thumb_ads, R.drawable.thumb_splash, R.drawable.thumb_ads, R.drawable.thumb_ads};
    int positionImage = 0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
//        callAdFragment(new AllMatchFragment());
        adsPagerAdapter = new AdsPagerAdapter(getContext(), images);
        viewPager.setAdapter(adsPagerAdapter);
        runAutoPager();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionImage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabsPagerAdapter sectionsPagerAdapter = new TabsPagerAdapter(getActivity(), getChildFragmentManager());
        viewPagerContent.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPagerContent);
        viewPagerContent.setCurrentItem(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1 || tab.getPosition() == 2) {
                    handler.removeCallbacks(update);
                    viewGoneAnimator(viewPager);

                } else {
                    runAutoPager();
                    viewVisibleAnimator(viewPager);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void viewGoneAnimator(final View view) {
        if (view.getVisibility() == VISIBLE) {
            view.setVisibility(View.GONE);
        }

    }

    private void viewVisibleAnimator(final View view) {

        Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_viewpager);
        view.setVisibility(VISIBLE);
        view.startAnimation(slideUp);

    }

    private void runAutoPager() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
    }

    Handler handler = new Handler();

    Runnable update = new Runnable() {

        public void run() {
            if (positionImage == images.length) {

                positionImage = 0;
            }
            viewPager.setCurrentItem(positionImage++, true);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }


    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(update);
    }
}
