package com.example.james.exploreapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        if (route.isFirst()) {
            holder.tvTagline.setVisibility(View.GONE);
            holder.tvRating.setVisibility(View.GONE);
            holder.chkVisited.setVisibility(View.GONE);
            holder.imgPicture.setVisibility(View.GONE);
            holder.tvName.setGravity(Gravity.CENTER);
            holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.tvName.setLayoutParams(lp);

            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) holder.tvStay.getLayoutParams();
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.tvStay.setLayoutParams(lp2);

            holder.tvStay.setGravity(Gravity.CENTER);
        }

        if (route.isLast()) {
            holder.tvDownArrow.setVisibility(View.GONE);
            holder.tvTravel.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPicture;
        TextView tvName, tvTagline, tvRating, tvStay, tvTravel, tvDownArrow;
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
            tvDownArrow = itemView.findViewById(R.id.tvDownArrow);

        }
    }
}
