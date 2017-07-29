package com.abyxcz.application.androidexamples;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.CustomRequest;
import com.abyxcz.application.androidexamples.helper.Constants;
import com.abyxcz.application.androidexamples.model.User;
import com.abyxcz.application.androidexamples.network.CustomRequestParameterFactory;
import com.abyxcz.application.androidexamples.network.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;


public class ExampleApplication extends android.app.Application {
    private String TAG = "~_~_~tjmtic~_~_~";


    private String mCsrf;
    private String mSid;

    private User mUser;

    private String mDeviceId;

    int channel = 5;
    private Socket mSocket;

        {

            try {
                //mSocket = IO.socket(Constants.CHAT_SERVER_URL);
                ////version 1
                IO.Options options = new IO.Options();
                options.path = "/socket.io";
                options.query = "channel="+channel;
                mSocket = IO.socket(Constants.CHAT_SERVER_URL, options);

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

    public Socket getSocket() {
        return mSocket;
    }

    public void setDeviceId(String s){
        mDeviceId = s;
    }
    public String getDeviceId(){
        return mDeviceId;
    }

    public void setUser(User u){
        mUser = u;
    }
    public User getUser() {
        return mUser;
    }

    public void setCsrf(String s){
        mCsrf = s;
    }
    public String getCsrf(){
        return mCsrf;
    }

    public void setSid(String s){
        mSid = s;
    }
    public String getSid(){
        return mSid;
    }

    /**
     * Initial GET call to server
     * Instantiates the client/server session
     *
     * Returns CSRF token, and SessionID token
    **/
    public void get(){

        CustomRequestParameterFactory f = new CustomRequestParameterFactory(this);
        Map<String, String> params = f.buildParams();

        CustomRequest jsObjRequest = new CustomRequest(Constants.HOME_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    //SET CUSTOM HEADERS
                    setCsrf(response.getString("csrf"));
                    setSid(response.getString("set-cookie"));

                }catch(JSONException e){
                    //Log and display error message
                    Log.d(TAG, "There was an aerror" + e);
                    get();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
// TODO Auto-generated method stub
                //throw alert about connecting to server
                Log.d("new error", "couldn't connect....." + error.getMessage());
                get();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}
