package com.moviebasket.android.client.basket_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.clickable.TwoClickable;

import java.util.ArrayList;

/**
 * Created by user on 2016-12-28.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    ArrayList<DetailDatas> mDatas;
    View.OnClickListener clickListener;
    View.OnClickListener subClickListener;
    private ViewGroup parent;
    TwoClickable twoClickable;


    public DetailAdapter() {

    }

    public DetailAdapter(ArrayList<DetailDatas> mDatas, View.OnClickListener clickListener, TwoClickable twoClickable) {
        this.mDatas = mDatas;
        this.clickListener = clickListener;
        this.twoClickable = twoClickable;
    }


    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        // 뷰홀더 패턴을 생성하는 메소드.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_detail, parent, false);
        if(this.clickListener!=null)
            itemView.setOnClickListener(clickListener);
        DetailViewHolder viewHolder = new DetailViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, final int position) {
        //리싸이클뷰에 항목을 뿌려주는 메소드.
        if (mDatas.get(position).movie_image.equals("")) {
            holder.movieImage.setImageResource(R.drawable.noimage);
        } else {
            Glide.with(parent.getContext()).load(mDatas.get(position).movie_image).into(holder.getMovieImageView());
        }

        if (mDatas.get(position).movie_title.length() <= 15) {
            holder.movieName.setText(mDatas.get(position).movie_title);
        } else {
            holder.movieName.setText(mDatas.get(position).movie_title);
            holder.movieName.setText(mDatas.get(position).movie_title.substring(0,15)+"...");
        }


        holder.BasketUserName.setText(mDatas.get(position).movie_adder);
        holder.year.setText(String.valueOf(mDatas.get(position).movie_pub_date));
        if ( mDatas.get(position).movie_director.length() >= 10) {
            holder.director.setText(mDatas.get(position).movie_director.substring(0, 7)+"...");
        } else {
            holder.director.setText(mDatas.get(position).movie_director);
        }
        holder.downCount.setText(String.valueOf(mDatas.get(position).movie_like));


        if (mDatas.get(position).is_liked == 0) {
            holder.heartImg.setImageResource(R.drawable.sub_no_heart);
        } else {
            holder.heartImg.setImageResource(R.drawable.sub_heart);
        }

        if (mDatas.get(position).is_cart == 0) {
            holder.downImg.setImageResource(R.drawable.sub_movie_nodown);
        } else {
            holder.downImg.setImageResource(R.drawable.sub_movie_down);
        }

        holder.heartImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                twoClickable.processOneMethodAtPosition(position);
                if ( mDatas.get(position).is_liked == 0 ) {
                    holder.downCount.setText(String.valueOf(++mDatas.get(position).movie_like));
                    holder.heartImg.setImageResource(R.drawable.sub_heart);
                    mDatas.get(position).is_liked = 1;
                } else {
                    holder.downCount.setText(String.valueOf(--mDatas.get(position).movie_like));
                    holder.heartImg.setImageResource(R.drawable.sub_no_heart);
                    mDatas.get(position).is_liked = 0;
                }

            }
        });

        holder.downImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                twoClickable.processTwoMethodAtPosition(position);
                if ( mDatas.get(position).is_cart == 0 ) {
                    holder.downImg.setImageResource(R.drawable.sub_movie_down);
                    mDatas.get(position).is_cart = 1;
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return (mDatas != null) ? mDatas.size() : 0;
    }
}