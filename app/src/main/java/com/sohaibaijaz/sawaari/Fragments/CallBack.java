package com.sohaibaijaz.sawaari.Fragments;

public interface CallBack {
    void onSuccess(String status_code, String message);
    void onFailure(String status_code, String message);
}

