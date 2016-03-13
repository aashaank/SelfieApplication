package com.example.aashankpratap.selfieapplication;

import android.os.Environment;
import android.provider.ContactsContract;

import java.io.File;

/**
 * Created by AASHANK PRATAP on 3/13/2016.
 */
public class Constant {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    /*public static final int CAPTURE_VEDIO_ACTIVITY_REQUEST_CODE = 200;*/
    public static final int MEDIA_TYPE_IMAGE = 1;
    /*public static final int MEDIA_TYPE_VEDIO = 2;*/

    public static final File IMAGEPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    public static final String IMAGEFOLDERNAME = "SelfieApplication";
}
