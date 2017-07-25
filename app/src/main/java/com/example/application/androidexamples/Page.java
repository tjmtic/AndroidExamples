package com.example.application.androidexamples;

/**
 * Created by Tim on 6/14/2017.
 */

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;

import com.example.application.androidexamples.helper.Statics;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_APPEND;

public class Page extends Fragment implements OnMapReadyCallback, LocationListener, PlaceSelectionListener {

    public String TAG = "~_~map-gps-place-loc~_~";

    ViewGroup rootView;

    EditText addressView;
    EditText cityView;
    EditText stateView;
    EditText zipCodeView;
    EditText countryView;

    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.locations, container, false);

        init();

        return rootView;
    }


    /**
     * init
     */


    public LocationManager locationManager;
    public LocationListener locationListener;
    public Location currentLocation;
    public Location location;

    public void init(){

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currentLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };


        //Get map placeholder and replace with fragment
        MapFragment fragment = new MapFragment();
        FragmentManager manager = getActivity().getFragmentManager();
        //Avoid crash upon re-initializing
        manager.beginTransaction()
                .replace(R.id.map, fragment)
                .commit();
        fragment.getMapAsync(this);


        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);



        //Set actions for buttons
        Button locationButton = (Button) rootView.findViewById(R.id.actionBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveLocation(getActiveLocation());
                setLocation(getActiveLocation());
                showLocation(location, "T:"+location.getTime());
            }
        });



        //EditText for Address Info
        addressView = (EditText) rootView.findViewById(R.id.address);
        cityView = (EditText) rootView.findViewById(R.id.city);
        stateView = (EditText) rootView.findViewById(R.id.state);
        zipCodeView = (EditText) rootView.findViewById(R.id.zip);
        countryView = (EditText) rootView.findViewById(R.id.country);

    }



    @Override
    public void onMapReady(GoogleMap map) {

        this.map = map;

    }

    public void showOnMap(LatLng lalo, String title){
        map.addMarker(new MarkerOptions()
                .position(lalo)
                .title(title));

        //map.setMinZoomPreference(6.0f);
        //map.setMaxZoomPreference(14.0f);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lalo, 10));
    }

    public void showLocation(Location loc, String title){

        LatLng lalo = new LatLng(loc.getLatitude(), loc.getLongitude());

        showOnMap(lalo, title);

    }

    public void setLocation(Location loc){

        this.location = loc;

    }

    public Location getLocation(){

        return location;
    }

    public void setCurrentLocation(Location loc){
        this.currentLocation = loc;
    }

    public Location getCurentLocation(){
        return currentLocation;
    }

    public Location getActiveLocation(){

        Location location = null;

        try {
            if (Statics.verifyLocationPermissions(getActivity())) {

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                Log.d("Location", location.toString());


            } else {
                Statics.verifyLocationPermissions(getActivity());
            }


        } catch(SecurityException e){

            Log.d("Security Exception", e.getMessage());

        }

        return location;

    }

    public void setActiveLocation(Location newLoc){

        this.location = newLoc;

        if(location != null) {
            TextView lat = (TextView) rootView.findViewById(R.id.latitude);
            TextView lon = (TextView) rootView.findViewById(R.id.longitude);

            lat.setText("Latitude: " + location.getLatitude());
            lon.setText("Longitude: " + location.getLongitude());
            Log.d("Location", "Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();

        Statics.toast(getActivity(), "Location Updated", 0);

        Log.d(TAG, msg);
    }



    /**Place**/

    Place place;

    public void setPlace(Place place){

        this.place = place;

    }

    public Place getPlace(){

        return place;

    }

    public void showPlace(Place place){

        LatLng lalo = place.getLatLng();

        showOnMap(lalo, place.getName().toString());

    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {

        try {

            setPlace(place);
            setAddressText(place);
            showPlace(place);

        }
        catch(ArrayIndexOutOfBoundsException e){
            Log.d(TAG, "There was a problem with the address, please check the information.");
        }
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Statics.setAlert(getActivity(), "Place selection failed: " + status.getStatusMessage());
    }



    /**Address**/

    public void setAddressText(Place place){
        String[] addy = place.getAddress().toString().split(",");

        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        addressView.setText(addy[0]);
        cityView.setText(addy[1]);
        String[] temp = addy[2].split(" ");
        stateView.setText(temp[1]);
        zipCodeView.setText(temp[2]);
        countryView.setText(addy[3]);
    }

    public String[] getAddressText(){

        String[] addy = new String[4];

        addy[0] = addressView.getText().toString();
        addy[1] = cityView.getText().toString();
        addy[2] = stateView.getText().toString();
        addy[2] += " " + zipCodeView.getText();
        addy[3] = countryView.getText().toString();

        return addy;

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