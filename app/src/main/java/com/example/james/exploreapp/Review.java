package com.example.james.exploreapp;

/**
 * Created by James on 2018-03-02.
 */

public class Review {

    private String name;
    private String imgUrl;
    private String text;
    private String time;
    private int rating;

    public Review(String name, String imgUrl, String text, String time, int rating) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.text = text;
        this.time = time;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
