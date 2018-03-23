package com.example.james.exploreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class RouteSettingsActivity extends AppCompatActivity {

    ArrayList<Location> locations;
    String defaultStartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_settings);

        locations = this.getIntent().getParcelableArrayListExtra("Locations");
        defaultStartLocation = (String) this.getIntent().getExtras().get("City");
    }
}
