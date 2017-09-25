package com.boileryao.rssreader.common.recyclerview;

import android.view.View;

/**
 * Created by boileryao on 8/28/2017.
 * Class: ClickListener
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}