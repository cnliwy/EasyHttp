package com.liwy.test.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liwy on 2017/6/5.
 */

public class TestResult {
    private int code;
    private int required;
    private String version;

    @SerializedName("releasednote")
    private String releasedNote;

    @SerializedName("releaseddate")
    private String releasedDate;

    @Override
    public String toString() {
        return "TestResult{" +
                "code=" + code +
                ", required=" + required +
                ", version='" + version + '\'' +
                ", releasedNote='" + releasedNote + '\'' +
                ", releasedDate='" + releasedDate + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleasedNote() {
        return releasedNote;
    }

    public void setReleasedNote(String releasedNote) {
        this.releasedNote = releasedNote;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }
}
