package com.moviebasket.android.client.movie_detail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.search.MovieDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

public class MovieDetailDialog extends Dialog {

    //뷰 데이터
    TextView storybord;         //줄거리
    ImageView xbtn;             //닫기
    MovieDetail detail;         //영화 정보 객체
    TextView txt_title;         //제목
    TextView txt_year;          //출판연도
    ImageView image_view_movie; //포스터
    TextView txt_director;      //감독
    TextView txt_view_count;    //누적관객수
    ImageView image_star_point; //별점
    ImageView btn_more;         //더보기(네이버링크)

    //영화데이터
    String movie_title;         //영화 제목
    String movie_link;          //영화 링크(네이버링크)
    String movie_image;         //영화 포스터 url
    String movie_pubDate;       //영화 출판연도
    String movie_director;      //영화 감독
    String movie_actor;         //영화 출연진 (?)
    String movie_userRating;    //영화 평점
    String movie_summary;       //영화 줄거리
    String movie_view_count;    //영화 누적관객수

    boolean isRunning;      //AsyncTask를 위한 boolean변수

    public MovieDetailDialog(Context context) {
        super(context);
    }


    //MovieDetail은
    public MovieDetailDialog(Context context, MovieDetail detail) {
        super(context);
        this.detail = detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_movie_detail);

        storybord = (TextView) findViewById(R.id.storybord);
        storybord.setMovementMethod(ScrollingMovementMethod.getInstance());
        //바깥영역 눌렀을 때 다이얼로그 종료
        this.setCanceledOnTouchOutside(true);
        //뷰참조(이벤트리스너 세팅 및 참조하기)
        initView();
        //영화 정보를 안가져왔을 때.
        if (detail == null) {
            Toast.makeText(getContext(), "영화 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        isRunning = true;

        initDataExceptSummaryAndViewCount();
        loadSummary();
        loadViewCount();

    }

    private void initDataExceptSummaryAndViewCount() {
        movie_title = detail.title.replaceAll("&"+"nbsp;","").replaceAll("&"+"amp;","");
        movie_link = detail.link;
        movie_image = detail.image;
        movie_pubDate = detail.pubDate;
        movie_director = detail.director;
        movie_actor = detail.actor;
        movie_userRating = detail.userRating;

        if( movie_title.length() <= 12 ) {
            txt_title.setText(movie_title + "(" + movie_pubDate + ")");
            //txt_year.setText("("+movie_pubDate+")");
        } else if ( movie_title.length() > 12 && movie_title.length() <= 14 ) {
            txt_title.setTextSize(21);
            txt_title.setText(movie_title + "(" + movie_pubDate + ")");
            //txt_year.setText("("+movie_pubDate+")");
        } else if ( movie_title.length() > 14 && movie_title.length() <= 18 ) {
            txt_title.setTextSize(19);
            txt_title.setText(movie_title + "("+movie_pubDate+")");
            //txt_year.setText("("+movie_pubDate+")");
        } else if ( movie_title.length() > 18 && movie_title.length() <= 21) {
            txt_title.setTextSize(17);
            txt_title.setText(movie_title + "("+movie_pubDate+")");
        } else {
            txt_title.setTextSize(15);
            txt_title.setText(movie_title + "("+movie_pubDate+")");
        }

        txt_director.setText(movie_director);
        txt_view_count.setText(movie_actor);
        if(movie_image.equals("")){
            image_view_movie.setImageResource(R.drawable.noimage);
        }else {
        Glide.with(getContext()).load(movie_image).into(image_view_movie);}


        //평점에따라 영화별점 이미지 세팅
        switch (Integer.parseInt(detail.userRating)) {
            case 0:
                image_star_point.setImageResource(R.drawable.popup_star_zero);
                break;
            case 1:
                image_star_point.setImageResource(R.drawable.popup_star_one);
                break;
            case 2:
                image_star_point.setImageResource(R.drawable.popup_star_two);
                break;
            case 3:
                image_star_point.setImageResource(R.drawable.popup_star_three);
                break;
            case 4:
                image_star_point.setImageResource(R.drawable.popup_star_four);
                break;
            case 5:
                image_star_point.setImageResource(R.drawable.popup_star_five);
                break;
        }

    }

    private void loadSummary() {
        getSummaryAsyncTask asyncTask = new getSummaryAsyncTask();
        asyncTask.execute();
    }

    private void loadViewCount(){
        getViewCountAsyncTask asyncTask = new getViewCountAsyncTask();
        asyncTask.execute();
    }

    public class getSummaryAsyncTask extends AsyncTask<String, Void, String> {

        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Document doc = null;

            try {
                doc = Jsoup.connect(movie_link).get();
            } catch (IOException e) {
                e.printStackTrace();
//                Log.i("ParsingTest", "run: IOException 오류남~~~ ");
            }

            Elements summary = doc.select("p.con_tx");
            movie_summary = "";

            Iterator it = summary.iterator();
            while (it.hasNext()) {
                movie_summary += it.next().toString();
            }
            isRunning = false;

            movie_summary = RemoveHTMLTag(movie_summary);
            movie_summary = movie_summary.replaceAll("&"+"nbsp;","");
            return movie_summary;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")||s==null){
                storybord.setText("줄거리 정보를 제공하지 않습니다.");
            }
            storybord.setText(s);
            super.onPostExecute(s);
        }
    }

    public class getViewCountAsyncTask extends AsyncTask<String, Void, String> {

        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Document doc = null;

            try {
                doc = Jsoup.connect(movie_link).get();
            } catch (IOException e) {
                e.printStackTrace();
//                Log.i("ParsingTest", "run: IOException 오류남~~~ ");
            }

            Elements summary = doc.select("p.count");
            movie_view_count = "";

            Iterator it = summary.iterator();
            while (it.hasNext()) {
                movie_view_count += it.next().toString();
            }
            isRunning = false;

            movie_view_count = movie_view_count.trim();
            movie_view_count = RemoveHTMLTag(movie_view_count);
            if(movie_view_count.equals("")){
                return "집계중입니다";
            }else {
                return movie_view_count;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            txt_view_count.setText(s);
            super.onPostExecute(s);
        }
    }


    public String RemoveHTMLTag(String changeStr) {
        if (changeStr != null && !changeStr.equals("")) {
            changeStr = changeStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        } else {
            changeStr = "";
        }
        return changeStr;
    }

    private void initView() {
        xbtn = (ImageView) findViewById(R.id.Xbtn);
        txt_title = (TextView) findViewById(R.id.title);
        txt_year = (TextView) findViewById(R.id.year);
        image_view_movie = (ImageView) findViewById(R.id.img_movie);
        txt_director = (TextView) findViewById(R.id.direct);
        txt_view_count = (TextView) findViewById(R.id.viewCount);
        image_star_point = (ImageView) findViewById(R.id.starPoint);
        btn_more = (ImageView) findViewById(R.id.morebtn);

        xbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailDialog.this.dismiss();
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie_link));
                getContext().startActivity(moreIntent);
            }
        });
    }
}
