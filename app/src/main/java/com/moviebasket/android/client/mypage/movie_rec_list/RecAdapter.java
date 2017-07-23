package com.moviebasket.android.client.mypage.movie_rec_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.clickable.OneClickable;

import java.util.ArrayList;

/**
 * Created by pilju on 2016-12-28.
 */

public class RecAdapter extends RecyclerView.Adapter<RecViewHolder> {

    ArrayList<RecDatas> mDatas;
    View.OnClickListener recyclerclickListener;

    private View itemView;
    private ViewGroup parent;
    OneClickable oneClickable;


    public RecAdapter(ArrayList<RecDatas> mDatas) {
        this.mDatas = mDatas;
    }
    public RecAdapter(ArrayList<RecDatas> mDatas, View.OnClickListener clickListener) {
        this.mDatas = mDatas;
        this.recyclerclickListener = clickListener;
    }

    public RecAdapter(ArrayList<RecDatas> mDatas, View.OnClickListener recyclerclickListener, OneClickable oneClickable) {
        this.mDatas = mDatas;
        this.recyclerclickListener = recyclerclickListener;
        this.oneClickable = oneClickable;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        // 뷰홀더 패턴을 생성하는 메소드.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rec, parent, false);

        if( this.recyclerclickListener != null )
            itemView.setOnClickListener(recyclerclickListener);

        RecViewHolder viewHolder = new RecViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecViewHolder holder, final int position) {

        //리싸이클뷰에 항목을 뿌려주는 메소드.
        //holder.movie_image.setImageResource(R.drawable.sub_movie_down);
        if (mDatas.get(position).movie_image.equals("")) {
            holder.movie_image.setImageResource(R.drawable.noimage);
        } else {
            Glide.with(parent.getContext()).load(mDatas.get(position).movie_image).into(holder.getMovieImageView());
        }

        //Glide.with(parent.getContext()).load(mDatas.get(position).movie_image).into(holder.getMovieImageView());
        holder.owner.setText(mDatas.get(position).movie_adder);
        holder.likecount.setText(String.valueOf(mDatas.get(position).movie_like));
        holder.movie_pub_date.setText(String.valueOf(mDatas.get(position).movie_pub_date));

        if (mDatas.get(position).movie_title.length() <= 15) {
            holder.title.setText(mDatas.get(position).movie_title);
        } else {
            holder.title.setText(mDatas.get(position).movie_title);
            holder.title.setText(mDatas.get(position).movie_title.substring(0,15)+"...");
        }

//        holder.direct.setText(mDatas.get(position).movie_director);
        if ( mDatas.get(position).movie_director.length() >= 10) {
            holder.direct.setText(mDatas.get(position).movie_director.substring(0, 7)+"...");
        } else {
            holder.direct.setText(mDatas.get(position).movie_director);
        }

        holder.basket_name.setText(mDatas.get(position).basket_name);

        if(mDatas.get(position).book_mark==1) {
            holder.book_mark.setImageResource(R.drawable.sub_movie_down);
        }else{
            holder.book_mark.setImageResource(R.drawable.sub_movie_nodown);
        }

        if(mDatas.get(position).is_liked==1){
            holder.is_liked.setImageResource(R.drawable.sub_heart);
        }else{
            holder.is_liked.setImageResource(R.drawable.sub_no_heart);
        }

        holder.is_liked.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                oneClickable.processOneMethodAtPosition(position);
                mDatas.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }
}
