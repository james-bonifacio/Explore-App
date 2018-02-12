package com.example.james.exploreapp;

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
import java.util.Date;

public class RouteActivity extends AppCompatActivity {


    //settings
    private String city = "Toronto";
    private int start = 1518407985;
    private String TAG = RouteActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        ArrayList<Location> locations = this.getIntent().getParcelableArrayListExtra("Locations");

        SimpleDateFormat sdf = new SimpleDateFormat("2009-09-22 16:47:08");
        try {
            Date d = sdf.parse("21/12/2012");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String boi = createURL(city, locations.get(0).getName(), start);


        Log.i(TAG,"url = " + boi );



        new JsonTask1().execute(createURL(city, locations.get(0).getName(), start) );



    }

    private class JsonTask1 extends AsyncTask<String, String, String> {

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
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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

            String departure = null, duration = null, arrival = null;

            try {
                departure = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("departure_time").get("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                duration = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").get("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                arrival = (String)object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("arrival_time").get("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "departure=" + departure);
            Log.i(TAG, "duration=" + duration);
            Log.i(TAG, "arrival=" + arrival);


        }
    }//Json Task 1

    private String createURL(String origin, String destination, int departureTime) {

        try {

            origin = URLEncoder.encode(origin, "UTF-8");
            destination = URLEncoder.encode(destination, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&departure_time=" + departureTime + "&mode=transit&key=AIzaSyBU8yxgy0MpZD3DXkkf_c5Cm8fIEikM3i4";
    }

}
