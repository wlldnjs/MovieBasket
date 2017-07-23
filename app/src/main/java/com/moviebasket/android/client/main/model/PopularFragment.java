package com.moviebasket.android.client.main.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moviebasket.android.client.R;
import com.moviebasket.android.client.basket_detail.SpecificBasketActivity;
import com.moviebasket.android.client.clickable.OneClickable;
import com.moviebasket.android.client.global.ApplicationController;
import com.moviebasket.android.client.mypage.basket_list.BasketListDataResult;
import com.moviebasket.android.client.mypage.basket_list.BasketListDatas;
import com.moviebasket.android.client.mypage.basket_list.BasketResult;
import com.moviebasket.android.client.network.MBService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by kh on 2017. 1. 1..
 */

public class PopularFragment extends Fragment implements OneClickable {

    public PopularFragment(){

//        Log.i("SortNumber", "pop: "+"popfrag 생성자");
    }

    private static final int REQEUST_CODE_FOR_SPECIFIC_BASKET = 1005;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<BasketListDatas> basketListDatases;
    MainAdapter mainAdapter;
ImageView moveTopBtn;

    MBService mbService;
    private String member_token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.viewpage_main_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerview);

        mbService = ApplicationController.getInstance().getMbService();

        member_token = ApplicationController.getInstance().getPreferences();

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        basketListDatases = new ArrayList<>();
        mainAdapter = new MainAdapter(basketListDatases, recylerClickListener, this);

        if(basketListDatases!=null)
            basketListDatases.clear();
//        Log.i("SortNumber", "pop: "+3);
        loadBasketListDatas(3);

        moveTopBtn = (ImageView)view.findViewById(R.id.moveTopBtn);
        moveTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                //상단이동
                if (newState == SCROLL_STATE_DRAGGING){
                    moveTopBtn.setVisibility(View.INVISIBLE);
                }
                else if(newState == SCROLL_STATE_IDLE)
                    moveTopBtn.setVisibility(View.VISIBLE);

                if(firstPosition <= 0) {
                    moveTopBtn.setVisibility(View.INVISIBLE);
                }
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollExtend = recyclerView.computeVerticalScrollExtent();
                int scrollRange = recyclerView.computeVerticalScrollRange();

                if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {
//                    Toast.makeText(getActivity(), "맨아래", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });

        recyclerView.setAdapter(mainAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener recylerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

//            Toast.makeText(getActivity(), position + "번째 리사이클러뷰 항목 클릭!" + " / " + basketListDatases.get(position).basket_name, Toast.LENGTH_SHORT).show();

            Intent specificBasketIntent = new Intent(getContext(), SpecificBasketActivity.class);
            //SpecificBasket에 무슨 바스켓을 선택했는지에 대한 정보를 보내줘야함.

            specificBasketIntent.putExtra("basket_id", basketListDatases.get(position).basket_id);
            specificBasketIntent.putExtra("basket_name", basketListDatases.get(position).basket_name);
            specificBasketIntent.putExtra("basket_image", basketListDatases.get(position).basket_image);
            specificBasketIntent.putExtra("basket_like", basketListDatases.get(position).basket_like);
            specificBasketIntent.putExtra("is_liked", basketListDatases.get(position).is_liked);

            startActivityForResult(specificBasketIntent, REQEUST_CODE_FOR_SPECIFIC_BASKET);
            getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.hold );
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


    public void loadBasketListDatas(int mode) {
        if(mbService==null){
//            Log.i("NetConfirm"  , "loadBasketListDatas: pop : mbServer is null  :" );
            mbService = ApplicationController.getInstance().getMbService();
        }
        String changedToken = ApplicationController.getInstance().getPreferences();
        Call<BasketListDataResult> getRecommendedBasketList = mbService.getBasketListDataResultOrderBy(changedToken, mode);
        getRecommendedBasketList.enqueue(new Callback<BasketListDataResult>() {
            @Override
            public void onResponse(Call<BasketListDataResult> call, Response<BasketListDataResult> response) {
                //바스켓리스트 가져옴.
                BasketListDataResult result = response.body();
                String message = result.result.message;
                if (message == null) {
                    if(basketListDatases==null) {
                        basketListDatases.addAll(result.result.baskets);
                    }else{
                        basketListDatases.clear();
                        basketListDatases.addAll(result.result.baskets);
                    }
                    mainAdapter = new MainAdapter(basketListDatases, recylerClickListener, PopularFragment.this);
                    recyclerView.setAdapter(mainAdapter);
                    mainAdapter.notifyDataSetChanged();
//                    Log.i("NetConfirm", "PopularFrament 바스켓리스트 가져옴.");
                } else {
                    basketListDatases = new ArrayList<BasketListDatas>();
                    Toast.makeText(getActivity(), "바스켓 리스트를 가져오는 데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasketListDataResult> call, Throwable t) {
                //바스켓리스트를 가져오는데 실패함
                Toast.makeText(getActivity(), "서버와 연결에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void processOneMethodAtPosition(final int position) {
        //바스켓 담으면 바스켓 담고, 이미지 변경.
        Call<BasketResult> cartResult = mbService.getCartPutResult(basketListDatases.get(position).basket_id, member_token);

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
                        Toast.makeText(getContext(), "바스켓을 담았습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "바스켓을 담는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasketResult> call, Throwable t) {
                Toast.makeText(getContext(), "서버와 통신을 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}