package com.kalicalitally.flixterapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kalicalitally.flixterapp.models.Config;
import com.kalicalitally.flixterapp.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.kalicalitally.flixterapp.MovieAdapter.backdropImg;
import static com.kalicalitally.flixterapp.MovieAdapter.posterImg;
import static com.kalicalitally.flixterapp.MovieListActivity.API_BASE_URL;
import static com.kalicalitally.flixterapp.MovieListActivity.API_KEY_PARAM;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView frontImg;
    ImageView backImg;

    AsyncHttpClient client;

    String id;

    public final static String youtubeId = "sddsadwass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        frontImg = (ImageView) findViewById(R.id.ivPImage);
        backImg = (ImageView) findViewById(R.id.ivBImage);



        //loaded image using glide
        GlideApp.with(this)
                .load(getIntent().getStringExtra(posterImg))
                .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(frontImg);

        GlideApp.with(this)
                .load(getIntent().getStringExtra(backdropImg))
                .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(backImg);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        client = new AsyncHttpClient();
        loadVideo(movie);
    }
    public void loadTrailer(View view){
        if(movie.getId() != null){
            Intent i  = new Intent(this, MovieTrailerActivity.class);
            i.putExtra(youtubeId, id);
            this.startActivity(i);
        }
    }
    //get the configruation from the API
    private void loadVideo(Movie movie){
        //create url
        String url = API_BASE_URL + "/movie/" + movie.getId()+"/videos" ;
        //set the request param
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required
        //call get request expect a JSON obj response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get the img base url
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject firstVid = results.getJSONObject(0);
                    id = firstVid.getString("key");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}

