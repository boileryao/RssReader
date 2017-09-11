package com.boileryao.rssreader

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.View
import android.widget.Toolbar

/**
 * Created by boileryao on 9/10/2017.
 * Class: SettingsActivity
 */
class SettingsActivity : Activity() {
    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setActionBar(toolbar)

        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fragmentManager.beginTransaction().replace(R.id.container, RssPreferenceFragment()).commit()
    }
}

class RssPreferenceFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}