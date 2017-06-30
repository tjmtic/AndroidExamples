package com.example.application.androidexamples;

/**
 * Created by Tim on 6/14/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_APPEND;

public class Page extends Fragment {

    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.page_layout, container, false);


        initGeocache();


        return rootView;
    }


    /**
     * GEOCACHE
     */

    //Permissions//
    private static final int REQUEST_FINE_LOCATION = 1;
    private static String[] LOCATIONS_STORAGE = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public LocationManager locationManager;
    public LocationListener locationListener;
    public Location currentLocation;

    public void initGeocache(){

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };



        //Set actions for buttons
        Button locationButton = (Button) rootView.findViewById(R.id.actionBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click","Clicked action button");
                //currentLocation = getCurrentLocation();
                setCurrentLocation(getCurrentLocation());
            }
        });

        Button writeButton = (Button) rootView.findViewById(R.id.writeFileBtn);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click","Clicked write button");
                saveCurrentLocation();
            }
        });

        Button readButton = (Button) rootView.findViewById(R.id.readFileBtn);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click","Clicked read button");
                readFromFile(filename);
            }
        });

    }

    public void setCurrentLocation(Location newLoc){

        this.currentLocation = newLoc;

        if(currentLocation != null) {
            TextView lat = (TextView) rootView.findViewById(R.id.latitude);
            TextView lon = (TextView) rootView.findViewById(R.id.longitude);

            lat.setText("Latitude: " + currentLocation.getLatitude());
            lon.setText("Longitude: " + currentLocation.getLongitude());
            Log.d("Location", "Latitude: " + currentLocation.getLatitude() + "Longitude: " + currentLocation.getLongitude());
        }

    }

    public void saveCurrentLocation(){

        if(currentLocation != null) {
            writeToFile(filename, "Latitude: " + currentLocation.getLatitude() + "Longitude: " + currentLocation.getLongitude());
        }
    }

    /**
     * Verify permissions for location and retrieve location
     * @return last received location from gps or network prodiver
     *          null on errors
     */
    public Location getCurrentLocation(){

        Location location = null;

        try {
            if (verifyLocationPermissions(getActivity())) {
            Log.d("Location", "Attempting");

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }


            } else {
                verifyLocationPermissions(getActivity());
            }

            Log.d("Location", location.toString());

        } catch(SecurityException e){

            Log.d("Security Exception", e.getMessage());

        }

        return location;

    }

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


    /**
     *
     * READ/WRITE
     *
     */

    public static String filename = "log.txt";

    public void writeToFile(String filename, String newText){

        try {
            FileOutputStream fos = getActivity().openFileOutput(filename, MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fos);
            outputWriter.write(newText);
            outputWriter.close();


            Toast.makeText(getActivity().getBaseContext(), "Successful write",
                    Toast.LENGTH_SHORT).show();

        } catch(FileNotFoundException e1) {

            try {
                File newFile = new File(filename);
                newFile.createNewFile();

                writeToFile(filename, newText);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String filename){

        try {
            FileInputStream fis = getActivity().openFileInput(filename);
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
            TextView text = (TextView) rootView.findViewById(R.id.text);
            text.setText(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}