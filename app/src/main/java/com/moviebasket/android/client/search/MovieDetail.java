package com.moviebasket.android.client.search;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LEECM on 2016-12-29.
 */

public class MovieDetail implements Parcelable{

    public String title;
    public String link;
    public String image;
    public String pubDate;
    public String director;
    public String actor;
    public String userRating;

    public MovieDetail(String title, String director, String pubDate, String image, String link, String userRating) {
        this.title = title;
        this.director = director;
        this.pubDate = pubDate;
        this.image = image;
        this.link = link;
        this.userRating = userRating;
        this.actor = "";            //공백문자열로 추가
    }

    public MovieDetail(String title, String link, String image, String pubDate, String director, String actor, String userRating) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = userRating;
    }

    protected MovieDetail(Parcel in) {
        title = in.readString();
        link = in.readString();
        image = in.readString();
        pubDate = in.readString();
        director = in.readString();
        actor = in.readString();
        userRating = in.readString();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(image);
        dest.writeString(pubDate);
        dest.writeString(director);
        dest.writeString(actor);
        dest.writeString(userRating);
    }

    /*
            "title": "<b>해리 포터</b>와 죽음의 성물 - 2부",
            "link": "http://movie.naver.com/movie/bi/mi/basic.nhn?code=47528",
            "image": "http://imgmovie.naver.com/mdi/mit110/0475/47528_P50_144916.jpg",
            "subtitle": "Harry Potter And The Deathly Hallows: Part 2",
            "pubDate": "2011",
            "director": "데이빗 예이츠|",
            "actor": "다니엘 래드클리프|엠마 왓슨|루퍼트 그린트|",
            "userRating":
    */
}
