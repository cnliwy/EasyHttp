package com.liwy.easyhttp.common;

import java.io.File;

/**
 * A file object which used to be upload
 * Created by liwy on 2017/6/14.
 */

public class EasyFile {
    // request key
    private String requestKey;
    // file name
    private String fileName;
    private String mediaType;
    private File file;

    public EasyFile(String requestKey, String fileName, String mediaType, File file) {
        this.requestKey = requestKey;
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
}
