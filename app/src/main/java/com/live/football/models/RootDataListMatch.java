package com.hoanmy.football.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RootDataListMatch implements Parcelable {

    public League league;
    public List<ListMatchInfo> matches;

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public List<ListMatchInfo> getMatches() {
        return matches;
    }

    public void setMatches(List<ListMatchInfo> matches) {
        this.matches = matches;
    }

    public static Creator<RootDataListMatch> getCREATOR() {
        return CREATOR;
    }

    protected RootDataListMatch(Parcel in) {
        matches = in.createTypedArrayList(ListMatchInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(matches);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RootDataListMatch> CREATOR = new Creator<RootDataListMatch>() {
        @Override
        public RootDataListMatch createFromParcel(Parcel in) {
            return new RootDataListMatch(in);
        }

        @Override
        public RootDataListMatch[] newArray(int size) {
            return new RootDataListMatch[size];
        }
    };
}
