package com.example.james.exploreapp;

public class Location {

    private String name;
    private String img;
    private String tagLine;

    public Location(String name, String tagLine){
        this.name = name;
        this.tagLine = tagLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}
