package com.boileryao.rssreader

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
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

        PreferenceManager.setDefaultValues(this, R.xml.preference, false)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setActionBar(toolbar)

        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fragmentManager.beginTransaction().replace(R.id.container, RssPreferenceFragment()).commit()
    }

    companion object {
        val WIFI_ONLY = "wifi_only"
        val REFRESH_ON_START = "refresh_all_on_startup"
        val CHECK_CLIPS = "check_clipboard"
        val MATCHED_REGEX = "url_regex"
    }
}

class RssPreferenceFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}