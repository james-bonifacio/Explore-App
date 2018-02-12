package com.example.james.exploreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class RouteActivity extends AppCompatActivity {

    //settings
    private int time = 1517942615;
    private int timeAtLocation = 7200;
    private int locationsToAdd = 3;
    private String mode = "transit";
    private String startLocation;


    private String TAG = RouteActivity.class.getName();
    private int numScheduled;
    private String currOrigin;

    private String[] departure = new String[locationsToAdd];
    private String[] duration = new String[locationsToAdd];
    private String[] arrival = new String[locationsToAdd];

    private ArrayList<Location> locations;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        dialog = new ProgressDialog(RouteActivity.this);
        dialog.setMessage("Loading, please wait...");
        dialog.show();

        locations = this.getIntent().getParcelableArrayListExtra("Locations");
        Collections.shuffle(locations);

        numScheduled = 0;

        startLocation  = (String) this.getIntent().getExtras().get("City");

        currOrigin = startLocation;

        String url = createURL(startLocation, locations.get(0).getName(), time);

        new DirectionTask().execute( url );

    }

    private class DirectionTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject object = null;
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String currDeparture = null, currDuration = null, currArrival = null;
            int currTime = 0;

            try {
                currDeparture = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("departure_time").get("text");
            } catch (JSONException e) {
                //e.printStackTrace();
            }


            try {
                currDuration = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").get("text");
            } catch (JSONException e) {
                //e.printStackTrace();
            }


            try {
                currArrival = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("arrival_time").get("text");
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            try {
                currTime = (int)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("arrival_time").get("value");
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            Log.i(TAG, "departure=" + currDeparture);
            Log.i(TAG, "duration=" + currDuration);
            Log.i(TAG, "arrival=" + currArrival);

            //if route exists
            if ( (currDeparture != null) && (currDuration != null) && (currArrival != null) ) {

                Log.i(TAG, "Route from " + currOrigin + " to " + locations.get(0).getName() + " exists.");

                currOrigin = locations.get(0).getName();

                departure[numScheduled] = currDeparture;
                duration[numScheduled] = currDuration;
                arrival[numScheduled] = currArrival;

                time = currTime + timeAtLocation;

                numScheduled++;

            } else {

                Log.i(TAG, "Route from " + currOrigin + " to " + locations.get(0).getName() + " doesnt exist.");

            }

            locations.remove(0);

            //if arraylist not empty and need more locations
            if ( (!(locations.isEmpty())) && (numScheduled < locationsToAdd) ) {

                String url = createURL(currOrigin, locations.get(0).getName(), time);

                new DirectionTask().execute( url );

            } else {
                //finished

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }


        }
    }//Json Task 1

    private String createURL(String origin, String destination, int departureTime) {

        try {

            origin = URLEncoder.encode(origin, "UTF-8");
            destination = URLEncoder.encode(destination, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&departure_time=" + departureTime + "&mode=" + mode + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";

        Log.i(TAG,"url = " + url );

        return url;
    }

}