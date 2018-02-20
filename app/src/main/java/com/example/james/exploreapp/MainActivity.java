package com.example.james.exploreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;
    private String TAG = MainActivity.class.getName();
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = "toronto";



        btnNext = (Button)findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = "montreal";
                new NetworkThread(MainActivity.this, view.getContext()).execute(input);

            }
        });
    }

    private class NetworkThread extends AsyncTask<String, String, ArrayList<Location>> {

        private ProgressDialog dialog;
        Context context;

        public NetworkThread(MainActivity activity, Context context) {
            dialog = new ProgressDialog(activity);
            this.context = context.getApplicationContext();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading, please wait...");
            dialog.show();
        }

        protected ArrayList<Location> doInBackground(String... params) {


            String placeUrl = getPlaceUrl("toronto");
            Log.i(TAG, placeUrl);
            ArrayList<Location> locations = getLocations(placeUrl);
            //ArrayList<Location> locations = getLocations("https://www.tripadvisor.ca/Attractions-g155019-Activities-Toronto_Ontario.html");

            for (Location l : locations) {

                Log.i(TAG, "name=" + l.getName() +" tagline=" + l.getTagLine());

            }

            return locations;


        }

        @Override
        protected void onPostExecute(ArrayList<Location> locations) {
            super.onPostExecute(locations);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Intent i = new Intent(context, SwipeActivity.class);
            i.putParcelableArrayListExtra("Locations", locations);
            i.putExtra("City", city);

            startActivity(i);

        }

        protected ArrayList<Location> getLocations(String requestUrl) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;

            try {

                URL url = new URL(requestUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                result = buffer.toString();

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

            //parse JSON
            JSONObject object = null;
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray results = null;

            try {
                results = object.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ArrayList<Location> locations = new ArrayList<>();

            for (int i=0; i<results.length(); i++) {

                JSONObject res = null;

                try {
                    res = results.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String name = null, rating = null, tagline = null, photoRef = null;

                try {
                    name = res.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    rating = Double.toString(res.getDouble("rating"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    tagline = res.getJSONArray("types").getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    photoRef = res.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String img = getImgUrl(photoRef);

                Location l = new Location(name, img, tagline, rating);

                locations.add(l);
            }


            return locations;


        }//end getUrl

    }//end Network Thread


    private String getPlaceUrl(String city) {
        try {
            return "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+ URLEncoder.encode(city, "UTF-8") +"+things+to+do&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getImgUrl (String locationRef) {

        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1080&photoreference=" + locationRef + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";
    }

    private JSONObject getJson (String requestUrl) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;

        try {

            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            result = buffer.toString();

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

        //parse JSON
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
