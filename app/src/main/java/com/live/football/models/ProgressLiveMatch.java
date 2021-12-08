package com.hoanmy.football.models;

public class ProgressLiveMatch {
    public String type;
    public String text;
    public int time;
    public boolean is_home;
    public boolean is_live;
    public int added_time;
    public int away_score;
    public int home_score;
    public PlayerScore player;
    public PlayerAssist assist;
    public PlayerIn player_in;
    public PlayerOut player_out;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isIs_home() {
        return is_home;
    }

    public void setIs_home(boolean is_home) {
        this.is_home = is_home;
    }

    public boolean isIs_live() {
        return is_live;
    }

    public void setIs_live(boolean is_live) {
        this.is_live = is_live;
    }

    public int getAdded_time() {
        return added_time;
    }

    public void setAdded_time(int added_time) {
        this.added_time = added_time;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public PlayerScore getPlayer() {
        return player;
    }

    public void setPlayer(PlayerScore player) {
        this.player = player;
    }

    public PlayerAssist getAssist() {
        return assist;
    }

    public void setAssist(PlayerAssist assist) {
        this.assist = assist;
    }

    public PlayerIn getPlayer_in() {
        return player_in;
    }

    public void setPlayer_in(PlayerIn player_in) {
        this.player_in = player_in;
    }

    public PlayerOut getPlayer_out() {
        return player_out;
    }

    public void setPlayer_out(PlayerOut player_out) {
        this.player_out = player_out;
    }
}
