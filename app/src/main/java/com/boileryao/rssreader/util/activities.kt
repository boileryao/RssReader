package com.boileryao.rssreader.util

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.boileryao.rssreader.MainActivity
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.util.database.WebsitesDbHelper
import java.net.URL

/**
 * Created by boileryao on 8/30/2017.
 * Class: activities
 */

fun MainActivity.handleMenuItemClick(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.nav_add -> {
            val view = layoutInflater.inflate(R.layout.dialog_add_source
                    , findViewById(R.id.dialog_add_source), false)
            val title = view.findViewById<View>(R.id.text_title) as TextInputEditText
            val description = view.findViewById<View>(R.id.text_description) as TextInputEditText
            val url = view.findViewById<View>(R.id.text_url) as TextInputEditText

            url.addTextChangedListener(ValidateTextWatcher {
                if (url.text.toString().isEmpty())
                    url.error = "此项不能为空"
                else url.error = null
            })

            //auto-fill rss-like url
            val clipManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipManager.primaryClip
            if (clip.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val text = clip.getItemAt(0).text
                val isUrl = text.endsWith(".xml")
                        || text.contains("/") && (text.contains("rss") || text.contains("feed"))
                if (isUrl) {
                    url.setText(text)
                }
            }

            AlertDialog.Builder(this).setView(view)
                    .setPositiveButton("添加", { _, _ ->
                        val website = Website(title.text.toString(), description.text.toString(), url.text.toString())
                        try {
                            URL(website.url)
                            if (website.title.isEmpty()) {
                                website.title = website.url.substringBefore("/")
                            }
                            WebsitesDbHelper.getInstance(applicationContext).insert(website)
                            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
                        } catch (ignored: Exception) {
                            Toast.makeText(this, "添加失败", Toast.LENGTH_LONG).show()
                        }
                    }).create().show()
            return false
        }
        R.id.nav_collection -> {
            //todo
        }
        R.id.nav_subscribe -> {
            //todo place the Subscribed Fragment
        }
        R.id.nav_settings -> {
            //todo
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