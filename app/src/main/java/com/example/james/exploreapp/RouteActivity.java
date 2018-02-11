package com.example.james.exploreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        ArrayList<Location> locations = this.getIntent().getParcelableArrayListExtra("Locations");

        ArrayList<Location> test = locations;
    }
}
