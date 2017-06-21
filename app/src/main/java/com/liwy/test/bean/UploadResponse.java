package com.liwy.test.bean;

import java.util.List;

/**
 * Created by liwy on 2017/6/21.
 */

public class UploadResponse {
    private int code;
    private String fileUrl;
    private List<Data> datas;

    @Override
    public String toString() {
        return "UploadResponse{" +
                "code=" + code +
                ", fileUrl='" + fileUrl + '\'' +
                ", datas=" + datas +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }
}
