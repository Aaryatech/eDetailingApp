package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag2 {

    @SerializedName("t2_tid")
    @Expose
    private int t2Tid;
    @SerializedName("t2_tag")
    @Expose
    private String t2Tag;

    public int getT2Tid() {
        return t2Tid;
    }

    public void setT2Tid(int t2Tid) {
        this.t2Tid = t2Tid;
    }

    public String getT2Tag() {
        return t2Tag;
    }

    public void setT2Tag(String t2Tag) {
        this.t2Tag = t2Tag;
    }

    @Override
    public String toString() {
        return "Tag2{" +
                "t2Tid=" + t2Tid +
                ", t2Tag='" + t2Tag + '\'' +
                '}';
    }
}

