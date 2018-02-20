package com.example.james.exploreapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by James on 2018-02-12.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mCtx;
    private ArrayList<Location> locations;

    public ListAdapter(Context mCtx, ArrayList<Location> locations) {
        this.mCtx = mCtx;
        this.locations = locations;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        ListViewHolder holder = new ListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        Location location = locations.get(position);
        holder.tvName.setText(location.getName());
        holder.tvTagline.setText(location.getTagLine());
        holder.tvRating.setText(location.getRating());

        Glide
            .with(mCtx)
            .load(location.getImg())
            .into(holder.imgPicture);

        //holder.imgPicture.setImageDrawable();

        if (location.getVisited() == "true") {
            holder.chkVisited.setChecked(true);

        } else {
            holder.chkVisited.setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPicture;
        TextView tvName, tvTagline, tvRating;
        CheckBox chkVisited;


        public ListViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTagline = itemView.findViewById(R.id.tvTagline);
            tvRating = itemView.findViewById(R.id.tvRating);
            chkVisited = itemView.findViewById(R.id.chkVisited);
            imgPicture = itemView.findViewById(R.id.imgPicture);

        }
    }



}
