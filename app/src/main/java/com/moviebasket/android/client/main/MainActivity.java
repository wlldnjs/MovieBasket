package com.moviebasket.android.client.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moviebasket.android.client.R;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.main.model.NewFragment;
import com.moviebasket.android.client.main.model.PopularFragment;
import com.moviebasket.android.client.main.model.RecommendFragment;
import com.moviebasket.android.client.main.presenter.ImagePagerAdatper;
import com.moviebasket.android.client.main.presenter.PagerAdapter;
import com.moviebasket.android.client.mypage.basket_list.BasketListActivity;
import com.moviebasket.android.client.mypage.movie_pack_list.MoviePackActivity;
import com.moviebasket.android.client.mypage.movie_rec_list.MovieRecActivity;
import com.moviebasket.android.client.mypage.setting.SettingActivity;
import com.moviebasket.android.client.mypage.setting.SettingResult;
import com.moviebasket.android.client.network.MBService;
import com.moviebasket.android.client.splash.SplashActivity;
import com.moviebasket.android.client.tag.hashtag.HashTagActivity;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String member_token;
    private static final int REQEUST_CODE_FOR_BASKET_LIST = 1000;
    private static final int REQEUST_CODE_FOR_MOVIE_PACK = 1001;
    private static final int REQEUST_CODE_FOR_MOVIE_REC = 1002;
    private static final int REQEUST_CODE_FOR_TEST = 1003;
    private static final int REQEUST_CODE_FOR_PRACTICE = 1004;
    private static final int REQEUST_CODE_FOR_SPECIFIC_BASKET = 1005;
    private static final int REQEUST_CODE_FOR_HASHTAG = 1006;
    private static final int REQEUST_CODE_FOR_SETTING = 1007;
    private boolean isResponseSuccess;

    MBService mbService;

    DrawerLayout drawerLayout;
    LinearLayout linearLayout;
    ImageView btn_toggle, btn_tag, newbtn, popularbtn, recommendbtn;
    LinearLayout mypage_basket_btn, mypage_movie_btn, mypage_reco_movie, mypage_mypage_logout_btn, mypage_setting_btn;
    TextView mypage_username;
    TextView eventBlocker1, eventBlocker2, eventBlocker3, eventBlocker4;
    CircleImageView userimage;

    Fragment recommendFragment, popularFragment, newFragment;

    ViewPager viewPager;
    ViewPager imageViewPager;
    PagerAdapter pagerAdapter;
    ImagePagerAdatper imagePagerAdapter;

    ImageView imagefragImg1;
    ImageView imagefragImg2;
    ImageView imagefragImg3;

    int pageNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        member_token = ApplicationController.getInstance().getPreferences();
        mbService = ApplicationController.getInstance().getMbService();

        //변수초기화
        pageNumber = 0;
        drawerLayout = (DrawerLayout) findViewById(R.id.dllayout_drawer_main);
        linearLayout = (LinearLayout) findViewById(R.id.lilayout_nav_drawer_main);
        btn_toggle = (ImageView) findViewById(R.id.btn_toggle_drawer_main);
        btn_tag = (ImageView) findViewById(R.id.btn_tag_main);
        newbtn = (ImageView) findViewById(R.id.newbtn);
        popularbtn = (ImageView) findViewById(R.id.popularbtn);
        recommendbtn = (ImageView) findViewById(R.id.recommendbtn);
        imagefragImg1 = (ImageView) findViewById(R.id.imagefragment_one);
        imagefragImg2 = (ImageView) findViewById(R.id.imagefragment_two);
        imagefragImg3 = (ImageView) findViewById(R.id.imagefragment_three);
        mypage_username = (TextView) findViewById(R.id.mypage_username);
        userimage = (CircleImageView) findViewById(R.id.my_image);
        mypage_basket_btn = (LinearLayout) findViewById(R.id.mypage_basket_btn);
        mypage_movie_btn = (LinearLayout) findViewById(R.id.mypage_movie_btn);
        mypage_reco_movie = (LinearLayout) findViewById(R.id.mypage_reco_movie);
        mypage_mypage_logout_btn = (LinearLayout) findViewById(R.id.mypage_mypage_logout_btn);
        mypage_setting_btn = (LinearLayout) findViewById(R.id.mypage_setting_btn);
        eventBlocker1 = (TextView)findViewById(R.id.eventBlocker1);
        eventBlocker2 = (TextView)findViewById(R.id.eventBlocker2);
        eventBlocker3 = (TextView)findViewById(R.id.eventBlocker3);
        eventBlocker4 = (TextView)findViewById(R.id.eventBlocker4);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        imagePagerAdapter = new ImagePagerAdatper(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        imageViewPager.setAdapter(imagePagerAdapter);

        recommendFragment = pagerAdapter.getItem(0);
        popularFragment = pagerAdapter.getItem(1);
        newFragment = pagerAdapter.getItem(2);

        //페이저
        viewPager.setOffscreenPageLimit(2);

        btn_toggle.setOnClickListener(clickListener);
        btn_tag.setOnClickListener(clickListener);
        newbtn.setOnClickListener(clickListener);
        popularbtn.setOnClickListener(clickListener);
        recommendbtn.setOnClickListener(clickListener);

        mypage_basket_btn.setOnClickListener(drawerClickListener);
        mypage_movie_btn.setOnClickListener(drawerClickListener);
        mypage_reco_movie.setOnClickListener(drawerClickListener);
        mypage_mypage_logout_btn.setOnClickListener(drawerClickListener);
        mypage_setting_btn.setOnClickListener(drawerClickListener);
        eventBlocker1.setOnClickListener(drawerClickListener);
        eventBlocker2.setOnClickListener(drawerClickListener);
        eventBlocker3.setOnClickListener(drawerClickListener);
        eventBlocker4.setOnClickListener(drawerClickListener);

        //유저의 개인정보 가져오기.
        requestProfile();

        // 초기화면은 추천순으로 정렬.
        recommendbtn.setBackgroundResource(R.drawable.main_reco_black);
        popularbtn.setBackgroundResource(R.drawable.main_pop);
        newbtn.setBackgroundResource(R.drawable.main_recent);
        viewPager.setCurrentItem(0);

        // 이미지뷰 프래그먼트 자동슬라이딩을 위한 코드영역
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (pageNumber == 3)
                    pageNumber = 0;
                imageViewPager.setCurrentItem(pageNumber, true);

                //동그라미 점
                switch (pageNumber) {
                    case 0:
                        imagefragImg1.setImageResource(R.drawable.main_circle);
                        imagefragImg2.setImageResource(R.drawable.main_no_circle);
                        imagefragImg3.setImageResource(R.drawable.main_no_circle);
                        break;
                    case 1:
                        imagefragImg1.setImageResource(R.drawable.main_no_circle);
                        imagefragImg2.setImageResource(R.drawable.main_circle);
                        imagefragImg3.setImageResource(R.drawable.main_no_circle);
                        break;
                    case 2:
                        imagefragImg1.setImageResource(R.drawable.main_no_circle);
                        imagefragImg2.setImageResource(R.drawable.main_no_circle);
                        imagefragImg3.setImageResource(R.drawable.main_circle);
                        break;
                }
                pageNumber++;
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 500, 2700);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        recommendbtn.setBackgroundResource(R.drawable.main_reco_black);
                        popularbtn.setBackgroundResource(R.drawable.main_pop);
                        newbtn.setBackgroundResource(R.drawable.main_recent);

                        break;
                    case 1:
                        recommendbtn.setBackgroundResource(R.drawable.main_reco);
                        popularbtn.setBackgroundResource(R.drawable.main_pop_black);
                        newbtn.setBackgroundResource(R.drawable.main_recent);

                        break;

                    case 2:
                        recommendbtn.setBackgroundResource(R.drawable.main_reco);
                        popularbtn.setBackgroundResource(R.drawable.main_pop);
                        newbtn.setBackgroundResource(R.drawable.main_recent_black);

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        imagefragImg1.setImageResource(R.drawable.main_circle);
                        imagefragImg2.setImageResource(R.drawable.main_no_circle);
                        imagefragImg3.setImageResource(R.drawable.main_no_circle);
                        break;
                    case 1:
                        imagefragImg1.setImageResource(R.drawable.main_no_circle);
                        imagefragImg2.setImageResource(R.drawable.main_circle);
                        imagefragImg3.setImageResource(R.drawable.main_no_circle);
                        break;

                    case 2:
                        imagefragImg1.setImageResource(R.drawable.main_no_circle);
                        imagefragImg2.setImageResource(R.drawable.main_no_circle);
                        imagefragImg3.setImageResource(R.drawable.main_circle);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        //드로워 열려 있으면 닫기만 하기.
        if (drawerLayout.isDrawerOpen(linearLayout)) {
            drawerLayout.closeDrawer(linearLayout);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.super.onBackPressed();
            }
        });
        builder.setMessage("무비바스켓을 종료하시겠습니까?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //메인 버튼들을 위한 이벤트리스너
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_tag_main:
                    Intent tagIntent = new Intent(MainActivity.this, HashTagActivity.class);
                    startActivityForResult(tagIntent, REQEUST_CODE_FOR_HASHTAG);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
                    break;
                case R.id.btn_toggle_drawer_main:
                    drawerLayout.openDrawer(linearLayout);
                    break;
                case R.id.newbtn:
                    newbtn.setBackgroundResource(R.drawable.main_recent_black);
                    popularbtn.setBackgroundResource(R.drawable.main_pop);
                    recommendbtn.setBackgroundResource(R.drawable.main_reco);
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.popularbtn:
                    newbtn.setBackgroundResource(R.drawable.main_recent);
                    popularbtn.setBackgroundResource(R.drawable.main_pop_black);
                    recommendbtn.setBackgroundResource(R.drawable.main_reco);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.recommendbtn:
                    newbtn.setBackgroundResource(R.drawable.main_recent);
                    popularbtn.setBackgroundResource(R.drawable.main_pop);
                    recommendbtn.setBackgroundResource(R.drawable.main_reco_black);
                    viewPager.setCurrentItem(0);
                    break;
            }
        }
    };

    private View.OnClickListener drawerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //마이페이지
                case R.id.mypage_username:
                    break;
                case R.id.mypage_basket_btn:
                    Intent BasketListIntent = new Intent(MainActivity.this, BasketListActivity.class);
                    startActivityForResult(BasketListIntent, REQEUST_CODE_FOR_BASKET_LIST);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    break;
                case R.id.mypage_movie_btn:
                    Intent moviePackIntent = new Intent(MainActivity.this, MoviePackActivity.class);
                    startActivityForResult(moviePackIntent, REQEUST_CODE_FOR_MOVIE_PACK);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    break;
                case R.id.mypage_reco_movie:
                    Intent movieRecIntent = new Intent(MainActivity.this, MovieRecActivity.class);
                    startActivityForResult(movieRecIntent, REQEUST_CODE_FOR_MOVIE_REC);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    break;
                case R.id.mypage_mypage_logout_btn:
                    //토큰값 지우기
                    ApplicationController.getInstance().savePreferences("");
                    //스플래시 화면으로 가기.
                    Intent logoutIntent = new Intent(MainActivity.this, SplashActivity.class);
                    //액티비티 스택 clear
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logoutIntent);
                    finish();
                    break;
                case R.id.mypage_setting_btn:
                    Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivityForResult(settingIntent, REQEUST_CODE_FOR_SETTING);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    break;
                case R.id.eventBlocker1:
                    break;
                case R.id.eventBlocker2:
                    break;
                case R.id.eventBlocker3:
                    break;
                case R.id.eventBlocker4:
                    break;
            }
            drawerLayout.closeDrawer(linearLayout);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQEUST_CODE_FOR_BASKET_LIST:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_MOVIE_PACK:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_MOVIE_REC:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_TEST:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_PRACTICE:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_SPECIFIC_BASKET:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_HASHTAG:
                if (resultCode == RESULT_OK) {

                }
                break;
            case REQEUST_CODE_FOR_SETTING:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 개인정보 리로딩
        requestProfile();

        ((RecommendFragment)recommendFragment).loadBasketListDatas(1);
        ((PopularFragment)popularFragment).loadBasketListDatas(3);
        ((NewFragment)newFragment).loadBasketListDatas(2);

    }

    private void requestProfile(){
        //유저의 개인정보 가져오기.
        String changedToken = ApplicationController.getInstance().getPreferences();
        Call<SettingResult> getSettingResult = mbService.getSettingResult(changedToken);
        getSettingResult.enqueue(new Callback<SettingResult>() {
            @Override
            public void onResponse(Call<SettingResult> call, Response<SettingResult> response) {
                SettingResult settingResult = response.body();
                if (response.isSuccessful()) {// 응답코드 200
                    isResponseSuccess = settingResult.result.message == null ? true : false;
                }
                if (isResponseSuccess) {
                    mypage_username.setText(String.valueOf(settingResult.result.member_name));
                    if (!(settingResult.result.member_image == null || settingResult.result.member_image.equals(""))) {
                        Glide.with(MainActivity.this).load(String.valueOf(settingResult.result.member_image)).into(userimage);
                    }else {
                        userimage.setImageResource(R.drawable.mypage_myimage);
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "개인정보를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }

        });
    }
}