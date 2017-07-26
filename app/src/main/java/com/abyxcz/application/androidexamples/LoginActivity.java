package com.abyxcz.application.androidexamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.CustomRequest;
import com.abyxcz.application.androidexamples.helper.Constants;
import com.abyxcz.application.androidexamples.helper.OverlayMask;
import com.abyxcz.application.androidexamples.helper.Statics;
import com.abyxcz.application.androidexamples.model.User;
import com.abyxcz.application.androidexamples.network.CustomRequestParameterFactory;
import com.abyxcz.application.androidexamples.network.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * A login screen with email ad password
 */
public class LoginActivity extends Activity {

    private final String TAG = "=~_~=tijAmAtic=~_~=";

    private OverlayMask mOM;

    private EditText mUsernameView;
    private String mUsername;

    private EditText mPasswordView;
    private String mPassword;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mOM = new OverlayMask(this);


        ExampleApplication app = (ExampleApplication) getApplication();
        mUser = app.getUser();

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.login);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "attempted login click");
                attemptLogin();
            }
        });





        //Facebook Login Initialization

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * Finishing Call once User is set
     */
    protected void onLogin(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim().toLowerCase();
        String password = mPasswordView.getText().toString().trim();


        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            return;
        }

        mUsername = username;
        mPassword = password;

        // perform the user login attempt.
        login(mUsername, mPassword);
    }


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
        mOM.showLoading();

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, Constants.loginUrl, params, ((ExampleApplication)(LoginActivity.this.getApplication())).getSid(),  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                JSONObject jsonMainNode = response;

                try {

                    String status = response.getString("message");
                    ((ExampleApplication)(LoginActivity.this.getApplication())).setCsrf(response.getString("csrf"));

                    try {
                        ((ExampleApplication) (LoginActivity.this.getApplication())).setSid(response.getString("set-cookie"));
                    }catch(JSONException e){
                    }
                    //check response
                    if (status.toString().equals("success")) {
                        //////////////good///////////

                        //capture response info
                        JSONObject uInfo = jsonMainNode.getJSONObject("user");
                        Log.d(TAG, uInfo.toString());
                        User lUser = new User(uInfo);

                        //"Logging in"
                        ((ExampleApplication)(LoginActivity.this.getApplication())).setUser(lUser);

                        //back to app
                        onLogin();

                    }
                    //bad
                    else{
                        //Log and display error message
                        String message = response.getString("error");
                        Statics.toast(LoginActivity.this,"There was an error. " + message.toString(), 0);
                        Log.d(TAG, "Login Failure " + message.toString());
                    }
                    //hideLoading();

                }catch(JSONException e){
                    //Log and display error message
                    Statics.setAlert(LoginActivity.this,"There was an error. Please check your internet connection.");
                    Log.d(TAG, "There was an aerror" + e);
                }
                //hide loading spinner
                mOM.hideLoading();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
// TODO Auto-generated method stub
                // hideLoading();
                //throw alert about connecting to server
                Statics.setAlert(LoginActivity.this,"There was an error. Please check your internet connection.");

                Log.d(TAG, "couldn't connect....." + error.getMessage());
                //setAlert("There was an error = " + error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}



