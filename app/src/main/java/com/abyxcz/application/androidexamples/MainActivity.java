package com.abyxcz.application.androidexamples;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.CustomRequest;
import com.abyxcz.application.androidexamples.helper.Constants;
import com.abyxcz.application.androidexamples.helper.Statics;
import com.abyxcz.application.androidexamples.model.User;
import com.abyxcz.application.androidexamples.network.CustomRequestParameterFactory;
import com.abyxcz.application.androidexamples.network.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "=~_~=tijAmAtic=~_~=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Should have to check for a permission here?...
        ((ExampleApplication)getApplication()).setDeviceId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        get();
       // startSignIn();
    }


    /**
     * MENU
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Action based on menu item
        if (id == R.id.action_1) {
            Log.d("Menu Click", "Action 1 Clicked");
            mPager.setCurrentItem(0);
        }

        else if (id == R.id.action_2) {
            Log.d("Menu Click", "Action 2 Clicked");
            mPager.setCurrentItem(1);
        }

        else if (id == R.id.action_3) {
            Log.d("Menu Click", "Action 3 Clicked");
            mPager.setCurrentItem(2);
        }

        else if (id == R.id.action_4) {
            Log.d("Menu Click", "Action 4 Clicked");
            mPager.setCurrentItem(3);
        }

        else if (id == R.id.action_5) {
            Log.d("Menu Click", "Action 5 Clicked");
            mPager.setCurrentItem(4);
        }


        return true;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            finish();
            return;
        }

        else if(requestCode == Constants.REQUEST_LOGIN) {
            User u = ((ExampleApplication)getApplication()).getUser();
            Statics.toast(this, getResources().getString(R.string.login_welcome) + u.getUsername(), 0);
        }
    }

    /**
     * Pages
     */
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public void initPager(){
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new Adapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    /**
     * Navigation
     */

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void showView(int i){
        mPager.setCurrentItem(i);
    }

    public void showView1(View view){
        showView(0);
    }

    public void showView2(View view){
        showView(1);
    }

    public void showView3(View view){
        showView(2);
    }

    public void showView4(View view){
        showView(3);
    }

    public void showView5(View view){
        showView(4);
    }


    /**
     * POPUP
     */
    View popup;
    public void showPopup(View view){
      /*  RelativeLayout layout = (RelativeLayout) findViewById(R.id.content_root);

        popup = LayoutInflater.from(this).inflate(R.layout.activity_popup, null, false);

        layout.addView(popup);*/


        try{
            ((ViewStub) findViewById(R.id.stub_import)).setVisibility(View.VISIBLE);

        }catch(NullPointerException e){

            ((RelativeLayout) findViewById(R.id.panel_import)).setVisibility(View.VISIBLE);
        }
    }

    public void leavePopup(View view){
        /*RelativeLayout layout = (RelativeLayout) findViewById(R.id.content_root);

        layout.removeView(popup);*/

        try{
            ((ViewStub) findViewById(R.id.stub_import)).setVisibility(View.GONE);

        }catch(NullPointerException e){

            ((LinearLayout) findViewById(R.id.panel_import)).setVisibility(View.GONE);
        }
    }


    /**
     * LOGIN ACTIVITY
     */
    private void startSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_LOGIN);
    }


    /**
     * LOGIN
     * NETWORK REQUEST
     */

    public void login(String e, String p){

        String email = e;
        String password = p;

       /* Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("device_type", "G");*/
       // params.put("store_id", appId);

       CustomRequestParameterFactory f = new CustomRequestParameterFactory(this);
        f.addStringParam("email", email);
        f.addStringParam("password", password);

        Map<String, String> params = f.buildParams();


        Log.d(TAG, "LOGIN PARAMS" + params.toString());
        //showLoading();

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, Constants.loginUrl, params, ((ExampleApplication)(MainActivity.this.getApplication())).getSid(),  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                JSONObject jsonMainNode = response;

                try {

                    String status = response.getString("message");
                    ((ExampleApplication)(MainActivity.this.getApplication())).setCsrf(response.getString("csrf"));
                    ((ExampleApplication)(MainActivity.this.getApplication())).setSid(response.getString("set-cookie"));

                    //check response
                    if (status.toString().equals("success")) {
                        //////////////good///////////

                        //capture response info
                        JSONObject uInfo = jsonMainNode.getJSONObject("user");
                        Log.d(TAG, uInfo.toString());

                        User lUser = new User(uInfo);

                        ((ExampleApplication)(MainActivity.this.getApplication())).setUser(lUser);

                    }
                    //bad
                    else{
                        //Log and display error message
                        JSONObject message = response.getJSONObject("message");
                        Log.d(TAG, "Login Failure " + message.toString());

                    }
                    //hideLoading();

                }catch(JSONException e){
                    //Log and display error message
                    Statics.setAlert(MainActivity.this,"There was an error");
                    Log.d(TAG, "There was an aerror" + e);
                }
                //hide loading spinner
                //hideLoading();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
// TODO Auto-generated method stub
                // hideLoading();
                //throw alert about connecting to server
                Log.d(TAG, "couldn't connect....." + error.getMessage());
                //setAlert("There was an error = " + error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void get(){


        CustomRequestParameterFactory f = new CustomRequestParameterFactory(this);
        Map<String, String> params = f.buildParams();
        Log.d(TAG, "GET PARAMS" + params.toString());

        //showLoading();

        CustomRequest jsObjRequest = new CustomRequest(Constants.homeUrl, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                JSONObject responseNode = new JSONObject();//response;

                try {

                    String status = response.getString("message");
                    ((ExampleApplication)(MainActivity.this.getApplication())).setCsrf(response.getString("csrf"));
                    ((ExampleApplication)(MainActivity.this.getApplication())).setSid(response.getString("set-cookie"));

                    //check response
                    if (status.toString().equals("success")) {
                        //////////////good///////////

                        Log.d(TAG, "Success");
                       // login("test@test.com", "cookie");
                        startSignIn();

                    }
                    //bad
                    else{
                        //Log and display error message
                        JSONObject message = response.getJSONObject("message");
                        Log.d(TAG, "Login Failure " + message.toString());

                    }
                    //hideLoading();

                }catch(JSONException e){
                    //Log and display error message
                    Statics.setAlert(MainActivity.this,"There was an error");
                    Log.d(TAG, "There was an aerror" + e);
                }
                //hide loading spinner
                //hideLoading();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
// TODO Auto-generated method stub
                // hideLoading();
                //throw alert about connecting to server
                Log.d("new error", "couldn't connect....." + error.getMessage());
                //setAlert("There was an error = " + error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}
