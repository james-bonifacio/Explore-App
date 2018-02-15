package com.example.james.exploreapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James on 2018-02-13.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context mCtx;
    private ArrayList<Route> routes;

    public RouteAdapter(Context mCtx, ArrayList<Route> routes) {
        this.mCtx = mCtx;
        this.routes = routes;

    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.route_layout, parent, false);
        RouteAdapter.RouteViewHolder holder = new RouteAdapter.RouteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {

        Route route = routes.get(position);
        holder.tvName.setText(route.getName());
        holder.tvTagline.setText(route.getTagline());
        holder.tvRating.setText("6.9");
        //holder.imgPicture.setImageDrawable();

        if (route.getVisited() == "true") {
            holder.chkVisited.setChecked(true);

        } else {
            holder.chkVisited.setChecked(false);
        }

        holder.tvStay.setText(route.getStay());
        holder.tvTravel.setText(route.getTravel());

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPicture;
        TextView tvName, tvTagline, tvRating, tvStay, tvTravel;
        CheckBox chkVisited;


        public RouteViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTagline = itemView.findViewById(R.id.tvTagline);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvStay = itemView.findViewById(R.id.tvStay);
            tvTravel = itemView.findViewById(R.id.tvTravel);
            chkVisited = itemView.findViewById(R.id.chkVisited);
            imgPicture = itemView.findViewById(R.id.imgPicture);

        }
    }
}
