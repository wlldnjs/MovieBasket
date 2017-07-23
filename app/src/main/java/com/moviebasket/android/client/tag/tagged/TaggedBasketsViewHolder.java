package com.moviebasket.android.client.tag.tagged;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebasket.android.client.R;

/**
 * Created by Jiwon on 2017-01-03.
 */
public class TaggedBasketsViewHolder  extends RecyclerView.ViewHolder{
    ImageView basketImg;
    ImageView textImg;
    TextView basketName;
    ImageView downBtn;
    TextView downCount;
    public TaggedBasketsViewHolder(View itemView) {
        super(itemView);

        basketImg = (ImageView)itemView.findViewById(R.id.basketImg);
        textImg = (ImageView)itemView.findViewById(R.id.textImg);
        basketName = (TextView)itemView.findViewById(R.id.basketName);
        downBtn = (ImageView)itemView.findViewById(R.id.downBtn);
        downCount = (TextView)itemView.findViewById(R.id.downCount);
    }
}