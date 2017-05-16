package com.haikarose.primepost.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by root on 5/15/17.
 */

public class PermissionHelper {

    static String[] PERMISSION_OTHER={Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void check(Activity activity, String ... PERMISSIONS){
        int PERMISSION_ALL = 1;
        if(!PermissionHelper.hasPermissions(activity.getBaseContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(activity,PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static void checkOther(Activity activity){
        int PERMISSION_ALL=1;
        if(!PermissionHelper.hasPermissions(activity.getBaseContext(),PERMISSION_OTHER)){
            ActivityCompat.requestPermissions(activity,PERMISSION_OTHER, PERMISSION_ALL);
        }
    }
}
