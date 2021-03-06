package com.example.james.exploreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.TwoStatePreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener  {

    private ExpandableTextView expandableTextView;
    private TextView tvName, tvRating, tvNumReviews, tvOpen, tvSuggestedDuration, tvAddress, tvPhoneNumber, tvWebsite;
    private RatingBar rbRating;
    private ReviewAdapter adapter;
    private RecyclerView recyclerView;
    private SliderLayout sliderLayout;

    private String placeId;

    //from Places api
    private String name, rating, address, phoneNumber, website;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> openingHours = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private Boolean openNow;
    private Double lat, lng;

    //from web scrape
    private String description, numReviews, suggestedDuration;
    private ArrayList<String> categories = new ArrayList<>();

    GoogleMap googleMap;
    MapFragment mapFragment;


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

    @Override
    public void onMapReady(GoogleMap map) {

        Log.i(TAG, "Latitude: " + lat + ", Longitude: " + lng);

        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Edit the following as per you needs
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //

        LatLng placeLocation = new LatLng(lat, lng); //Make them global
        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation)
                .title(name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 1000, null);

    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
        tvSuggestedDuration = (TextView) findViewById(R.id.suggested_duration_text_view);
        tvAddress = (TextView) findViewById(R.id.address_text_view);
        tvPhoneNumber = (TextView) findViewById(R.id.phone_number_text_view);
        tvWebsite = (TextView) findViewById(R.id.website_text_view);

        tvName.setText(name);
        tvRating.setText(rating);
        tvNumReviews.setText(numReviews);
        tvOpen.setText(openNow ? "OPEN NOW" : "CLOSED NOW");
        tvSuggestedDuration.setText(suggestedDuration);
        tvAddress.setText(address);
        tvPhoneNumber.setText(phoneNumber);
        tvWebsite.setText(website);

        expandableTextView = (ExpandableTextView)findViewById(R.id.expandable_text_view);
        expandableTextView.setText(description);

        rbRating = (RatingBar) findViewById(R.id.rating_rating_bar);
        rbRating.setRating(Float.parseFloat(rating));

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(this, reviews);

        recyclerView.setAdapter(adapter);

        sliderLayout = findViewById(R.id.slider);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .centerCrop();

        for (int i = 0; i < images.size(); i++) {

            TextSliderView sliderView = new TextSliderView(this);
            sliderView
                    .image(images.get(i))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            sliderLayout.addSlider(sliderView);

        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);



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

            imageUrl = getImgUrl(imageUrl);
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

        try {
            lat = res.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            lng = res.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            lng = res.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray reviewsJSON = null;
        try {
            reviewsJSON = res.getJSONArray("reviews");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i<reviewsJSON.length(); i++) {

            Review review = new Review();

            JSONObject obj = null;

            try {
                obj = reviewsJSON.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String name = null;
            try {
                name = obj.getString("author_name");
                review.setName(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String imgUrl = null;
            try {
                imgUrl = obj.getString("profile_photo_url");
                review.setImgUrl(imgUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String text = null;
            try {
                text = obj.getString("text");
                review.setText(text);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String time = null;
            try {
                time = obj.getString("relative_time_description");
                review.setTime(time);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int rating = 5;
            try {
                rating = obj.getInt("rating");
                review.setRating(rating);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            reviews.add(review);

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

    private String getImgUrl (String photoRef) {

        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1440&photoreference=" + photoRef + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";
    }

}
