package com.moviebasket.android.client.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.moviebasket.android.client.R;
import com.moviebasket.android.client.intro.presenter.IntroPagerAdapter;
import com.moviebasket.android.client.login.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    private static final int TOTAL_PAGE_NUMBER = 6; //인덱스 아님. 페이지 갯수임. EndPage는 제외한 갯수임.
    int pageNumber;

    ViewPager introViewPager;
    IntroPagerAdapter introPagerAdapter;
    ImageView[] dotIndicators = new ImageView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        pageNumber = 0;

        introViewPager = (ViewPager) findViewById(R.id.introViewPager);
        introPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        introViewPager.setAdapter(introPagerAdapter);

        dotIndicators[0] = (ImageView) findViewById(R.id.dot1);
        dotIndicators[1] = (ImageView) findViewById(R.id.dot2);
        dotIndicators[2] = (ImageView) findViewById(R.id.dot3);
        dotIndicators[3] = (ImageView) findViewById(R.id.dot4);
        dotIndicators[4] = (ImageView) findViewById(R.id.dot5);
        dotIndicators[5] = (ImageView) findViewById(R.id.dot6);

        //초기화면 설정
        introViewPager.setCurrentItem(0);
        dotIndicators[0].setImageResource(R.drawable.circle);

        introViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == TOTAL_PAGE_NUMBER){
                    //로그인 화면으로 이동.
                    Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.slide_in_right_faster, R.anim.slide_out_left);
                    finish();
                }

                for(int i=0; i<dotIndicators.length; i++){
                    dotIndicators[i].setImageResource(R.drawable.circle_opacity);
                }
                //DotIndicator 활성화
                if(position!=TOTAL_PAGE_NUMBER)
                    dotIndicators[position].setImageResource(R.drawable.circle);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
