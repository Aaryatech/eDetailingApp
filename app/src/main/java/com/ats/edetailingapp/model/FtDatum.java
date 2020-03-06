package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FtDatum {

    @SerializedName("ft_id")
    @Expose
    private int ftId;
    @SerializedName("t1_tid")
    @Expose
    private int t1Tid;
    @SerializedName("t2_tid")
    @Expose
    private int t2Tid;
    @SerializedName("f_type_id")
    @Expose
    private int fTypeId;
    @SerializedName("lang_id")
    @Expose
    private int langId;
    @SerializedName("f_id")
    @Expose
    private int fId;

    public int getFtId() {
        return ftId;
    }

    public void setFtId(int ftId) {
        this.ftId = ftId;
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

    public int getfTypeId() {
        return fTypeId;
    }

    public void setfTypeId(int fTypeId) {
        this.fTypeId = fTypeId;
    }

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    @Override
    public String toString() {
        return "FtDatum{" +
                "ftId=" + ftId +
                ", t1Tid=" + t1Tid +
                ", t2Tid=" + t2Tid +
                ", fTypeId=" + fTypeId +
                ", langId=" + langId +
                ", fId=" + fId +
                '}';
    }
}
