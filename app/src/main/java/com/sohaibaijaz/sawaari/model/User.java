package com.sohaibaijaz.sawaari.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {




    @SerializedName("status")
    private int status;
    @SerializedName("first_name")
    private  String firstName;
    @SerializedName("last_name")
    private  String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }






}
