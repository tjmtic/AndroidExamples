package com.example.application.androidexamples.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TMc on 4/11/16.
 */
public class User {

    public String id;
    public String email;
    public String avatar;
    public String first_name;
    public String last_name;
    public String contact;
    public String[] permissions;


    public User(){

        String id1 = "-1";
        String[] permissions1 = new String[0];
        String lname1 = "";
        String fname1 = "";
        String email1 = "";
        String avatar1 = "";
        String contact1 = "";



        this.id = id1;
        this.permissions = permissions1;
        this.email = email1;
        this.avatar = avatar1;
        this.first_name = fname1;
        this.last_name = lname1;
        this.contact = contact1;

    }


    public User(JSONObject userBean){
        String id1 = "-1";
        String lname1 = "";
        String fname1 = "";
        String email1 = "";
        String avatar1 = "";
        String contact1 = "";





            try{
                id1=userBean.getString("_id");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }

            try{
                fname1=userBean.getString("first_name");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }

            try{
                lname1=userBean.getString("last_name");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }

            try{
                email1=userBean.getString("email");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }

            try{
                avatar1=userBean.getString("avatar");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }

            try{
                contact1=userBean.getString("phone");
            }catch(JSONException e){
                Log.d("JSONEXCEPTION", e.getMessage());
            }




        this.id = id1;
        this.email = email1;
        this.avatar = avatar1;
        this.first_name = fname1;
        this.last_name = lname1;
        this.contact = contact1;

    }

    public String getName() {
        return first_name + " " + last_name;
    }

    public String getUsername() {
        return email;
    }


}
