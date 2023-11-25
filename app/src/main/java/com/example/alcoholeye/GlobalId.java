package com.example.alcoholeye;

import android.app.Application;

public class GlobalId extends Application {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
