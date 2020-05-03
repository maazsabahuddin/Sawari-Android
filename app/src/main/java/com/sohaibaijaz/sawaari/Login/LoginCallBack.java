package com.sohaibaijaz.sawaari.Login;

public interface LoginCallBack {
    void onSuccess(String status_code, String message, String token);
    void onFailure(String status_code, String message);
}
