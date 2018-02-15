package com.example.james.exploreapp;

/**
 * Created by James on 2018-02-13.
 */

public class Route {
    private String name;
    private String tagline;
    private String rating;
    private String img;
    private String stay;
    private String travel;
    private String url;
    private String visited;
    private boolean first;
    private boolean last;

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getRating() {
        return rating;
    }

    public String getVisited() {
        return visited;
    }

    public String getImg() {
        return img;
    }

    public String getStay() {
        return stay;
    }

    public String getTravel() {
        return travel;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public String getUrl() {
        return url;
    }

    public Route(String name, String tagline, String rating, String img, String stay, String travel, String url, String visited, boolean first, boolean last) {
        this.name = name;
        this.tagline = tagline;
        this.rating = rating;
        this.img = img;
        this.stay = stay;
        this.travel = travel;
        this.url = url;
        this.visited = visited;
        this.first = first;
        this.last = last;
    }
}
