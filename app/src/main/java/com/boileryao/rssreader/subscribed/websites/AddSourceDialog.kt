package com.boileryao.rssreader.subscribed.websites

import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.preference.PreferenceManager
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.boileryao.rssreader.R
import com.boileryao.rssreader.SettingsActivity
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.util.ValidateTextWatcher
import com.boileryao.rssreader.util.database.WebsitesDbHelper
import java.net.URL

/**
 * Created by boileryao on 9/21/2017.
 * Class: AddSourceDialog
 */

class AddSourceDialog {
    companion object {
        fun show(activity: Activity) {
            val view = activity.layoutInflater.inflate(R.layout.dialog_add_source
                    , activity.findViewById(R.id.dialog_add_source), false)
            val titleEditText = view.findViewById<View>(R.id.text_title) as TextInputEditText
            val descriptionEditText = view.findViewById<View>(R.id.text_description) as TextInputEditText
            val urlEditText = view.findViewById<View>(R.id.text_url) as TextInputEditText

            urlEditText.addTextChangedListener(ValidateTextWatcher {
                if (urlEditText.text.toString().isEmpty())
                    urlEditText.error = "此项不能为空"
                else urlEditText.error = null
            })

            //auto-fill rss-like url
            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val shouldInterceptClipboard = preferences
                    .getBoolean(SettingsActivity.CHECK_CLIPS, false)
            if (shouldInterceptClipboard) {
                val clipManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (clipManager.hasPrimaryClip()) {
                    val clip = clipManager.primaryClip

                    if (clip.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        val text = clip.getItemAt(0).text
                        val isUrl = preferences.getString(SettingsActivity.MATCHED_REGEX, "")
                                .toRegex().matches(text)
                        if (isUrl) {  // if url-like, fill it into edit text
                            urlEditText.setText(text)
                        }
                    }
                }
            }

            // compose dialog
            var success = false
            AlertDialog.Builder(activity).setView(view)
                    .setPositiveButton("添加", { _, _ ->
                        val website = Website(titleEditText.text.toString()
                                , descriptionEditText.text.toString()
                                , urlEditText.text.toString())
                        try {
                            URL(website.url)  // Check URL
                            if (website.title.isEmpty()) {
                                website.title = website.url.substringAfter("//")
                                        .substringBeforeLast("/")
                            }
                            // fixme, insert is async
                            success = WebsitesDbHelper.getInstance(activity).insert(website)
                        } catch (ignored: Exception) {
                        }
                        val msg = if (success) "添加成功" else "添加失败"
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
                    }).create().show()
        }
    }
}