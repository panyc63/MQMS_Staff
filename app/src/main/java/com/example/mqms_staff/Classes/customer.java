package com.example.mqms_staff.Classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class customer implements Serializable {

    private String name;
    private String nric;
    private String queueNo;
    private String queueType;
    private String token;
    private Boolean acknowledge;


    public customer() {
    }

    public customer(String name, String nric, String queueNo,String token , Boolean acknowledge) {
        this.name = name;
        this.nric = nric;
        this.queueNo = queueNo;
        this.token = token;
        this.acknowledge = acknowledge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(Boolean acknowledge) {
        this.acknowledge = acknowledge;
    }
}
