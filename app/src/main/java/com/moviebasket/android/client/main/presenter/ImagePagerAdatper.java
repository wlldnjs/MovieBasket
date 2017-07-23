package com.moviebasket.android.client.main.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.moviebasket.android.client.main.model.ImageOneFragment;
import com.moviebasket.android.client.main.model.ImageThreeFragment;
import com.moviebasket.android.client.main.model.ImageTwoFragment;

/**
 * Created by LEECM on 2017-01-03.
 */

public class ImagePagerAdatper extends FragmentStatePagerAdapter {

    public Fragment[] fragments = new Fragment[3];

    public ImagePagerAdatper(FragmentManager fm) {
        super(fm);
        fragments[0] = new ImageOneFragment();
        fragments[1] = new ImageTwoFragment();
        fragments[2] = new ImageThreeFragment();
    }
    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }
    public int getCount() {
        return fragments.length;
    }
}
