package com.example.application.androidexamples;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/23/2017.
 */

public class CustomRequestParameterFactory {

    protected Context mCtx;
    private final String TAG = "=~_~=tijAmAtic=~_~=";


    Map<String, String> params;


    public CustomRequestParameterFactory(Context ctx){

        this.params = new HashMap<String, String>();

        params.put("_csrf", Constants.getInstance(mCtx).getCsrf());
        params.put("sid", Constants.getInstance(mCtx).getSid());


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
