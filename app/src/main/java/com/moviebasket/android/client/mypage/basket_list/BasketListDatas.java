package com.moviebasket.android.client.mypage.basket_list;

/**
 * Created by pilju on 2016-12-28.
 */

public class BasketListDatas {

    public int basket_id;
    public String basket_name;
    public String basket_image;
    public int basket_like;
    public int is_liked;        // 1이면 좋아요를 한 것. 0이면 좋아요를 취소한 것

    public BasketListDatas(int basket_id, String basket_name, String basket_image, int basket_like, int is_liked) {
        this.basket_id = basket_id;
        this.basket_name = basket_name;
        this.basket_image = basket_image;
        this.basket_like = basket_like;
        this.is_liked = is_liked;
    }
}


