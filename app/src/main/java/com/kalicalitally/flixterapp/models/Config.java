package com.kalicalitally.flixterapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    // the base url for loading images
    String ImageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;
    // the backdrop size ti use when fetching images
    String backdropSize;




    public Config(JSONObject object) throws JSONException{
        JSONObject images = object.getJSONObject("images");
        ImageBaseUrl = images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3 or x342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse the backfrop sizes and use the option at index 1 or w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", ImageBaseUrl, size ,path); //concatenated
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getImageBaseUrl() {
        return ImageBaseUrl;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
