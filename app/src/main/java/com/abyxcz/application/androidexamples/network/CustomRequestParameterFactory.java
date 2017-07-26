package com.abyxcz.application.androidexamples.network;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.abyxcz.application.androidexamples.ExampleApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/23/2017.
 */

public class CustomRequestParameterFactory {

    protected Context mCtx;
    private final String TAG = "=~_~=tijAmAtic=~_~=";


    Map<String, String> params;


    public CustomRequestParameterFactory(Application ctx){

        this.params = new HashMap<String, String>();

        ExampleApplication app = (ExampleApplication)ctx;

        params.put("_csrf", app.getCsrf());
        params.put("sid", app.getSid());


    }

    public CustomRequestParameterFactory(Context ctx){

        this.params = new HashMap<String, String>();

        ExampleApplication app = (ExampleApplication)((Activity)ctx).getApplication();

        params.put("_csrf", app.getCsrf());
        params.put("sid", app.getSid());


    }


    public void addStringParam(String name, String value){

      //  try{
            params.put(name, value);
      /*  }
        catch(JSONException e){
            Log.d(TAG, "CustomRequestParameterFactory Json Exception:" + e.getMessage());
            return false;
        }
       */

    }

    public Map buildParams(){
        return params;
    }
}
