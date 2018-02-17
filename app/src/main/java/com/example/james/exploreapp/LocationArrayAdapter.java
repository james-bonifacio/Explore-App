package com.example.james.exploreapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by James on 2018-02-10.
 */

public class LocationArrayAdapter extends ArrayAdapter<Location> {

    Context context;

    public LocationArrayAdapter(Context context, int resourceId, ArrayList<Location> locations) {
        super(context, resourceId, locations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Location location = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.tvName);
        TextView tagline = (TextView) convertView.findViewById(R.id.tvTagline);
        ImageView image = (ImageView) convertView.findViewById(R.id.imgPicture);

        name.setText(location.getName());
        tagline.setText(location.getTagLine());

        Glide
            .with(getContext())
            .load("https://lh3.googleusercontent.com/p/AF1QipOsScagqHmCNGnHMG4ZHy7IX2zz6XRX93vgWKqD=s1600-w400")
            .into(image);

        return convertView;

    }
}
