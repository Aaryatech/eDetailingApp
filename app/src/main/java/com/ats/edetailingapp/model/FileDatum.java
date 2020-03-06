package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileDatum {

    @SerializedName("f_id")
    @Expose
    private int fId;
    @SerializedName("f_title")
    @Expose
    private String fTitle;
    @SerializedName("f_path")
    @Expose
    private String fPath;
    @SerializedName("f_full_path")
    @Expose
    private String fFullPath;
    @SerializedName("is_active")
    @Expose
    private int isActive;

    private String fType;

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public String getfTitle() {
        return fTitle;
    }

    public void setfTitle(String fTitle) {
        this.fTitle = fTitle;
    }

    public String getfPath() {
        return fPath;
    }

    public void setfPath(String fPath) {
        this.fPath = fPath;
    }

    public String getfFullPath() {
        return fFullPath;
    }

    public void setfFullPath(String fFullPath) {
        this.fFullPath = fFullPath;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    @Override
    public String toString() {
        return "FileDatum{" +
                "fId=" + fId +
                ", fTitle='" + fTitle + '\'' +
                ", fPath='" + fPath + '\'' +
                ", fFullPath='" + fFullPath + '\'' +
                ", isActive=" + isActive +
                ", fType='" + fType + '\'' +
                '}';
    }
}
