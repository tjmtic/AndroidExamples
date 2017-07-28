package com.abyxcz.application.androidexamples.helper;

/**
 * Created by Tim on 6/29/2017.
 */

public class Constants {

    /**
     * API KEYS
     */
    public final String google_geo_apikey ="AIzaSyDB7bm7x8SrkYPhxgStOvhomT80wlVV8_A";


    /**
     * SERVER ACCESS
     */
    public static final String server = "http://10.0.2.2:3003/"; //localhost:3000
    public static final String chatServer = "http://10.0.2.2:3003/"; //chat


    /**
     * NETOWRK REQUESTS
     */


    //LOGINS
    //public static final String loginUrl = server+"appLogin"; //localhost:3000
    public static final String LOGIN_URL = server+"users/app/login/"; //localhost:3000
    public static final String OAUTH_FACEBOOK_LOGIN_URL = server+"users/app/auth/facebook/";

    public static final String CHAT_SERVER_URL = chatServer;

    public static final String HOME_URL = server+"users/app/";


    /**
     * REQUEST CODES
     */

        public static int REQUEST_LOGIN = 0;











    /*
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
*/


}
