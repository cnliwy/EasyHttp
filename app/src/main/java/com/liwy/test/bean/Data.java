package com.liwy.test.bean;

import java.util.List;

/**
 * Created by liwy on 2017/6/21.
 */

public class Data {
    public String name;
    public int age;
    public String url;
    public String title;
    public List<Test> tests;

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
