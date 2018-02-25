package com.example.james.exploreapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class DescriptionActivity extends AppCompatActivity {

    ExpandableTextView expandableTextView;
    String description = "The CN Tower is a National icon, engineering Wonder, a Toronto must-see attraction and award winning dining and entertainment destination. Rocket to the top aboard the Tower's glass fronted and glass floor paneled elevators which take you to the top in only 58 seconds. Take in spectacular views of up to 160km (100 miles) away from four observation areas on three levels, including the LookOut, world famous Glass Floor with outdoor SkyTerrace and the SkyPod, the highest of them all. Experience the thrill of EdgeWalk at the CN Tower, the World's Highest Outdoor Walk on a Building. Enjoy award-winning Canadian cuisine and breathtaking revolving views at 360 The Restaurant at the CN Tower, family friendly fare at Horizons Toronto celebrating the multicultural flavours of Toronto neighbourhoods, or grab and go fare from Le Café. Plus, visual displays, KidZone play area and 8,000 square feet of unique Canadian artisan and souvenir shopping in the Gift Shop. Don't miss the architectural illumination of the CN Tower each night from dusk with top of the hour light show.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Location location = getIntent().getExtras().getParcelable("Location");

        Location test = location;

        expandableTextView = (ExpandableTextView)findViewById(R.id.expandable_text_view);
        expandableTextView.setText(description);


    }

}
