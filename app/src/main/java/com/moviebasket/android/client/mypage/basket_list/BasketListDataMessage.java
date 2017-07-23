package com.moviebasket.android.client.mypage.basket_list;

import java.util.ArrayList;

/**
 * Created by LEECM on 2016-12-31.
 */

public class BasketListDataMessage {
    public ArrayList<BasketListDatas> baskets;      //가져오는거 성공했을 때 담길 BasketListDatas
    public String message;      // 가져오는거 실패 했을 때.
}