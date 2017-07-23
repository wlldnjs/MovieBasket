package com.moviebasket.android.client.main.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.moviebasket.android.client.main.model.NewFragment;
import com.moviebasket.android.client.main.model.PopularFragment;
import com.moviebasket.android.client.main.model.RecommendFragment;

/**
 * Created by kh on 2016. 12. 7..
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    public Fragment[] fragments = new Fragment[3];

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        Log.i("SortNumber", "rec: "+"PagerAdapter new RecommendFragment() 이전");
        fragments[0] = new RecommendFragment();
        Log.i("SortNumber", "rec: "+"PagerAdapter new RecommendFragment() 이후");
        Log.i("SortNumber", "pop: "+"PagerAdapter new PopularFragment() 이전");
        fragments[1] = new PopularFragment();
        Log.i("SortNumber", "pop: "+"PagerAdapter new PopularFragment() 이후");
        Log.i("SortNumber", "new: "+"PagerAdapter new NewFragment() 이전");
        fragments[2] = new NewFragment();
        Log.i("SortNumber", "new: "+"PagerAdapter new NewFragment() 이후");
    }
    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }
    public int getCount() {
        return fragments.length;
    }


}
