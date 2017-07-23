package com.moviebasket.android.client.mypage.basket_list;

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

public class BasketListAdapter extends RecyclerView.Adapter<BasketListViewHolder>{

    ArrayList<BasketListDatas> mDatas;
    View.OnClickListener clickListener;

    View.OnClickListener subClickListener;

    OneClickable oneClickable;


    private ViewGroup parent;
    private View itemView;

    public BasketListAdapter(ArrayList<BasketListDatas> mDatas) {
        this.mDatas = mDatas;
    }



    public BasketListAdapter(ArrayList<BasketListDatas> mDatas, View.OnClickListener clickListener, OneClickable oneClickable) {
        this.mDatas = mDatas;
        this.clickListener = clickListener;
        this.oneClickable = oneClickable;

    }

    @Override
    public BasketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 뷰홀더 패턴을 생성하는 메소드.

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mybl, parent, false);
        if(this.clickListener!=null)
            itemView.setOnClickListener(clickListener);

        this.parent = parent;
        this.itemView = itemView;
        BasketListViewHolder viewHolder = new BasketListViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasketListViewHolder holder, final int position) {
        //리싸이클뷰에 항목을 뿌려주는 메소드.
        Glide.with(parent.getContext()).load(mDatas.get(position).basket_image).into(holder.basketImg);
        holder.downCount.setText(String.valueOf(mDatas.get(position).basket_like));

        if (mDatas.get(position).basket_name.length() <= 18) {
            holder.basketName.setText(mDatas.get(position).basket_name);
        } else if (mDatas.get(position).basket_name.length() > 18 && mDatas.get(position).basket_name.length() <= 20){
            holder.basketName.setTextSize(14);
            holder.basketName.setText(mDatas.get(position).basket_name);
        }

        if ( mDatas.get(position).is_liked == 1 ) {
            holder.downBtn.setImageResource(R.drawable.trash_white);
        } else {
            holder.downBtn.setImageResource(R.drawable.trash);
        }

        holder.downBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                oneClickable.processOneMethodAtPosition(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }
}