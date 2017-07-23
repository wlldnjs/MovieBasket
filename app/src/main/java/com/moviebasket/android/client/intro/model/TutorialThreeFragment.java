package com.moviebasket.android.client.intro.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moviebasket.android.client.R;

/**
 * Created by LEECM on 2017-01-06.
 */

public class TutorialThreeFragment extends Fragment {
    ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.viewpage_intro_image_view, container, false);

        imageView = (ImageView)view.findViewById(R.id.image_intro);
        //입혀야 할 이미지 파일 튜토리얼3 이미지
        imageView.setImageResource(R.drawable.tutorial_c);

        return view;
    }
}
