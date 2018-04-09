package com.example.james.exploreapp;

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

public class Api {

    private JSONObject getDirections(String origin, String destination, int departureTime, String mode) {

        String directionsUrl = generateDirectionsUrl(origin, destination, departureTime, mode);
        return requestJson(directionsUrl);

    }

    private JSONObject getPlaceTextSearch (String query) {

        String placeTextSearchUrl = generatePlaceTextSearchUrl(query);
        return requestJson(placeTextSearchUrl);

    }

    private JSONObject getPlaceDetailsById (String placeId) {

        String placeDetailsUrl = generatePlaceDetailsUrl(placeId);
        return requestJson(placeDetailsUrl);

    }

    private JSONObject getTripadvisorCustomSearch (String query) {

        String customSearchUrl = generateCustomSearchUrl(query);
        return requestJson(customSearchUrl);

    }

    private String generateDirectionsUrl(String origin, String destination, int departureTime, String mode) {

        String orig = origin;
        String dest = destination;

        try {

            orig = URLEncoder.encode(origin, "UTF-8");
            dest = URLEncoder.encode(destination, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + orig + "&destination=" + dest + "&departure_time=" + departureTime + "&mode=" + mode + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";

        return url;
    }

    private String generateCustomSearchUrl(String locationName) {

        String url = null;

        try {
            url = "https://www.googleapis.com/customsearch/v1?q="+ URLEncoder.encode(locationName, "UTF-8") +"&cx=005489561495639641028%3Aallb65ukzxo&num=1&key=AIzaSyBoiVEvK5X5IIgfdHDkFJZYYKaQzYi4Bsg";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return url;
    }

    private String generatePlaceDetailsUrl(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";

        return  url;
    }

    private String generatePlaceTextSearchUrl(String city) {
        try {
            return "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+ URLEncoder.encode(city, "UTF-8") +"+things+to+do&key=AIzaSyBME8XX7Bml-QRTX_TX0o7jskALXHrXHcw";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
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
