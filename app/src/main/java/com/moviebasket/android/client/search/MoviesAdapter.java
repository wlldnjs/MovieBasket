package com.moviebasket.android.client.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;

import java.util.ArrayList;


/**
 * Created by pilju on 2016-12-30.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {

    ArrayList<MovieDetail> mDatas;
    View.OnClickListener clickListener;
    private ViewGroup parent;
    private View itemView;
    private static final int FOOTER_VIEW = 1;


    public MoviesAdapter(ArrayList<MovieDetail> mDatas, View.OnClickListener clickListener) {
        this.mDatas = mDatas;
        this.clickListener = clickListener;
    }


    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;

        if (viewType == FOOTER_VIEW) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_moviesearch, parent, false);

            MoviesViewHolder vh = new MoviesViewHolder(itemView);

            return vh;
        }

        // 뷰홀더 패턴을 생성하는 메소드.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_moviesearch, parent, false);
        if (this.clickListener != null)
            itemView.setOnClickListener(clickListener);

        MoviesViewHolder viewHolder = new MoviesViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        if (mDatas.get(position).image.equals("")) {
            holder.movieImg.setImageResource(R.drawable.noimage);
        } else {
            Glide.with(parent.getContext()).load(mDatas.get(position).image).into(holder.getImageView());
        }
        //holder.title_year.setText(mDatas.get(position).title);

        if (mDatas.get(position).title.length() <= 13) {
            holder.title_year.setText(mDatas.get(position).title);
        } else {
            holder.title_year.setText(mDatas.get(position).title);
            holder.title_year.setText(mDatas.get(position).title.substring(0,13)+"...");
        }

        holder.director_movie_search.setText(mDatas.get(position).director);

        if ( mDatas.get(position).director.length() >= 10) {
            holder.director_movie_search.setText(mDatas.get(position).director.substring(0, 7)+"...");
        } else {
            holder.director_movie_search.setText(mDatas.get(position).director);
        }

        holder.director_country.setText(mDatas.get(position).pubDate);

        float startPoint = Float.parseFloat(mDatas.get(position).userRating);
        int starNum = (int)(startPoint+0.5)/2;

        //평점에따라 영화별점 이미지 세팅
        switch(starNum){
            case 0:
                holder.starPointImg.setImageResource(R.drawable.search_star_zero);
                break;
            case 1:
                holder.starPointImg.setImageResource(R.drawable.search_star_one);
                break;
            case 2:
                holder.starPointImg.setImageResource(R.drawable.search_star_two);
                break;
            case 3:
                holder.starPointImg.setImageResource(R.drawable.search_star_three);
                break;
            case 4:
                holder.starPointImg.setImageResource(R.drawable.search_star_four);
                break;
            case 5:
                holder.starPointImg.setImageResource(R.drawable.search_star_five);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }
}
