package com.liwy.test.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * Created by liwy on 2017/6/20.
 */
@Root(strict = false)
public class LoginResponse {
    @Element(name = "success",required = false)
    public String success;
    @Element(name = "errormessage",required = false)
    public String errorMessage;

    // 医院的logo图片地址
    @Element(name = "icon",required = false)
    public String icon;

    // 全名
    @Element(name = "fullname",required = false)
    public String fullName;

    //角色/职务
    @Element(name = "usergroupname",required = false)
    public String jobTitle;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success='" + success + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", icon='" + icon + '\'' +
                ", fullName='" + fullName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
