package com.moviebasket.android.client.tag.hashtag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.moviebasket.android.client.R;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.main.MainActivity;
import com.moviebasket.android.client.network.MBService;
import com.moviebasket.android.client.tag.tagged.TaggedBasketListActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashTagActivity extends Activity {

    ImageView backBtnIcon;
    ImageView Homeicon;

    private static final int REQUEST_CODE_FOR_TAGGED = 1005;
    private ProgressDialog mProgressDialog;
    Activity act = this;
    GridView gridView1, gridView2, gridView3, gridView4;


    //텍스트 배열 선언
    ArrayList<String> textArr = new ArrayList<String>();
    ArrayList<String> textArr2 = new ArrayList<String>();
    ArrayList<String> textArr3 = new ArrayList<String>();
    ArrayList<String> textArr4 = new ArrayList<String>();

    //c_id 배열 선언
    ArrayList<Integer> cidArr = new ArrayList<Integer>();
    ArrayList<Integer> cidArr2 = new ArrayList<Integer>();
    ArrayList<Integer> cidArr3 = new ArrayList<Integer>();
    ArrayList<Integer> cidArr4 = new ArrayList<Integer>();

    //통신관련
    private MBService mbService;
    private boolean isSearchSuccess;
    ArrayList<SearchCategoriesData> mDatas = new ArrayList<SearchCategoriesData>();
    ArrayList<SearchCategoriesData> searchcategoriesDatas = new ArrayList<SearchCategoriesData>();
    ArrayList<SearchCategoriesData> searchcategoriesDatas1 = new ArrayList<SearchCategoriesData>();
    ArrayList<SearchCategoriesData> searchcategoriesDatas2 = new ArrayList<SearchCategoriesData>();
    ArrayList<SearchCategoriesData> searchcategoriesDatas3 = new ArrayList<SearchCategoriesData>();

    SearchResult result;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);


        backBtnIcon = (ImageView)findViewById(R.id.backBtnIcon);
        backBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Homeicon = (ImageView)findViewById(R.id.Homeicon);
        Homeicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(HashTagActivity.this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        //String totalRecomendation[]={"1","2","3","4"};// = {"#썸탈 때","#연인","#가족","#혼자"};
//
//        String theme1[] = {"#썸탈 때","#연인","#가족","#혼자","#밤/새벽","#화창한 날","#흐린 날","#테마8"};
//        String theme2[] = {"#우울/슬픔","#스트레스","#지칠 때","#행복/기쁨"};
//        String theme3[] = {"#인물","#시대","#같은 장르","#OST","#홍수빈인성","#줼림줼림","#필주/미국","#김정은/북한"};


        final gridAdapter adapter = new gridAdapter();
        final gridAdapter2 adapter2 = new gridAdapter2();
        final gridAdapter3 adapter3 = new gridAdapter3();
        final gridAdapter4 adapter4 = new gridAdapter4();

        mProgressDialog = new ProgressDialog(HashTagActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("검색중..");
        mProgressDialog.setIndeterminate(true);

        mProgressDialog.show();




//        int total = totalRecomendation.length;
//        int theme1tot = theme1.length;
//        int theme2tot = theme2.length;
//        int theme3tot = theme3.length;

///////////////////////////////// 이제 통신을 시작해 보자
        mbService = ApplicationController.getInstance().getMbService();

        Call<SearchResult> getSearchResult = mbService.getSearchResult();
        getSearchResult.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
//                Log.i("tag : ", "성공");

                result = response.body();
                searchcategoriesDatas = result.result.today_recommand;
                searchcategoriesDatas1 = result.result.categories.big_category_1;
                searchcategoriesDatas2 = result.result.categories.big_category_2;
                searchcategoriesDatas3 = result.result.categories.big_category_3;
                mDatas.clear();
                mDatas.addAll(searchcategoriesDatas);
                //adapter.notifyDataSetChanged();
//                    total = mDatas.toArray(new String[mDatas.size()]);
                for (int i = 0; i < mDatas.size(); i++) {
//                    Log.i("tag", mDatas.get(i).small_category);
                    textArr.add(mDatas.get(i).small_category);
                    cidArr.add(mDatas.get(i).c_id);
                    adapter.notifyDataSetChanged();
                }
                gridView1.setAdapter(adapter);
                mDatas.clear();

                mDatas.addAll(searchcategoriesDatas1);
                for (int i = 0; i < mDatas.size(); i++) {
//                    Log.i("tag", mDatas.get(i).small_category + mDatas.get(i).c_id);
                    textArr2.add(mDatas.get(i).small_category);
                    cidArr2.add(mDatas.get(i).c_id);
                    adapter.notifyDataSetChanged();
                }
                gridView2.setAdapter(adapter2);
                mDatas.clear();

                mDatas.addAll(searchcategoriesDatas2);
                for (int i = 0; i < mDatas.size(); i++) {
//                    Log.i("tag", mDatas.get(i).small_category);
                    textArr3.add(mDatas.get(i).small_category);
                    cidArr3.add(mDatas.get(i).c_id);
                    adapter.notifyDataSetChanged();
                }
                gridView3.setAdapter(adapter3);
                mDatas.clear();

                mDatas.addAll(searchcategoriesDatas3);
                for (int i = 0; i < mDatas.size(); i++) {
//                    Log.i("tag", mDatas.get(i).small_category);
                    textArr4.add(mDatas.get(i).small_category);
                    cidArr4.add(mDatas.get(i).c_id);
                    adapter.notifyDataSetChanged();
                }
                gridView4.setAdapter(adapter4);
                mProgressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
//                Log.i("tag : ", "실패" + t.getMessage());
                mProgressDialog.dismiss();
            }
        });


        ///////////////////////////////통신 끝

        //추천 랜덤추출
        int a[] = new int[4];
//        Random random = new Random();
//        for (int i = 0 ; i < 4 ; i++)
//        {
//            a[i] = random.nextInt(total);
//            for(int j=0 ; j<i ; j++)
//            {
//                if(a[i]==a[j])
//                {
//                    i--;
//                }
//            }
//        }
//        for(int k=0 ; k<4 ; k++)
//        {
//            //textArr.add(totalRecomendation[a[k]]);
//            textArr.add(totalRecomendation[k]);
//        }
        //


        gridView1 = (GridView) findViewById(R.id.gridView1);
        //gridView1.setAdapter(adapter);

        /////theme1
        //추천 랜덤추출
//        int b[] = new int[8];
//        Random random1 = new Random();
//        for (int i = 0 ; i < 8 ; i++)
//        {
//            b[i] = random1.nextInt(theme1tot);
//            for(int j=0 ; j<i ; j++)
//            {
//                if(b[i]==b[j])
//                {
//                    i--;
//                }
//            }
//        }
//        for(int k=0 ; k<8 ; k++)
        {
//            textArr2.add(theme1[b[k]]);
            //    textArr2.add(theme1[k]);
        }
        //


        gridView2 = (GridView) findViewById(R.id.gridView2);
        //gridView2.setAdapter(new gridAdapter2());

/////////
        /////theme2
        //추천 랜덤추출
//        int c[] = new int[4];
//        Random random2 = new Random();
//        for (int i = 0 ; i < 4 ; i++)
//        {
//            c[i] = random2.nextInt(theme2tot);
//            for(int j=0 ; j<i ; j++)
//            {
//                if(c[i]==c[j])
//                {
//                    i--;
//                }
//            }
//        }
//        for(int k=0 ; k<4 ; k++)
//        {
////            textArr3.add(theme2[c[k]]);
//            textArr3.add(theme2[k]);
//        }
//        //


        gridView3 = (GridView) findViewById(R.id.gridView3);
        //gridView3.setAdapter(new gridAdapter3());

/////////

        /////theme3
        //추천 랜덤추출
//        int d[] = new int[8];
//        Random random3 = new Random();
//        for (int i = 0 ; i < 8 ; i++)
//        {
//            d[i] = random3.nextInt(theme3tot);
//            for(int j=0 ; j<i ; j++)
//            {
//                if(d[i]==d[j])
//                {
//                    i--;
//                }
//            }
//        }
//        for(int k=0 ; k<8 ; k++)
//        {
////            textArr4.add(theme3[d[k]]);
//            textArr4.add(theme3[k]);
//        }
        //


        gridView4 = (GridView) findViewById(R.id.gridView4);
        // gridView4.setAdapter(new gridAdapter4());

/////////
    }


    public class gridAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter() {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return textArr.size();    //그리드뷰에 출력할 목록 수

        }

        @Override
        public Object getItem(int position) {
            return textArr.get(position);    //아이템을 호출할 때 사용하는 메소드
        }

        @Override
        public long getItemId(int position) {
            return position;    //아이템의 아이디를 구할 때 사용하는 메소드
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.search_grid_item, parent, false);
            }

            final Button btn =(Button)convertView.findViewById(R.id.gridItem);

            btn.setText(textArr.get(position));
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //이미지를 터치했을때 동작하는 곳
//                    Toast.makeText(getApplicationContext(),btn.getText().toString(),Toast.LENGTH_SHORT).show();

                    //어떤 해쉬태그를 선택했는지를 인텐트로 보내줘야함.
                    Intent taggedBasketIntent = new Intent(HashTagActivity.this, TaggedBasketListActivity.class);

                    //putExtra 이용하여 누른 버튼의 Text를 다음화면으로 보내줌
                    taggedBasketIntent.putExtra("hash_title",btn.getText().toString());
                    taggedBasketIntent.putExtra("c_id",cidArr.get(position));
                    startActivityForResult(taggedBasketIntent, REQUEST_CODE_FOR_TAGGED);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
                }
            });

            return convertView;
        }
    }

    ////그리드2(theme1)
    public class gridAdapter2 extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter2() {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return textArr2.size();    //그리드뷰에 출력할 목록 수

        }

        @Override
        public Object getItem(int position) {
            return textArr2.get(position);    //아이템을 호출할 때 사용하는 메소드
        }

        @Override
        public long getItemId(int position) {
            return position;    //아이템의 아이디를 구할 때 사용하는 메소드
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.search_grid_item, parent, false);
            }

            final Button btn2 =(Button)convertView.findViewById(R.id.gridItem);

            btn2.setText(textArr2.get(position));
            btn2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //이미지를 터치했을때 동작하는 곳
//                    Toast.makeText(getApplicationContext(),btn2.getText().toString(),Toast.LENGTH_SHORT).show();

                    //어떤 해쉬태그를 선택했는지를 인텐트로 보내줘야함.
                    Intent taggedBasketIntent = new Intent(HashTagActivity.this, TaggedBasketListActivity.class);

                    //putExtra 이용하여 누른 버튼의 Text를 다음화면으로 보내줌
                    taggedBasketIntent.putExtra("hash_title",btn2.getText().toString());
                    taggedBasketIntent.putExtra("c_id",cidArr2.get(position));
                    startActivityForResult(taggedBasketIntent, REQUEST_CODE_FOR_TAGGED);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.hold);

                }
            });

            return convertView;
        }
    }

    ////그리드2(theme2)
    public class gridAdapter3 extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter3() {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return textArr3.size();    //그리드뷰에 출력할 목록 수

        }

        @Override
        public Object getItem(int position) {
            return textArr3.get(position);    //아이템을 호출할 때 사용하는 메소드
        }

        @Override
        public long getItemId(int position) {
            return position;    //아이템의 아이디를 구할 때 사용하는 메소드
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.search_grid_item, parent, false);
            }

            final Button btn3 =(Button)convertView.findViewById(R.id.gridItem);

            btn3.setText(textArr3.get(position));
            btn3.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //이미지를 터치했을때 동작하는 곳
//                    Toast.makeText(getApplicationContext(),btn3.getText().toString(),Toast.LENGTH_SHORT).show();

                    //어떤 해쉬태그를 선택했는지를 인텐트로 보내줘야함.
                    Intent taggedBasketIntent = new Intent(HashTagActivity.this, TaggedBasketListActivity.class);

                    //putExtra 이용하여 누른 버튼의 Text를 다음화면으로 보내줌
                    taggedBasketIntent.putExtra("hash_title",btn3.getText().toString());
                    taggedBasketIntent.putExtra("c_id",cidArr3.get(position));
                    startActivityForResult(taggedBasketIntent, REQUEST_CODE_FOR_TAGGED);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.hold);

                }
            });

            return convertView;
        }
    }

    ////그리드2(theme3)
    public class gridAdapter4 extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter4() {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return textArr4.size();    //그리드뷰에 출력할 목록 수

        }

        @Override
        public Object getItem(int position) {
            return textArr4.get(position);    //아이템을 호출할 때 사용하는 메소드
        }

        @Override
        public long getItemId(int position) {
            return position;    //아이템의 아이디를 구할 때 사용하는 메소드
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.search_grid_item, parent, false);
            }

            final Button btn4 =(Button)convertView.findViewById(R.id.gridItem);

            btn4.setText(textArr4.get(position));
            btn4.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //이미지를 터치했을때 동작하는 곳
//                    Toast.makeText(getApplicationContext(),btn4.getText().toString() ,Toast.LENGTH_SHORT).show();


                    //어떤 해쉬태그를 선택했는지를 인텐트로 보내줘야함.
                    Intent taggedBasketIntent = new Intent(HashTagActivity.this, TaggedBasketListActivity.class);

                    //putExtra 이용하여 누른 버튼의 Text를 다음화면으로 보내줌
                    taggedBasketIntent.putExtra("hash_title",btn4.getText().toString());
                    taggedBasketIntent.putExtra("c_id",cidArr4.get(position));
                    startActivityForResult(taggedBasketIntent, REQUEST_CODE_FOR_TAGGED);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.hold);


                }
            });

            return convertView;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_FOR_TAGGED:
                if(resultCode==RESULT_OK){

                }
                break;
        }
    }
}