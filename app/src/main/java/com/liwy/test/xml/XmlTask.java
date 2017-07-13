package com.liwy.test.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;

import java.util.List;

/**
 * Created by liwy on 2017/3/30.
 */
public class XmlTask extends BaseResult{

    @Path("result")
    @ElementList(inline = true,required = false,entry = "model")
    public List<Data> dataList;


    @Override
    public String toString() {
        return "XmlTask{" +
                "success='" + success + '\'' +
                ", dataList=" + (dataList != null ? dataList.size() : 0) +
                '}';
    }
}
