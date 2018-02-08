package com.example.james.exploreapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button btnNext;
    private String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String city = "toronto";

                new NetworkThread().execute(city);

            }
        });
    }

    private class NetworkThread extends AsyncTask<String, String, String> {

        String words;


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            //ArrayList<Location> locations = getLocations(getUrl(params[0]));
            ArrayList<Location> locations = getLocations("https://www.tripadvisor.ca/Attractions-g155019-Activities-Toronto_Ontario.html");

            for (Location location : locations) {

                Log.i(TAG,location.getName());

            }

            return null;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



        }

        protected ArrayList<Location> getLocations(String url) {

            ArrayList<Location> locations = new ArrayList<Location>();

            try {

                Document doc = Jsoup.connect(url).get();
                words = doc.text();

                Elements names = doc.select("div.listing_title > a");

                for (Element name : names) {
                    Location l = new Location();
                    l.setName(name.text());
                    locations.add(l);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return locations;
        }//end getLocations

        protected String getUrl(String requestUrl) {

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

            JSONObject object = null;
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = null;

            try {
                url = (String)object.getJSONArray("items").getJSONObject(0).get("link");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return url;


        }//end getUrl

    }//end Network Thread




    private String getRequestUrl(String city) {
        return "https://www.googleapis.com/customsearch/v1?q="+city+"+things+to+do&cx=005489561495639641028%3Aallb65ukzxo&num=1&key=AIzaSyBoiVEvK5X5IIgfdHDkFJZYYKaQzYi4Bsg";
    }






}
