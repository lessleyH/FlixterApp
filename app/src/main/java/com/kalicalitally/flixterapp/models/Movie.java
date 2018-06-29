package com.kalicalitally.flixterapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    //instanse fields to track val
    private String title;
    private String overview;
    private String posterPath; //only path not full url

    //init from json dt
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
