package com.hoanmy.football.models;

public class SplashAds {
    private String type;
    private String click_url;
    private String url;
    private boolean skip;
    private int skip_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public int getSkip_time() {
        return skip_time;
    }

    public void setSkip_time(int skip_time) {
        this.skip_time = skip_time;
    }
}
