package com.example.aashankpratap.selfieapplication;

import android.util.Log;

/**
 * Created by AASHANK PRATAP on 3/13/2016.
 */
public class CustomGridImageViewObject {

    private String imageUrl ;
    private boolean state ;

    public static final String TAG = "SelfieApplication.CustomGridViewObject" ;

    public CustomGridImageViewObject(String name, boolean state) {
        super();
        imageUrl = name ;
        this.state = state ;
        Log.d(TAG, "CustomGridImageViewObject");
    }

    public String getName() {
        Log.d(TAG, "getName");
        return imageUrl;
    }

    public void setName(String name) {
        Log.d(TAG, "setName");
        imageUrl = name ;
    }

    public boolean getState() {
        Log.d(TAG, "getState");
        return state;
    }

    public void setState(boolean state) {
        Log.d(TAG, "setState");
        this.state = state;
    }

}
