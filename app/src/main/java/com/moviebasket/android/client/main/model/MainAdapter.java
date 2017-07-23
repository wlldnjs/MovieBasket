package com.moviebasket.android.client.main.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.clickable.OneClickable;
import com.moviebasket.android.client.mypage.basket_list.BasketListDatas;
import com.moviebasket.android.client.mypage.basket_list.BasketListViewHolder;

import java.util.ArrayList;

/**
 * Created by pilju on 2017-01-02.
 */

public class MainAdapter extends RecyclerView.Adapter<BasketListViewHolder> {

    ArrayList<BasketListDatas> mDatas;
    View.OnClickListener clickListener;
    View.OnClickListener subClickListener;
    OneClickable oneClickable;

    private ViewGroup parent;
    private View itemView;

    public MainAdapter(ArrayList<BasketListDatas> mDatas) {
        this.mDatas = mDatas;
    }

    public MainAdapter(ArrayList<BasketListDatas> mDatas, View.OnClickListener clickListener, View.OnClickListener subClickListener) {
        this.mDatas = mDatas;
        this.clickListener = clickListener;
        this.subClickListener = subClickListener;
    }

    public MainAdapter(ArrayList<BasketListDatas> mDatas, View.OnClickListener clickListener, OneClickable oneClickable) {
        this.mDatas = mDatas;
        this.clickListener = clickListener;
        this.oneClickable = oneClickable;
    }

    @Override
    public BasketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 뷰홀더 패턴을 생성하는 메소드.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bl, parent, false);
        if(this.clickListener!=null)
            itemView.setOnClickListener(clickListener);

        this.parent = parent;
        this.itemView = itemView;
        BasketListViewHolder viewHolder = new BasketListViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BasketListViewHolder holder, final int position) {
        //리싸이클뷰에 항목을 뿌려주는 메소드.
        Glide.with(parent.getContext()).load(mDatas.get(position).basket_image).into(holder.basketImg);
        if (mDatas.get(position).basket_name.length() <= 18) {
            holder.basketName.setText(mDatas.get(position).basket_name);
        } else if (mDatas.get(position).basket_name.length() > 18 && mDatas.get(position).basket_name.length() <= 20){
            holder.basketName.setTextSize(14);
            holder.basketName.setText(mDatas.get(position).basket_name);
        }
        holder.downCount.setText(String.valueOf(mDatas.get(position).basket_like));

        if (mDatas.get(position).is_liked == 1) {
            holder.downBtn.setImageResource(R.drawable.sub_basket_down);
        } else {
            holder.downBtn.setImageResource(R.drawable.sub_basket_nodown);
        }
        holder.downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 담은 바스켓인 경우엔 요청을 하지 않는다.
                if(mDatas.get(position).is_liked==1){
                    return;
                }
                //이미지를 바꾸고 담기count를 1 증가시킨다.
                holder.downBtn.setImageResource(R.drawable.sub_basket_down);
                holder.downCount.setText(String.valueOf(++mDatas.get(position).basket_like));
                oneClickable.processOneMethodAtPosition(position);
                mDatas.get(position).is_liked = 1;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }
}
