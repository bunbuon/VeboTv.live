package com.hoanmy.football.models;

import java.util.List;

public class Ads {
    private List<Banner> banner;
    private SplashAds splash;
    private List<FullScreenAds> full_screen;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public SplashAds getSplash() {
        return splash;
    }

    public void setSplash(SplashAds splash) {
        this.splash = splash;
    }

    public List<FullScreenAds> getFull_screen() {
        return full_screen;
    }

    public void setFull_screen(List<FullScreenAds> full_screen) {
        this.full_screen = full_screen;
    }
}
