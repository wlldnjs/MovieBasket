package com.moviebasket.android.client.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebasket.android.client.R;


/**
 * Created by pilju on 2016-12-30.
 */

public class MoviesViewHolder extends RecyclerView.ViewHolder {

    ImageView movieImg;
    TextView title_year;
    TextView director_country;
    TextView scoreImg;
    TextView director_movie_search;
    ImageView starPointImg;

    public MoviesViewHolder(View itemView) {
        super(itemView);

        movieImg = (ImageView)itemView.findViewById(R.id.movieImg);
        title_year = (TextView)itemView.findViewById(R.id.title_year);
        director_country = (TextView)itemView.findViewById(R.id.director_country);
        director_movie_search = (TextView)itemView.findViewById(R.id.director_movie_search);
        scoreImg = (TextView)itemView.findViewById(R.id.scoreImg);
        starPointImg = (ImageView)itemView.findViewById(R.id.search_star);

    }
    public ImageView getImageView(){
        return movieImg;
    }
}
