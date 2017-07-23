package com.moviebasket.android.client.mypage.movie_pack_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.clickable.TwoClickable;

import java.util.ArrayList;


/**
 * Created by pilju on 2016-12-27.
 */


public class PackAdapter extends RecyclerView.Adapter<PackViewHolder> {

    ArrayList<PackDetail> packDetails;
    View.OnClickListener clickListener;
    View.OnClickListener subClickListener;
    TwoClickable two;
    private ViewGroup parent;

    public PackAdapter() {

    }

    public PackAdapter(ArrayList<PackDetail> packDetails, View.OnClickListener clickListener, TwoClickable two) {
        this.packDetails = packDetails;
        this.clickListener = clickListener;
        this.two = two;
    }

    @Override
    public PackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;

        // 뷰홀더 패턴을 생성하는 메소드.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pack, parent, false);
        if(this.clickListener!=null)
            itemView.setOnClickListener(clickListener);
        PackViewHolder viewHolder = new PackViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PackViewHolder holder, final int position) {

        if (packDetails.get(position).movie_image.equals("")) {
            holder.movieImage.setImageResource(R.drawable.noimage);
        } else {
            Glide.with(parent.getContext()).load(packDetails.get(position).movie_image).into(holder.movieImage);
        }

        if (packDetails.get(position).movie_title.length() <= 15) {
            holder.movieName.setText(packDetails.get(position).movie_title);
        } else {
            holder.movieName.setText(packDetails.get(position).movie_title);
            holder.movieName.setText(packDetails.get(position).movie_title.substring(0,15)+"...");
        }

        holder.basketName.setText(packDetails.get(position).basket_name);
        holder.BasketUserName.setText(packDetails.get(position).movie_adder);
        holder.year.setText(String .valueOf(packDetails.get(position).movie_pub_date));
        if ( packDetails.get(position).movie_director.length() >= 10) {
            holder.director.setText(packDetails.get(position).movie_director.substring(0, 7)+"...");
        } else {
            holder.director.setText(packDetails.get(position).movie_director);
        }
        holder.downCount.setText(String .valueOf(packDetails.get(position).movie_like));

        if (packDetails.get(position).is_liked == 0) {
            holder.heartImg.setImageResource(R.drawable.sub_no_heart);
        } else {
            holder.heartImg.setImageResource(R.drawable.sub_heart);
        }

        holder.removeImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                two.processOneMethodAtPosition(position);
                //packDetails.remove(position);
            }
        });
        holder.heartImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                two.processTwoMethodAtPosition(position);
                if ( packDetails.get(position).is_liked == 0 ) {
                    holder.downCount.setText(String.valueOf(++packDetails.get(position).movie_like));
                    holder.heartImg.setImageResource(R.drawable.sub_heart);
                    packDetails.get(position).is_liked = 1;
                } else {
                    holder.downCount.setText(String.valueOf(--packDetails.get(position).movie_like));
                    holder.heartImg.setImageResource(R.drawable.sub_no_heart);
                    packDetails.get(position).is_liked = 0;
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return (packDetails != null) ? packDetails.size() : 0;
    }
}

