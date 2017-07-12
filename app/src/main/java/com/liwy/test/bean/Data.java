package com.liwy.test.bean;

/**
 * Created by liwy on 2017/6/21.
 */

public class Data {
    public String name;
    public int age;
    public String url;

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", url='" + url + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
