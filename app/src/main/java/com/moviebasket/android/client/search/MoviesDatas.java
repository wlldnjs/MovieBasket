package com.moviebasket.android.client.search;

/**
 * Created by pilju on 2016-12-30.
 */

public class MoviesDatas {
    String movieImg;
    String title;
    String year;
    String director;
    String country;
    String scoreImg;


    public MoviesDatas(String movieImg, String title, String year, String director, String country, String scoreImg) {
        this.movieImg = movieImg;
        this.title = title;
        this.year = year;
        this.director = director;
        this.country = country;
        this.scoreImg = scoreImg;
    }

}
