package com.example.mqms_staff.Classes;

import androidx.annotation.NonNull;

public class appointment {
    private String name;
    private String phoneNumber;
    private String appointmentTime;
    private String reasonOfVisit;

    public appointment(String name, String phoneNumber, String appointmentTime, String reasonOfVisit) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.appointmentTime = appointmentTime;
        this.reasonOfVisit = reasonOfVisit;
    }

    public appointment() {
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(@NonNull String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReasonOfVisit() {
        return reasonOfVisit;
    }

    public void setReasonOfVisit(@NonNull String reasonOfVisit) {
        this.reasonOfVisit = reasonOfVisit;
    }

}
