package com.moviebasket.android.client.mypage.movie_rec_list;

/**
 * Created by pilju on 2016-12-28.
 */

public class RecDatas {

    String movie_image;
    int movie_id;
    String movie_adder;
    String movie_title;
    String movie_director;
    int movie_pub_date;
    int movie_user_rating;
    String movie_link;
    int movie_like;
    int book_mark;
    int is_liked;
    int is_cart;
    String message;
    String basket_name;

    public RecDatas(String movie_image, int movie_id, String movie_adder, String movie_title, String movie_director, int movie_pub_date,
                    int movie_user_rating, String movie_link, int movie_like, int book_mark, int is_liked, int is_cart, String message, String basket_name) {
        this.movie_image = movie_image;
        this.movie_id = movie_id;
        this.movie_adder = movie_adder;
        this.movie_title = movie_title;
        this.movie_director = movie_director;
        this.movie_pub_date = movie_pub_date;
        this.movie_user_rating = movie_user_rating;
        this.movie_link = movie_link;
        this.movie_like = movie_like;
        this.book_mark = book_mark;
        this.is_liked = is_liked;
        this.is_cart = is_cart;
        this.message = message;
        this.basket_name = basket_name;
    }
}
