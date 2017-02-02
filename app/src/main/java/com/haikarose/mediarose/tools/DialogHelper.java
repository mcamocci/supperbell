package com.haikarose.mediarose.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by root on 2/1/17.
 */

public class DialogHelper {

    public static void showNetDialog(final Context context,String title,String message){

        final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);

        //Set the Yes/Positive and No/Negative Button text
        String yesButtonText = "Yes";
        String noButtonText = "No";
        //Define the positive button text and action on alert dialog
        adb.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something to be done here
                /*Intent intent=new Intent(context, SendingLogIntentService.class);
                context.startService(intent);*/

            }
        });

        //Define the negative button text and action on alert dialog
        adb.setNegativeButton(noButtonText, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something else to be also done here
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();
    }
}
