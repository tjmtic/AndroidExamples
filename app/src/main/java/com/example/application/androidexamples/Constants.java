package com.example.application.androidexamples;

import android.content.Context;

/**
 * Created by Tim on 6/29/2017.
 */

public class Constants {

    public final String google_geo_apikey ="AIzaSyDB7bm7x8SrkYPhxgStOvhomT80wlVV8_A";

    //public static final String loginUrl = "http://10.0.2.2:3000/"; //localhost:3000
    //public static String loginUrl = "https://maxecommerce.herokuapp.com/appLogin";

    private static String server = "https://tjmtic-node-examples.herokuapp.com/";

    public static final String loginUrl = server+"login"; //localhost:3000
    public static final String homeUrl = server+"logout";


    private static Context mCtx;
    private static Constants mInstance;

    private Constants(Context context) {
        mCtx = context;

    }

    public static synchronized Constants getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Constants(context);
        }
        return mInstance;
    }


    //Application Level Vars

    static String csrf = "";
    static String sid = "";


    public static void setCsrf(String s){
        csrf = s;
    }
    public String getCsrf(){
        return csrf;
    }

    public static void setSid(String s){
        sid = s;
    }

    public String getSid(){
        return sid;
    }
}
