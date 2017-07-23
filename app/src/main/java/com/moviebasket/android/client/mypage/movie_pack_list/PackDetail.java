package com.moviebasket.android.client.mypage.movie_pack_list;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pilju on 2016-12-31.
 */

public class PackDetail implements Parcelable{

    int movie_id; //인덱스
    String movie_title; //영화이름
    String movie_image; //영화이미지 URL
    String movie_director; //감독
    int movie_pub_date; //연도
    String movie_adder ; // 바스캇에담은사람이름
    String movie_user_rating; //평점
    String movie_link; //영화링크
    int movie_like; // 좋아요 갯수
    int is_liked; // 좋아요정보 : 1, 0
    int is_cart; //담은정보 : 1,0
    String basket_name; // 바스켓이름

    protected PackDetail(Parcel in) {
        movie_id = in.readInt();
        movie_title = in.readString();
        movie_image = in.readString();
        movie_director = in.readString();
        movie_pub_date = in.readInt();
        movie_adder = in.readString();
        movie_user_rating = in.readString();
        movie_link = in.readString();
        movie_like = in.readInt();
        is_liked = in.readInt();
        is_cart = in.readInt();
        basket_name = in.readString();
    }

    public static final Creator<PackDetail> CREATOR = new Creator<PackDetail>() {
        @Override
        public PackDetail createFromParcel(Parcel in) {
            return new PackDetail(in);
        }

        @Override
        public PackDetail[] newArray(int size) {
            return new PackDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(movie_title);
        dest.writeString(movie_image);
        dest.writeString(movie_director);
        dest.writeInt(movie_pub_date);
        dest.writeString(movie_adder);
        dest.writeString(movie_user_rating);
        dest.writeString(movie_link);
        dest.writeInt(movie_like);
        dest.writeInt(is_liked);
        dest.writeInt(is_cart);
        dest.writeString(basket_name);
    }
}
