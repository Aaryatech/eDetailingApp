package com.ats.edetailingapp.model;

public class FilteredFileNameModel {

    private String fileName;
    private int isDownloaded;

    public FilteredFileNameModel() {
    }

    public FilteredFileNameModel(String fileName, int isDownloaded) {
        this.fileName = fileName;
        this.isDownloaded = isDownloaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    @Override
    public String toString() {
        return "FilteredFileNameModel{" +
                "fileName='" + fileName + '\'' +
                ", isDownloaded=" + isDownloaded +
                '}';
    }
}
