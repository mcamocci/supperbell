package com.haikarose.mediarose.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 2/2/17.
 */

public class ConnectionChecker{

    public static boolean isInternetConnected(Context context){

        ConnectivityManager connectivityManager=
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();

        if(info!=null && info.isConnectedOrConnecting()){
            return true;
        }
        return false;

    }
}