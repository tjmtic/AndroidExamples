package com.example.application.androidexamples;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPager();
       // initGeocache();
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


    public void showMenuList(View view){
        showMenuList();
    }
    public void showMenuList(){

        View list = findViewById(R.id.activity_menu2);
        list.setVisibility(View.VISIBLE);
    }

    public void hideMenuList(){
        View list = findViewById(R.id.activity_menu2);
        list.setVisibility(View.GONE);
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

        hideMenuList();
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
     * GEOCACHE
     */

    //Permissions//
   /* private static final int REQUEST_FINE_LOCATION = 1;
    private static String[] LOCATIONS_STORAGE = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };*/

 /*   public LocationManager locationManager;
    public LocationListener locationListener;
    public Location currentLocation;

    public void initGeocache(){

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };



        Button locationButton = (Button) findViewById(R.id.actionBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

    }
*//*
    public void setCurrentLocation(){

       // this.currentLocation = getCurrentLocation();

        if(currentLocation != null) {
            TextView lat = (TextView) findViewById(R.id.latitude);
            TextView lon = (TextView) findViewById(R.id.longitude);

            lat.setText("Latitude: " + currentLocation.getLatitude());
            lon.setText("Longitude: " + currentLocation.getLongitude());


            writeToFile(filename, "Latitude: " + currentLocation.getLatitude() + "Longitude: " + currentLocation.getLongitude());
        }

    }
*/
    /**
     * Verify permissions for location and retrieve location
     * @return last received location from gps or network prodiver
     *          null on errors
     */
 /*
    public Location getCurrentLocation(){

        Location location = null;

        try {
            //if (verifyLocationPermissions(this)) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

            } else {
                verifyLocationPermissions(this);
            }
        } catch(SecurityException e){

            Log.d("Security Exception", e.getMessage());

        }

        return location;

    }
    */

/*
    public static boolean verifyLocationPermissions(Activity activity) {
        // Check if we have permission
        Log.d("GEOCACHE", "check permission");
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    LOCATIONS_STORAGE,
                    REQUEST_FINE_LOCATION
            );
        }

        else{
            return true;
        }

        return false;
    }
*/

    /**
     *
     * READ/WRITE
     *
     */

  //  public static String filename = "newfile.txt";
/*
    public void writeFile(View v){

        setCurrentLocation();
    }*/
/*
    public void writeToFile(String filename, String newText){

        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fos);
            outputWriter.write(newText);
            outputWriter.close();


            Toast.makeText(getBaseContext(), "Successful write",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(View v){
        readFromFile(filename);
    }
    public void readFromFile(String filename){

        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader InputRead= new InputStreamReader(fis);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();


            //Use value
            TextView text = (TextView) findViewById(R.id.text);
            text.setText(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/





}
