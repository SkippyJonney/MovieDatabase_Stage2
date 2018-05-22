package com.example.jonathan.moviedatabase_stage1.Utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class PosterLayoutSize {

    private double aspectRatio = 1.5;

    private LinearLayout.LayoutParams params;

    public PosterLayoutSize() {
        //Get Display Width and Set Image View Layout Params
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = Resources.getSystem().getSystem().getDisplayMetrics();

        Integer width = displayMetrics.widthPixels / 2;
        Double height = width * aspectRatio;

        params = new LinearLayout.LayoutParams(
                width,
                height.intValue()

        );
    }

    public LinearLayout.LayoutParams getParams() {
        return params;
    }

}
