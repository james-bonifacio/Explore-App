package com.example.james.exploreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

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

public class DescriptionActivity extends AppCompatActivity {

    private ExpandableTextView expandableTextView;
    private TextView tvName, tvRating, tvNumReviews, tvOpen, tvAddress, tvPhoneNumber, tvWebsite;
    private RatingBar rbRating;

    private String placeId;

    //from Places api
    private String name, rating, address, phoneNumber, website;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> openingHours = new ArrayList<>();
    private Boolean openNow;

    //from web scrape
    private String description, numReviews, suggestedDuration;
    private ArrayList<String> categories = new ArrayList<>();


    private String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Location location = getIntent().getExtras().getParcelable("Location");


        name = location.getName();
        rating = location.getRating();
        placeId = location.getPlaceId();

        new NetworkThread(DescriptionActivity.this, getApplicationContext()).execute();


    }

    private class NetworkThread extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;
        Context context;

        public NetworkThread(DescriptionActivity activity, Context context) {
            dialog = new ProgressDialog(activity);
            this.context = context.getApplicationContext();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading, please wait...");
            dialog.show();
        }

        protected String doInBackground(String... params) {


            //get data from places api
            String placeDetailsUrl = generatePlaceDetailsUrl(placeId);
            parsePlaceDetails(placeDetailsUrl);
            String customSearchUrl = generateCustomSearchUrl(name);
            String webPageUrl = getWebPageUrl(customSearchUrl);
            scrapeWebPage(webPageUrl);
            return null;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            displayData();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }


    }//end Network Thread

    private void displayData() {

        tvName = (TextView) findViewById(R.id.name_text_view);
        tvRating = (TextView) findViewById(R.id.rating_text_view);
        tvNumReviews = (TextView) findViewById(R.id.number_reviews_text_view);
        tvOpen = (TextView) findViewById(R.id.open_text_view);
        tvAddress = (TextView) findViewById(R.id.address_text_view);
        tvPhoneNumber = (TextView) findViewById(R.id.phone_number_text_view);
        tvWebsite = (TextView) findViewById(R.id.website_text_view);

        tvName.setText(name);
        tvRating.setText(rating);
        tvNumReviews.setText(numReviews);
        tvOpen.setText(openNow ? "OPEN NOW" : "CLOSED NOW");
        tvAddress.setText(address);
        tvPhoneNumber.setText(phoneNumber);
        tvWebsite.setText(website);

        expandableTextView = (ExpandableTextView)findViewById(R.id.expandable_text_view);
        expandableTextView.setText(description);

        rbRating = (RatingBar) findViewById(R.id.rating_rating_bar);
        rbRating.setRating(Float.parseFloat(rating));

    }

    private String generateCustomSearchUrl(String locationName) {

        String url = null;

        try {
            url = "https://www.googleapis.com/customsearch/v1?q="+ URLEncoder.encode(locationName, "UTF-8") +"&cx=005489561495639641028%3Aallb65ukzxo&num=1&key=AIzaSyBoiVEvK5X5IIgfdHDkFJZYYKaQzYi4Bsg";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Custom Search Url: " + url);

        return url;
    }

    private String generatePlaceDetailsUrl(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";
        Log.i(TAG, "Place Details Url: " + url);

        return  url;
    }

    private String getWebPageUrl(String customSearchUrl) {

        JSONObject res = requestJson(customSearchUrl);

        String url = null;

        try {
            url = (String)res.getJSONArray("items").getJSONObject(0).get("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Webpage to Scrape Url: " + url);

        return url;
    }

    private void parsePlaceDetails(String url) {

        JSONObject res = null;
        try {
            res = requestJson(url).getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray imageJson = null;
        try {
            imageJson = res.getJSONArray("photos");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i<imageJson.length(); i++) {

            String imageUrl = null;
            try {
                imageUrl = imageJson.getJSONObject(i).getString("photo_reference");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            images.add(imageUrl);
        }

        JSONArray hoursJson = null;
        try {
            hoursJson = res.getJSONObject("opening_hours").getJSONArray("weekday_text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i<hoursJson.length(); i++) {

            String hours = null;
            try {
                hours = hoursJson.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            openingHours.add(hours);
        }

        try {
            address = res.getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            phoneNumber = res.getString("formatted_phone_number");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            website = res.getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            openNow = res.getJSONObject("opening_hours").getBoolean("open_now");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void scrapeWebPage(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

            description = doc.select("div.description > div.text").text();
            numReviews = doc.select("a.seeAllReviews").text();
            suggestedDuration = doc.select("div.detail_section.duration").text();

            Elements categoriesToParse = doc.select("span.header_detail.attraction_details > div.detail > a");

            for (Element e : categoriesToParse) {
                String category = e.text();
                categories.add(category);
            }

    }

    private JSONObject requestJson (String requestUrl) {

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

        return object;

    }

}
