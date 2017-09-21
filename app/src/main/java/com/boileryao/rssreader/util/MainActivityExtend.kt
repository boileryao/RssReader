package com.boileryao.rssreader.util

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import com.boileryao.rssreader.MainActivity
import com.boileryao.rssreader.R
import com.boileryao.rssreader.SettingsActivity
import com.boileryao.rssreader.subscribed.websites.AddSourceDialog

/**
 * Created by boileryao on 8/30/2017.
 * Class: activities
 */

fun MainActivity.handleMenuItemClick(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.nav_add -> {
            AddSourceDialog.show(this)
            return false
        }
        R.id.nav_collection -> {
            //todo
        }
        R.id.nav_subscribe -> {
            //todo place the Subscribed Fragment
        }
        R.id.nav_settings -> {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
        R.id.nav_share -> {
            //todo share
        }
        R.id.nav_about -> {
            //todo
        }
    }
    return true
}

class ValidateTextWatcher(private val action: () -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        Log.d("TAG", p0.toString())
        action.invoke()
    }

}