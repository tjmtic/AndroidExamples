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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * A login screen with email ad password
 */
public class LoginActivity extends Activity {

    private final String TAG = "=~_~=tijAmAtic=~_~=";

    private OverlayMask mOM;

    CallbackManager callbackManager;

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
        initFacebookLogin();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
     * Initialize Facebook OAuth Login
     *
     */
    public void initFacebookLogin(){


        if(AccessToken.getCurrentAccessToken().getCurrentAccessToken() != null){
            //Already Logged In with Facebook Token
            //Send login request to server
            final String token = AccessToken.getCurrentAccessToken().getToken();
            final String pid = AccessToken.getCurrentAccessToken().getUserId();
            oauthFacebookLogin(token, "", pid);

        }

        else {
            //Not Logged In with Facebook
            //Initialize Facebook Login actions
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code

                            final String token = loginResult.getAccessToken().getToken();
                            final String pid = loginResult.getAccessToken().getUserId();

                            new GraphRequest(
                                    //loginResult.getAccessToken().getToken(),
                                    AccessToken.getCurrentAccessToken().getCurrentAccessToken(),
                                    "/me?fields=id,name,email",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {

                                            Log.d(TAG, response.toString());
                                            JSONObject responseJSON = response.getJSONObject();
                                            Log.d(TAG, responseJSON.toString());

                                            try {
                                                String fName = responseJSON.getString("name").split(" ", 2)[0];
                                                String lName = responseJSON.getString("name").split(" ", 2)[1];
                                                String email = responseJSON.getString("email");

                                                oauthFacebookLogin(token, email, pid);

                                            } catch (JSONException e) {
                                                Log.e(TAG, "FACEBOOK RESPONSE ERROR:" + e.getMessage());
                                            }

                                        }
                                    }
                            ).executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            Log.d(TAG, "Facebo0k Login Cancel");
                            Statics.toast(LoginActivity.this, "Facebook Login Cancelled", 0);
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.d(TAG, "Facebo0k Login Exception:" + exception.toString());
                            Statics.toast(LoginActivity.this, "Facebook Login Error", 0);
                        }
                    });

        }


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

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, Constants.LOGIN_URL, params, ((ExampleApplication)(LoginActivity.this.getApplication())).getSid(),  new Response.Listener<JSONObject>() {
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

    public void oauthFacebookLogin(String t, String e, String p){

        String token = t;
        String email = e;
        String pid = p;

       /* Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("device_type", "G");*/
        // params.put("store_id", appId);

        CustomRequestParameterFactory f = new CustomRequestParameterFactory(this);
        f.addStringParam("token", token);
        f.addStringParam("email", email);
        f.addStringParam("pid", pid);

        Map<String, String> params = f.buildParams();


        Log.d(TAG, "FACEBOOK LOGIN PARAMS" + params.toString());
        mOM.showLoading();

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, Constants.OAUTH_FACEBOOK_LOGIN_URL, params, ((ExampleApplication)(LoginActivity.this.getApplication())).getSid(),  new Response.Listener<JSONObject>() {
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



