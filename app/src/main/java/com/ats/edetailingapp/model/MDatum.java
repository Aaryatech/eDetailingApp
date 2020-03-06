package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MDatum {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("t1_tid")
    @Expose
    private int t1Tid;
    @SerializedName("t2_tid")
    @Expose
    private int t2Tid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getT1Tid() {
        return t1Tid;
    }

    public void setT1Tid(int t1Tid) {
        this.t1Tid = t1Tid;
    }

    public int getT2Tid() {
        return t2Tid;
    }

    public void setT2Tid(int t2Tid) {
        this.t2Tid = t2Tid;
    }

    @Override
    public String toString() {
        return "MDatum{" +
                "id=" + id +
                ", t1Tid=" + t1Tid +
                ", t2Tid=" + t2Tid +
                '}';
    }
}
