package com.liwy.test.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "model",strict = false)
public class Data {


    @Element(name = "sender",required = false)
    private String sender;


    /**
     * 任务集合数据
     */
    @Element(name = "usertaskid",required = false)
    private int usertaskid;


    @Element(name = "taskid",required = false)
    private int taskid;


    @Element(name = "createddate",required = false)
    private String createddate;


    @Element(name = "datetime",required = false)
    private String datetime;


    @Element(name = "task",required = false)
    private String task;


    @Element(name = "taskname",required = false)
    private String taskname;



    @Element(name = "username",required = false)
    private String username;


    @Override
    public String toString() {
        return "Data{" +
                "sender='" + sender + '\'' +
                ", usertaskid=" + usertaskid +
                ", taskid=" + taskid +
                ", createddate='" + createddate + '\'' +
                ", datetime='" + datetime + '\'' +
                ", task='" + task + '\'' +
                ", taskname='" + taskname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getUsertaskid() {
        return usertaskid;
    }

    public void setUsertaskid(int usertaskid) {
        this.usertaskid = usertaskid;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
