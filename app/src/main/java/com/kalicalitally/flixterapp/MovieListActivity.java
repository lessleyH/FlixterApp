package com.kalicalitally.flixterapp;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.kalicalitally.flixterapp.models.Config;
import com.kalicalitally.flixterapp.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //base url for API
    public final static  String API_BASE_URL = "https://api.themoviedb.org/3";
    // param name for key
    public final static  String API_KEY_PARAM = "api_key";
    //tag for log
    public final static String TAG = "MovieListActivity";


    //instance fields
    AsyncHttpClient client;
    //List of curr playing movie
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the ad wired to the recycler view
    MovieAdapter adapter;
    //img config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialized the client
        client = new AsyncHttpClient();
        //init the list of movie
        movies  = new ArrayList<>();
        //init the adapter -- movie array ren=init after this pt
        adapter = new MovieAdapter(movies);

        //resolve reference view and layout manager
        rvMovies =(RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get config on app creation
        getConfiguration();

    }

    // get the list of curr playing mov from the API
    private void getNowPlaying(){
        //create url
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request param
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //parse JSON response and populate the lis of movies
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through JSON array
                    for (int i =0; i <results.length(); i ++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify list is changed and what changed
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed while parsing response", e ,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }
    //get the configruation from the API
    private void getConfiguration(){
        //create url
        String url = API_BASE_URL + "/configuration";
        //set the request param
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required
        //call get request expect a JSON obj response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get the img base url
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded config w/ imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    //pass config to adapter
                    adapter.setConfig(config);
                    //get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle error, log and alert
    private void logError(String message, Throwable error, boolean alertUser) {
        //log error
        Log.e(TAG, message, error);

        if(alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
