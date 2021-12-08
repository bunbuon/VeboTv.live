package com.hoanmy.football.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListMatch implements Parcelable {
    public List<ListMatchInfo> rootMatchInfos;

    protected ListMatch(Parcel in) {
        rootMatchInfos = in.createTypedArrayList(ListMatchInfo.CREATOR);
    }

    public static final Creator<ListMatch> CREATOR = new Creator<ListMatch>() {
        @Override
        public ListMatch createFromParcel(Parcel in) {
            return new ListMatch(in);
        }

        @Override
        public ListMatch[] newArray(int size) {
            return new ListMatch[size];
        }
    };

    public List<ListMatchInfo> getRootMatchInfos() {
        return rootMatchInfos;
    }

    public void setRootMatchInfos(List<ListMatchInfo> rootMatchInfos) {
        this.rootMatchInfos = rootMatchInfos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(rootMatchInfos);
    }
}
