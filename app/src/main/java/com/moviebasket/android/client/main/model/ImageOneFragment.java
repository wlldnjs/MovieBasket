package com.moviebasket.android.client.main.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moviebasket.android.client.R;

/**
 * Created by LEECM on 2017-01-03.
 */

public class ImageOneFragment extends Fragment {

    ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.viewpage_main_image_view, container, false);

        imageView = (ImageView)view.findViewById(R.id.image_page_main);
        imageView.setImageResource(R.drawable.main_text);

        return view;
    }
}
