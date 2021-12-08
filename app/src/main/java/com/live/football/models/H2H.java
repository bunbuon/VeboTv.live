package com.hoanmy.football.models;

import java.util.List;

public class H2H {
    private int home_win_count;
    private int away_win_count;
    private int draw_count;
    private List<ListMatchInfo> matches;

    public int getHome_win_count() {
        return home_win_count;
    }

    public void setHome_win_count(int home_win_count) {
        this.home_win_count = home_win_count;
    }

    public int getAway_win_count() {
        return away_win_count;
    }

    public void setAway_win_count(int away_win_count) {
        this.away_win_count = away_win_count;
    }

    public int getDraw_count() {
        return draw_count;
    }

    public void setDraw_count(int draw_count) {
        this.draw_count = draw_count;
    }

    public List<ListMatchInfo> getMatches() {
        return matches;
    }

    public void setMatches(List<ListMatchInfo> matches) {
        this.matches = matches;
    }
}
