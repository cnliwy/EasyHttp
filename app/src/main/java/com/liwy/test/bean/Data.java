package com.liwy.test.bean;

/**
 * Created by liwy on 2017/6/21.
 */

public class Data {
    public String name;
    public int age;

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
