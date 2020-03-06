package com.ats.edetailingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectionDataModel {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("tag1")
    @Expose
    private Tag1 tag1;
    @SerializedName("tag2")
    @Expose
    private List<Tag2> tag2;
    @SerializedName("file_type")
    @Expose
    private List<FileType> fileType;
    @SerializedName("lang")
    @Expose
    private List<Lang> lang;
    @SerializedName("m_data")
    @Expose
    private List<MDatum> mData;
    @SerializedName("ft_data")
    @Expose
    private List<FtDatum> ftData;
    @SerializedName("file_data")
    @Expose
    private List<FileDatum> fileData;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Tag1 getTag1() {
        return tag1;
    }

    public void setTag1(Tag1 tag1) {
        this.tag1 = tag1;
    }

    public List<Tag2> getTag2() {
        return tag2;
    }

    public void setTag2(List<Tag2> tag2) {
        this.tag2 = tag2;
    }

    public List<FileType> getFileType() {
        return fileType;
    }

    public void setFileType(List<FileType> fileType) {
        this.fileType = fileType;
    }

    public List<Lang> getLang() {
        return lang;
    }

    public void setLang(List<Lang> lang) {
        this.lang = lang;
    }

    public List<MDatum> getMData() {
        return mData;
    }

    public void setMData(List<MDatum> mData) {
        this.mData = mData;
    }

    public List<FtDatum> getFtData() {
        return ftData;
    }

    public void setFtData(List<FtDatum> ftData) {
        this.ftData = ftData;
    }

    public List<FileDatum> getFileData() {
        return fileData;
    }

    public void setFileData(List<FileDatum> fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "SelectionDataModel{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", tag1=" + tag1 +
                ", tag2=" + tag2 +
                ", fileType=" + fileType +
                ", lang=" + lang +
                ", mData=" + mData +
                ", ftData=" + ftData +
                ", fileData=" + fileData +
                '}';
    }
}
