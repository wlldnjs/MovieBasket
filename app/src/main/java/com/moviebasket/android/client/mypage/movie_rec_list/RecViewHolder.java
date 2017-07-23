package com.moviebasket.android.client.mypage.movie_rec_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebasket.android.client.R;

/**
 * Created by pilju on 2016-12-28.
 */

public class RecViewHolder extends RecyclerView.ViewHolder {
    ImageView movie_image;
    TextView owner;
    TextView likecount;
    TextView title;
    TextView direct;
    TextView movie_pub_date;
    TextView basket_name;
    ImageView book_mark;
    ImageView is_liked;

    public RecViewHolder(View itemView) {
        super(itemView);

        movie_image = (ImageView)itemView.findViewById(R.id.movie_image);
        owner = (TextView)itemView.findViewById(R.id.owner);
        likecount = (TextView)itemView.findViewById(R.id.likecount);
        title = (TextView)itemView.findViewById(R.id.title);
        direct = (TextView)itemView.findViewById(R.id.direct);
        movie_pub_date =  (TextView)itemView.findViewById(R.id.movie_pub_date);
        book_mark = (ImageView)itemView.findViewById(R.id.book_mark);
        is_liked = (ImageView)itemView.findViewById(R.id.heart);
        basket_name = (TextView)itemView.findViewById(R.id.basketName);

    }
    public ImageView getMovieImageView(){
        return movie_image;
    }

}
