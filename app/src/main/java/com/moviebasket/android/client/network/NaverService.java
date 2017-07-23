package com.moviebasket.android.client.network;

import com.moviebasket.android.client.search.MovieDataResult;
import com.moviebasket.android.client.security.SecurityDataSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by LEECM on 2016-12-28.
 */

public interface NaverService {

    @Headers({"X-Naver-Client-Id :" + SecurityDataSet.NaverServiceClientID,
            "X-Naver-Client-Secret :" + SecurityDataSet.NaverServiceClientSecret})
    @GET("movie.json")
    Call<MovieDataResult> getMovieDataResult(@Query("query") String query, @Query("start") int start, @Query("display") int display, @Query("yearto") int yearto, @Query("yearfrom") int yearfrom);

}
