package com.liwy.test.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 网络请求返回数据基类
 * Created by liwy on 2017/4/7.
 */
@Root(strict = false)
public class BaseResult {
    @Element(name = "success",required = false)
    public String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
