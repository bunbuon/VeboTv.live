package com.hoanmy.football.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ListMatchInfo implements Parcelable {
    public Scores scores;
    public HomeMatch home;
    public AwayMatch away;
    public String date;
    public long timestamp;
    public long change_timestamp;
    public String sport_type;
    public String match_status;
    public ParseStatus parse_data;
    public League league;
    public int win_code;
    public int home_red_cards;
    public int away_red_cards;
    public ColorsMatch colors;
    public String id;
    public Commentators commentators;

    public Referee referee;
    public Venue venue;

    public Commentators getCommentators() {
        return commentators;
    }

    public void setCommentators(Commentators commentators) {
        this.commentators = commentators;
    }

    protected ListMatchInfo(Parcel in) {
        date = in.readString();
        timestamp = in.readLong();
        change_timestamp = in.readLong();
        sport_type = in.readString();
        match_status = in.readString();
        win_code = in.readInt();
        home_red_cards = in.readInt();
        away_red_cards = in.readInt();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeLong(timestamp);
        dest.writeLong(change_timestamp);
        dest.writeString(sport_type);
        dest.writeString(match_status);
        dest.writeInt(win_code);
        dest.writeInt(home_red_cards);
        dest.writeInt(away_red_cards);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListMatchInfo> CREATOR = new Creator<ListMatchInfo>() {
        @Override
        public ListMatchInfo createFromParcel(Parcel in) {
            return new ListMatchInfo(in);
        }

        @Override
        public ListMatchInfo[] newArray(int size) {
            return new ListMatchInfo[size];
        }
    };

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HomeMatch getHome() {
        return home;
    }

    public void setHome(HomeMatch home) {
        this.home = home;
    }

    public AwayMatch getAway() {
        return away;
    }

    public void setAway(AwayMatch away) {
        this.away = away;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getChange_timestamp() {
        return change_timestamp;
    }

    public void setChange_timestamp(long change_timestamp) {
        this.change_timestamp = change_timestamp;
    }


    public String getSport_type() {
        return sport_type;
    }

    public void setSport_type(String sport_type) {
        this.sport_type = sport_type;
    }

    public String getMatch_status() {
        return match_status;
    }

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
    }

    public int getWin_code() {
        return win_code;
    }

    public void setWin_code(int win_code) {
        this.win_code = win_code;
    }

    public int getHome_red_cards() {
        return home_red_cards;
    }

    public void setHome_red_cards(int home_red_cards) {
        this.home_red_cards = home_red_cards;
    }

    public int getAway_red_cards() {
        return away_red_cards;
    }

    public void setAway_red_cards(int away_red_cards) {
        this.away_red_cards = away_red_cards;
    }

    public ColorsMatch getColors() {
        return colors;
    }

    public void setColors(ColorsMatch colors) {
        this.colors = colors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public ParseStatus getParse_data() {
        return parse_data;
    }

    public void setParse_data(ParseStatus parse_data) {
        this.parse_data = parse_data;
    }

    public static Creator<ListMatchInfo> getCREATOR() {
        return CREATOR;
    }
}
