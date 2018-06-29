package com.kalicalitally.flixterapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kalicalitally.flixterapp.models.Config;
import com.kalicalitally.flixterapp.models.Movie;

import java.util.ArrayList;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //instance field
    //list of movies
    ArrayList<Movie> movies;
    Config config;
    Context context;

    //init w/ list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(Config config) {
        this.config = config;
    }

    //creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //get the context from parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create teh view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return view wrapped by view holder
        return new ViewHolder(movieView);
    }
    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie = movies.get(position);
        //populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //build url for poster img
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        //loaded image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCorners(25))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);

    }
    // returns the size of the items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the view holder static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        //track view obj
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //find obj by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }
    }

}
