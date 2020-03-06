package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileType {

    @SerializedName("ftype_id")
    @Expose
    private int ftypeId;
    @SerializedName("file_type")
    @Expose
    private String fileType;

    public int getFtypeId() {
        return ftypeId;
    }

    public void setFtypeId(int ftypeId) {
        this.ftypeId = ftypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "FileType{" +
                "ftypeId=" + ftypeId +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
