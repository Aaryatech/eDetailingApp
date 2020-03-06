package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag1 {

    @SerializedName("t1_tid")
    @Expose
    private int t1Tid;
    @SerializedName("t1_tag")
    @Expose
    private String t1Tag;

    public Tag1() {
    }

    public Tag1(int t1Tid, String t1Tag) {
        this.t1Tid = t1Tid;
        this.t1Tag = t1Tag;
    }

    public int getT1Tid() {
        return t1Tid;
    }

    public void setT1Tid(int t1Tid) {
        this.t1Tid = t1Tid;
    }

    public String getT1Tag() {
        return t1Tag;
    }

    public void setT1Tag(String t1Tag) {
        this.t1Tag = t1Tag;
    }

    @Override
    public String toString() {
        return "Tag1{" +
                "t1Tid=" + t1Tid +
                ", t1Tag='" + t1Tag + '\'' +
                '}';
    }
}
