package com.moviebasket.android.client.mypage.basket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebasket.android.client.R;

/**
 * Created by pilju on 2016-12-28.
 */

public class BasketListViewHolder extends RecyclerView.ViewHolder {

    public ImageView basketImg;
    public ImageView textImg;
    public TextView basketName;
    public ImageView downBtn;
    public TextView downCount;


    public BasketListViewHolder(View itemView) {
        super(itemView);

        basketImg = (ImageView)itemView.findViewById(R.id.basketImg);
        textImg = (ImageView)itemView.findViewById(R.id.textImg);
        basketName = (TextView)itemView.findViewById(R.id.basketName);
        downBtn = (ImageView)itemView.findViewById(R.id.downBtn);
        downCount = (TextView)itemView.findViewById(R.id.downCount);
    }
}
