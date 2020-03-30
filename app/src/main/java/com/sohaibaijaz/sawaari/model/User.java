package com.sohaibaijaz.sawaari.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

 private static User instance;

 private User(){

 }

 public static User getInstance(){

     if(instance==null){
         instance= new User();
     }
     return instance;
 }


    @SerializedName("status")
    private int status;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("email")
    private String email;

    @SerializedName("last_name")
    private String lastName;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @SerializedName("phone_number")
    private String phoneNumber;

//    @SerializedName("status")
//    private static int status;
//    @SerializedName("first_name")
//    private  static String firstName;
//    @SerializedName("last_name")
//    private static String lastName;
//    @SerializedName("email")
//    private static String email;
//
//    @SerializedName("phone_number")
//    private static String phoneNumber;
//
//    public User() {
//    }
//
//    public static int getStatus() {
//        return status;
//    }
//
//    public static void setStatus(int status) {
//        User.status = status;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        User.firstName = firstName;
//    }
//
//    public static String getLastName() {
//        return lastName;
//    }
//
//    public static void setLastName(String lastName) {
//        User.lastName = lastName;
//    }
//
//    public static String getEmail() {
//        return email;
//    }
//
//    public static void setEmail(String email) {
//        User.email = email;
//    }
//
//    public static String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public static void setPhoneNumber(String phoneNumber) {
//        User.phoneNumber = phoneNumber;
//    }



}
