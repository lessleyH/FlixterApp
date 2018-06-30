package com.kalicalitally.flixterapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //instance field
    //list of movies
    ArrayList<Movie> movies;
    Config config;
    Context context;

    public static final String backdropImg = "dfd";
    public static final String posterImg = "dsds";

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

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        //build url for poster img
        //change the
        String imageUrl = "http://www.denofgeek.com/us/books/deadpool/251654/spider-man-and-deadpool-their-smart-alecky-history";

         // change url if portrait
        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }else{
            //load img url if landscape using the backdrop
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get the placeholder dep on orientation == ternary expression
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        //loaded image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCorners(25))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }
    // returns the size of the items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the view holder static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                intent.putExtra(posterImg, config.getImageUrl(config.getPosterSize(), movie.getPosterPath()));
                intent.putExtra(backdropImg, config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath()));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

}
