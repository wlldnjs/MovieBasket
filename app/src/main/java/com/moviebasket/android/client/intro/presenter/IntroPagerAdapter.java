package com.moviebasket.android.client.intro.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.moviebasket.android.client.intro.model.TutorialEndFragment;
import com.moviebasket.android.client.intro.model.TutorialFiveFragment;
import com.moviebasket.android.client.intro.model.TutorialFourFragment;
import com.moviebasket.android.client.intro.model.TutorialOneFragment;
import com.moviebasket.android.client.intro.model.TutorialSixFragment;
import com.moviebasket.android.client.intro.model.TutorialThreeFragment;
import com.moviebasket.android.client.intro.model.TutorialTwoFragment;

/**
 * Created by kh on 2016. 12. 7..
 */
public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    public Fragment[] fragments = new Fragment[7];

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new TutorialOneFragment();
        fragments[1] = new TutorialTwoFragment();
        fragments[2] = new TutorialThreeFragment();
        fragments[3] = new TutorialFourFragment();
        fragments[4] = new TutorialFiveFragment();
        fragments[5] = new TutorialSixFragment();
        fragments[6] = new TutorialEndFragment();
    }
    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }
    public int getCount() {
        return fragments.length;
    }


}
