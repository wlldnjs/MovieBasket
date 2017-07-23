package com.moviebasket.android.client.tag.tagged;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moviebasket.android.client.R;
import com.moviebasket.android.client.basket_detail.SpecificBasketActivity;
import com.moviebasket.android.client.clickable.OneClickable;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.main.MainActivity;
import com.moviebasket.android.client.mypage.basket_list.BasketListDatas;
import com.moviebasket.android.client.mypage.basket_list.BasketResult;
import com.moviebasket.android.client.network.MBService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaggedBasketListActivity extends AppCompatActivity implements OneClickable {

    private static final int REQEUST_CODE_FOR_SPECIFIC_BASKET = 1001;

    RecyclerView recyclerView;
    ArrayList<BasketListDatas> mDatas = new ArrayList<BasketListDatas>();
    ArrayList<Baskets> mDatas1 = new ArrayList<Baskets>();

    LinearLayoutManager mLayoutManager;
    String token;
    private MBService mbService;
    SearchDataResult result;
    int basket_id;
    String basket_name;
    String basket_image;
    int basket_like;
    boolean isSearchResult;

    ImageView backBtnIcon;
    ImageView Homeicon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_basket_list);

        //search_list에서 누른 버튼의 텍스트를 불러옴
        TextView title = (TextView) findViewById(R.id.hash_tag_title);


        Intent intent = getIntent();
        String tag_title = intent.getStringExtra("hash_title");
        int c_id = intent.getExtras().getInt("c_id");

        title.setText(tag_title);


        backBtnIcon = (ImageView)findViewById(R.id.backBtnIcon);
        backBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Homeicon = (ImageView)findViewById(R.id.Homeicon);
        Homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(TaggedBasketListActivity.this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
                finish();
            }
        });


//        Toast.makeText(getApplicationContext(),c_id,Toast.LENGTH_SHORT).show();
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
        mDatas = new ArrayList<BasketListDatas>();

        //여기는 네이버 api에서 정보 받아와서 for문으로 돌려서 add해야될것같아요 임시로 넣어둠!!
        // mDatas.add(new BasketListDatas(R.drawable.basket_img, R.drawable.text_image, "이필주", R.drawable.down_btn, "1,882"));
        //   mDatas.add(new BasketListDatas(R.drawable.basket_img, R.drawable.text_image, "김지원", R.drawable.down_btn, "2,647"));
        //  mDatas.add(new BasketListDatas(R.drawable.basket_img, R.drawable.text_image, "최서문", R.drawable.down_btn, "822"));
        //   mDatas.add(new BasketListDatas(R.drawable.basket_img, R.drawable.text_image, "이충민", R.drawable.down_btn, "3,236"));
        //   mDatas.add(new BasketListDatas(R.drawable.basket_img, R.drawable.text_image, "권민하", R.drawable.down_btn, "7,457"));

        /**
         * 3. Adapter 생성 후 recyclerview에 지정
         */
        final TaggedAdapter adapter = new TaggedAdapter(mDatas1, recylerClickListener, this);

        recyclerView.setAdapter(adapter);


        mbService = ApplicationController.getInstance().getMbService();
        token = ApplicationController.getInstance().getPreferences();

        Call<SearchDataResult> getSearchDataResult = mbService.getSearchDataResult(token, c_id);
        getSearchDataResult.enqueue(new Callback<SearchDataResult>() {
            @Override
            public void onResponse(Call<SearchDataResult> call, Response<SearchDataResult> response) {
//                Log.i("tag : ", "성공");
                if(response.isSuccessful()){
//                    Log.i("tag : ", "성공이당");
                    result = response.body();
//                    mDatas1 = result.result.baskets;
                    isSearchResult = result.result.baskets != null ? true : false;
//                    Log.i("tag : ", "요청결과"+isSearchResult);
                }
//                isSearchResult = result.result.message == null ? true : false;
                if (isSearchResult) {
                    mDatas1.clear();
                    mDatas1.addAll(result.result.baskets);

//                    Log.i("dataconfirm", "mDatas1 : "+mDatas1.toString());

                    for (int i = 0; i < mDatas1.size(); i++) {

//                        Log.i("데이터들 : ", mDatas1.get(i).basket_name);
//                        Log.i("데이터들 : ", "" + mDatas1.get(i).basket_id);
//                        Log.i("데이터들 : ", mDatas1.get(i).basket_image);
//                        Log.i("데이터들 : ", "" + mDatas1.get(i).basket_like);
//                        Log.i("데이터들 : ", "" + mDatas1.get(i).is_liked);


                    }
                    adapter.notifyDataSetChanged();
                } else {
//                    Log.i("tag : ", "실패" + result.result.message);
                }
            }

            @Override
            public void onFailure(Call<SearchDataResult> call, Throwable t) {
//                Log.i("tag : ", "실패" + t.getMessage());
            }
        });
    }

    public void processOneMethodAtPosition(final int position){
        //바스켓 담으면 바스켓 담고, 이미지 변경.
        Call<BasketResult> cartResult = mbService.getCartPutResult(mDatas1.get(position).basket_id, token);

        //바스켓 담기 요청
        cartResult.enqueue(new Callback<BasketResult>() {
            @Override
            public void onResponse(Call<BasketResult> call, Response<BasketResult> response) {
                if (response.isSuccessful()) {
                    BasketResult result = response.body();
                    if (result.result == null) {
                        return;
                    }
                    if (result.result.message.equals("like update success")) {
                        //이미지 바꾸고,
                        Toast.makeText(getApplicationContext(), "바스켓을 담았습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "바스켓을 담는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasketResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버와 통신을 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    private View.OnClickListener recylerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //1.리사이클러뷰에 몇번째 항목을 클릭했는지 그 position을 가져오는 것.
            int position = recyclerView.getChildLayoutPosition(v);
            //2.position번째 항목의 Data를 가져오는 방법

            // String basketName = mDatas.get(position).basketName;
            // String downCount = mDatas.get(position).downCount;

            Intent specificBasketIntent = new Intent(getApplicationContext(), SpecificBasketActivity.class);
            //SpecificBasket에 무슨 바스켓을 선택했는지에 대한 정보를 보내줘야함.

            specificBasketIntent.putExtra("basket_id", mDatas1.get(position).basket_id);
            specificBasketIntent.putExtra("basket_name", mDatas1.get(position).basket_name);
            specificBasketIntent.putExtra("basket_image", mDatas1.get(position).basket_image);
            specificBasketIntent.putExtra("basket_like", mDatas1.get(position).basket_like);
            specificBasketIntent.putExtra("is_liked", mDatas1.get(position).is_liked);

            startActivityForResult(specificBasketIntent, REQEUST_CODE_FOR_SPECIFIC_BASKET);
            TaggedBasketListActivity.this.overridePendingTransition( R.anim.slide_in_up, R.anim.hold );

//            Toast.makeText(TaggedBasketListActivity.this, position + "번째 리사이클러뷰 항목 클릭!" + basketName + "/" + downCount, Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener subClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.downBtn:
                    //바스켓 담기|제거버튼
                    AlertDialog.Builder BasketBuilder = new AlertDialog.Builder(v.getContext());
                    BasketBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    BasketBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                            Toast.makeText(v.getContext(), "바스켓을 담았다고 치자", Toast.LENGTH_SHORT).show();
                        }
                    });
                    BasketBuilder.show();
                    break;
            }
        }
    };
}
