package com.kalicalitally.flixterapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel // annotation indicates class is Parcelable
public class Movie {

    // fields must be public for parceler
    String title;
    String overview;
    String posterPath;
    String backdropPath;
    Double voteAverage;
    Integer id;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject movie) throws JSONException {
        title = movie.getString("title");
        overview = movie.getString("overview");
        posterPath = movie.getString("poster_path");
        backdropPath = movie.getString("backdrop_path");
        voteAverage = movie.getDouble("vote_average");
        id = movie.getInt("id");
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
