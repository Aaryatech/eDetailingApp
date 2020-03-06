package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lang {


    @SerializedName("lang_id")
    @Expose
    private int langId;
    @SerializedName("lang_name")
    @Expose
    private String langName;

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    @Override
    public String toString() {
        return "Lang{" +
                "langId=" + langId +
                ", langName='" + langName + '\'' +
                '}';
    }
}
