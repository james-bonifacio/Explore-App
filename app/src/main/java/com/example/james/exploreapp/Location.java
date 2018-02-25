package com.example.james.exploreapp;

import android.os.Parcel;
import android.os.Parcelable;


public class Location implements Parcelable{

    private String name;
    private String img;
    private String tagLine;
    private String rating;
    private String visited;
    private String isOpen;
    private String placeId;

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        @Override
        public Location createFromParcel(Parcel parcel) {
            return new Location(parcel);
        }

        @Override
        public Location[] newArray(int i) {
            return new Location[i];
        }
    };

    public Location() {
        super();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Location(String name, String img, String tagLine, String rating, String isOpen, String placeId){
        this.name = name;
        this.img = img;
        this.tagLine = tagLine;
        this.rating = rating;
        this.isOpen = isOpen;
        this.placeId = placeId;

        this.visited = "false";
    }

    public Location(Parcel parcel) {
        this.name = parcel.readString();
        this.img = parcel.readString();
        this.tagLine = parcel.readString();
        this.visited = parcel.readString();
        this.rating = parcel.readString();
        this.isOpen = parcel.readString();
        this.placeId = parcel.readString();
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

    public String getIsOpen() {
        return isOpen;
    }

    public void setVisited(String visited) {
        this.visited = visited;
    }

    public String getVisited() {

        return visited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.name);
        parcel.writeString(this.img);
        parcel.writeString(this.tagLine);
        parcel.writeString(this.visited);
        parcel.writeString(this.rating);
        parcel.writeString(this.isOpen);
        parcel.writeString(this.placeId);

    }
}
