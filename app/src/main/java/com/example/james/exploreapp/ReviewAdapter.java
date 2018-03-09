package com.example.james.exploreapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

/**
 * Created by James on 2018-03-05.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context mCtx;
    private ArrayList<Review> reviews;

    public ReviewAdapter(Context mCtx, ArrayList<Review> reviews) {
        this.mCtx = mCtx;
        this.reviews = reviews;
    }


    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.review_layout, parent, false);
        ReviewHolder holder = new ReviewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewHolder holder, int position) {

        Review review = reviews.get(position);

        holder.tvName.setText(review.getName());
        holder.tvTime.setText(review.getTime());
        holder.rating_rating_bar.setRating(review.getRating());
        holder.expandable_text_view.setText(review.getText());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    class ReviewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTime;
        RatingBar rating_rating_bar;
        ExpandableTextView expandable_text_view;


        public ReviewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            rating_rating_bar = itemView.findViewById(R.id.rating_rating_bar);
            expandable_text_view = itemView.findViewById(R.id.expandable_text_view);

        }
    }
}
