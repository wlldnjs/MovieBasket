package com.moviebasket.android.client.basket_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.clickable.TwoClickable;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.main.MainActivity;
import com.moviebasket.android.client.movie_detail.MovieDetailDialog;
import com.moviebasket.android.client.mypage.basket_list.BasketResult;
import com.moviebasket.android.client.mypage.movie_rec_list.HeartResult;
import com.moviebasket.android.client.network.MBService;
import com.moviebasket.android.client.search.MovieDetail;
import com.moviebasket.android.client.search.MovieSearchActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SpecificBasketActivity extends AppCompatActivity implements TwoClickable {

    private static final int REQUEST_CODE_FOR_MOVIE_SEARCH = 1000;

    private MBService mbService;
    DetailAdapter adapter;
    Button btn_add_movie;

    ImageView basketImg;
    TextView basketName;
    ImageView downBtn;
    TextView downCount;
    ImageView backBtnIcon;
    ImageView Homeicon;

    int basket_id;
    int basket_count;
    int is_liked;
    String token;

    private MovieDetailDialog detailDialog;

    private boolean isHeartSuccess = false;
    private boolean isCarttSuccess = false;

    RecyclerView recyclerView;
    ArrayList<DetailDatas> mDatas = new ArrayList<DetailDatas>();

    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_basket);

        Intent basketInfo  = getIntent();
        mbService = ApplicationController.getInstance().getMbService();

        backBtnIcon = (ImageView)findViewById(R.id.backBtnIcon);
        Homeicon = (ImageView)findViewById(R.id.Homeicon);
        Homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
                //홈화면으로 가는거
                Intent homeIntent = new Intent(SpecificBasketActivity.this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        basket_id = basketInfo.getExtras().getInt("basket_id");
        basket_count = basketInfo.getExtras().getInt("basket_like");
        is_liked = basketInfo.getExtras().getInt("is_liked");

//        Log.i("Info_BasketId : ", String.valueOf(basket_id));

        basketImg = (ImageView)findViewById(R.id.basketImg);
        basketName = (TextView)findViewById(R.id.basketName);
        downBtn = (ImageView)findViewById(R.id.specificDownBtn);
        downCount = (TextView)findViewById(R.id.downCount);

        backBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(getApplicationContext()).load(basketInfo.getExtras().getString("basket_image")).into(basketImg);
        basketName.setText(basketInfo.getExtras().getString("basket_name"));
        if ( basketInfo.getExtras().getInt("is_liked") == 0 ) {
            downBtn.setImageResource(R.drawable.sub_basket_nodown);

        } else {
            downBtn.setImageResource(R.drawable.sub_basket_down);
        }
        downCount.setText(String.valueOf(basket_count));

        if(is_liked == 0) {
            downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downBtn.setImageResource(R.drawable.sub_basket_down);
                    downCount.setText(String.valueOf(++basket_count));
                    Call<BasketResult> cartResult = mbService.getCartPutResult(basket_id, token);
                    //바스켓 담기 요청
                    cartResult.enqueue(new Callback<BasketResult>() {
                        @Override
                        public void onResponse(Call<BasketResult> call, Response<BasketResult> response) {
                            if (response.isSuccessful()) {
                                BasketResult result = response.body();
                                if (result.result.message.equals("like update success")) {
                                    Toast.makeText(SpecificBasketActivity.this, "바스켓을 담았습니다.", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                downBtn.setImageResource(R.drawable.sub_basket_nodown);
                                Toast.makeText(SpecificBasketActivity.this, "실패.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasketResult> call, Throwable t) {
                            Toast.makeText(SpecificBasketActivity.this, "서버와 통신을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }

        btn_add_movie = (Button)findViewById(R.id.btn_add_movie_specific);
        btn_add_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieSearchIntent = new Intent(SpecificBasketActivity.this, MovieSearchActivity.class);
                movieSearchIntent.putExtra("basket_id", basket_id);
                startActivityForResult(movieSearchIntent, REQUEST_CODE_FOR_MOVIE_SEARCH);
                overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
            }
        });

        /**
         * 1. recyclerview 초기화
         */
        recyclerView = (RecyclerView) findViewById(R.id.specific__Recyclerview);
        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);


        // LayoutManager 초기화
        // layoutManager 설정
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);


        /**
         * 2. recyclerview에 보여줄 data
         */
        mDatas = new ArrayList<DetailDatas>();
        token = ApplicationController.getInstance().getPreferences();

        //바스켓 리스트를 가져온다.
        loadBasketList();

    }
    private View.OnClickListener recylerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //1.리사이클러뷰에 몇번째 항목을 클릭했는지 그 position을 가져오는 것.
            int position = recyclerView.getChildLayoutPosition(v);

            String movie_title = mDatas.get(position).movie_title;
            String movie_link = mDatas.get(position).movie_link;
            String movie_image = mDatas.get(position).movie_image;
            String movie_pubDate = String.valueOf(mDatas.get(position).movie_pub_date);
            String movie_director = mDatas.get(position).movie_director;
            String movie_userRating = mDatas.get(position).movie_user_rating;

            MovieDetail detail = new MovieDetail(movie_title, movie_director, movie_pubDate, movie_image, movie_link, movie_userRating);
            detailDialog = new MovieDetailDialog(SpecificBasketActivity.this, detail);
            detailDialog.show();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        //데이터 다시 불러오기
        loadBasketList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_FOR_MOVIE_SEARCH:
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "영화를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loadBasketList(){
        Call<DetailResultParent> getBasketDetail = mbService.getBasketDetail(token, basket_id);
        getBasketDetail.enqueue(new Callback<DetailResultParent>() {
            @Override
            public void onResponse(Call<DetailResultParent> call, Response<DetailResultParent> response) {
//                Log.i("NetConfirm", "onResponse: 들어옴");

                DetailResultParent recResult = response.body();
                if (response.isSuccessful()) {// 응답코드 200
//                    Log.i("recommendMovie Test", "요청메시지:" + call.toString() + " 응답메시지:" + response.toString());
                    mDatas.clear();
                    mDatas.addAll(recResult.result.result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DetailResultParent> call, Throwable t) {
//                Log.i("NetConfirm", "onFailure: 들어옴");

                Toast.makeText(SpecificBasketActivity.this, "서비스에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
//                Log.i("recommendMovie Test", "요청메시지:" + call.toString());
            }
        });

        adapter = new DetailAdapter(mDatas, recylerClickListener, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void processOneMethodAtPosition(final int position) {
        Call<HeartResult> getHeartReasult = mbService.getHeartResult(mDatas.get(position).movie_id, mDatas.get(position).is_liked, token);
        getHeartReasult.enqueue(new Callback<HeartResult>() {
            @Override
            public void onResponse(Call<HeartResult> call, Response<HeartResult> response) {
                HeartResult heartResult = response.body();
                if (response.isSuccessful()) {// 응답코드 200
                    isHeartSuccess = heartResult.result.message != null ? true : false;
                }
                if (isHeartSuccess) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HeartResult> call, Throwable t) {
                Toast.makeText(SpecificBasketActivity.this, "서비스에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }


    //TODO : 영화 담기 수정해야함.
    @Override
    public void processTwoMethodAtPosition(final int position) {
        if(mDatas.get(position).is_cart == 0) {
            Call<HeartResult> getMovieCartReasult = mbService.getMovieCartReasult(mDatas.get(position).movie_id, mDatas.get(position).is_cart, token);
            getMovieCartReasult.enqueue(new Callback<HeartResult>() {
                @Override
                public void onResponse(Call<HeartResult> call, Response<HeartResult> response) {
//                    Log.i("Cart", "onResponse: 2번메서드(담기)");
                    HeartResult heartResult = response.body();
                    if (response.isSuccessful()) {// 응답코드 200
//                        Log.i("Cart", "요청메시지:" + call.toString() + " 응답메시지:" + response.toString());
//                        Log.i("Cart", "응답 결과 : " + heartResult.result.message);
                        isCarttSuccess = heartResult.result.message != null ? true : false;
//                        Log.i("Cart", "응답 결과 : " + isCarttSuccess);
//                        Log.i("Cart", "포지션 : " + mDatas.get(position));
//                        Log.i("Cart", "무비아이디 : " + mDatas.get(position).movie_id);
//                        Log.i("Cart", "Cart : " + mDatas.get(position).is_cart);
                        if (isCarttSuccess) {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "영화담기실패", Toast.LENGTH_SHORT);
                    }

                }

                @Override
                public void onFailure(Call<HeartResult> call, Throwable t) {
                    Log.i("Cart", "onFailure: 들어옴" + call.toString());
                    Toast.makeText(SpecificBasketActivity.this, "서비스에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "이미 영화를 담았습니다.", Toast.LENGTH_SHORT);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_down);
    }
}
