package com.ats.edetailingapp.model;

public class FileTypeListModel {

    private int fileTypeId;
    private String fileType;

    public int getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(int fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "FileTypeListModel{" +
                "fileTypeId=" + fileTypeId +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
