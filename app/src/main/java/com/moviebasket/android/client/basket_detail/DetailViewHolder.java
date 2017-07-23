package com.moviebasket.android.client.basket_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebasket.android.client.R;

/**
 * Created by user on 2016-12-28.
 */
public class DetailViewHolder extends RecyclerView.ViewHolder {

    ImageView movieImage;
    TextView basketName;
    TextView BasketUserName;
    TextView movieName;
    TextView year;
    TextView director;
    TextView downCount;
    ImageView heartImg;
    ImageView downImg;

    public DetailViewHolder(View itemView) {
        super(itemView);

        movieImage = (ImageView)itemView.findViewById(R.id.movieImage);
        basketName = (TextView)itemView.findViewById(R.id.basketName);
        BasketUserName = (TextView)itemView.findViewById(R.id.BasketUserName);
        movieName = (TextView)itemView.findViewById(R.id.movieName);
        year = (TextView)itemView.findViewById(R.id.year);
        director = (TextView)itemView.findViewById(R.id.director);
        downCount = (TextView)itemView.findViewById(R.id.downCount);
        heartImg = (ImageView)itemView.findViewById(R.id.heartImg);
        downImg = (ImageView)itemView.findViewById(R.id.downImg);
    }

    public ImageView getMovieImageView(){
        return movieImage;
    }
}
