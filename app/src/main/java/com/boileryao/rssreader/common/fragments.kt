package com.boileryao.rssreader.common

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
            .commit()
}