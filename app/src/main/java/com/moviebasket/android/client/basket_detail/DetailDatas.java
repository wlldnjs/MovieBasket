package com.moviebasket.android.client.basket_detail;

/**
 * Created by user on 2016-12-28.
 */
public class DetailDatas {
    int movie_id;
    String movie_image;
    String movie_adder;
    String movie_title;
    int movie_pub_date;
    String movie_director;
    int movie_like;
    int is_liked;
    int is_cart;
    String movie_link;
    String movie_user_rating;




    public DetailDatas(int movie_id, String movieImage, String BasketUserName, String movieName, int year, String director, int downCount, int heartImg, int downImg, String movie_link, String movie_user_rating) {
        this.movie_id = movie_id;
        this.movie_image = movieImage;
        this.movie_adder = BasketUserName;
        this.movie_title = movieName;
        this.movie_pub_date = year;
        this.movie_director = director;
        this.movie_like = downCount;
        this.is_liked = heartImg;
        this.is_cart = downImg;
        this.movie_link = movie_link;
        this.movie_user_rating = movie_user_rating;
    }

}
