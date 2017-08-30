package com.boileryao.rssreader.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.boileryao.rssreader.R

/**
 * Created by boileryao on 8/28/2017.
 * Class: fragments
 */

infix fun FragmentManager.replaceMainFragmentTo(fragment: Fragment) {
    this.beginTransaction()
            .replace(R.id.main_fragment, fragment)
            .addToBackStack(null)
            .commit()
}