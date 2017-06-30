package com.example.application.androidexamples;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tim on 6/29/2017.
 */

public class Statics {

    /**Alerts**/

    public static void setAlert(Activity activity, String alertMessage){

        final String am = alertMessage;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Alert")
                .setMessage(alertMessage)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("STATICS", "Alert: " + am);

                    }
                });

        builder.create().show();
    }

    public void setAlertWithTitle(Activity activity, String alertMessage, String title){

        final String am = alertMessage;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title)
                .setMessage(alertMessage)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("STATICS", "Alert: " + am);
                    }
                });

        builder.create().show();
    }

   /* public void setAlertWithCallback(Activity activity, String alertMessage, Callback cb){

        final String am = alertMessage;
        builder = new AlertDialog.Builder(activity);

        builder.setTitle("Alert")
                .setMessage(alertMessage)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "Alert: " + am);

                    }
                });

        builder.create().show();
    }*/


    /**
     * Uselessly Simplified Toasting
     * duration 0: short
     * duation 1: long
     */

    public static void toast(Activity activity, String message, int duration){

        switch(duration) {
            default:
                    Toast.makeText(activity.getBaseContext(), message, Toast.LENGTH_LONG).show();
                    break;
            case 0:
                    Toast.makeText(activity.getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    break;
        }
    }


}
