package com.example.mqms_staff.Classes;

public class Report_Class {

    private String custName,queueDate,queueStartTime,queueEndTime,transactionType,servedBy,feedback;

    public Report_Class(String custName, String queueDate, String queueStartTime, String queueEndTime, String transactionType, String servedBy, String feedback) {
        this.custName = custName;
        this.queueDate = queueDate;
        this.queueStartTime = queueStartTime;
        this.queueEndTime = queueEndTime;
        this.transactionType = transactionType;
        this.servedBy = servedBy;
        this.feedback = feedback;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(String queueDate) {
        this.queueDate = queueDate;
    }

    public String getQueueStartTime() {
        return queueStartTime;
    }

    public void setQueueStartTime(String queueStartTime) {
        this.queueStartTime = queueStartTime;
    }

    public String getQueueEndTime() {
        return queueEndTime;
    }

    public void setQueueEndTime(String queueEndTime) {
        this.queueEndTime = queueEndTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
