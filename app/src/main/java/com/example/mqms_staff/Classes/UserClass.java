package com.example.mqms_staff.Classes;

import java.io.Serializable;

public class UserClass implements Serializable {
    String Role;
    String Name;
    String Email;
    String Department;
    String Counter;
    String imgSrc;
    String Password;

    public UserClass() {
    }

    public UserClass(String role, String name, String department, String counter, String imgSrc, String password) {
        Role = role;
        Name = name;
        Department = department;
        Counter = counter;
        this.imgSrc = imgSrc;
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
