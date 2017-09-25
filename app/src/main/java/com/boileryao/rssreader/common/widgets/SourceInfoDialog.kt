package com.boileryao.rssreader.common.widgets

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
import com.boileryao.rssreader.common.ValidateTextWatcher
import com.boileryao.rssreader.common.database.WebsitesDbHelper
import java.net.URL

/**
 * Created by boileryao on 9/21/2017.
 * Class: SourceInfoDialog
 */

class SourceInfoDialog {
    companion object {
        fun show(activity: Activity, which: Website? = null) {
            val view = activity.layoutInflater.inflate(R.layout.dialog_add_source
                    , activity.findViewById(R.id.dialog_add_source), false)
            val titleEditText = view.findViewById<View>(R.id.text_title) as TextInputEditText
            val descriptionEditText = view.findViewById<View>(R.id.text_description) as TextInputEditText
            val urlEditText = view.findViewById<View>(R.id.text_url) as TextInputEditText

            // compose dialog
            var success = false
            val dialog = AlertDialog.Builder(activity)
                    .setView(view)
                    .setPositiveButton("添加", { _, _ ->
                        val website = Website(titleEditText.text.toString()
                                , descriptionEditText.text.toString()
                                , urlEditText.text.toString())
                        try {
                            tryComplete(website)
                            URL(website.url)  // Check URL
                            // fixme, insert is async
                            success = WebsitesDbHelper.getInstance(activity).insert(website)
                        } catch (ignored: Exception) {
                        }
                        val msg = if (success) "添加成功" else "添加失败"
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
                    })
                    .create()

            if (which != null) {
                titleEditText.setText(which.title)
                descriptionEditText.setText(which.description)
                urlEditText.setText(which.url)
                dialog.show()
                return
            }

            urlEditText.addTextChangedListener(ValidateTextWatcher {
                if (urlEditText.text.toString().isEmpty())
                    urlEditText.error = "此项不能为空"
                else urlEditText.error = null
            })

            //auto-fill rss-like url, if not in modification mode and config is yes
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
            dialog.show()
        }

        private fun tryComplete(website: Website) {
            val title = website.title
            if (title.isEmpty()) return

            if (!website.url.startsWith("http")) {
                website.url = title.prependIndent("http://")
            }

            if (website.description.isEmpty()) {
                website.description = website.url
            }

            if (website.title.isEmpty()) {
                val result = "https?://([^/]+)/?.*".toRegex().matchEntire(website.url)?.groupValues
                website.title = if (result != null && result.size > 1) result[1] else website.url
            }
        }
    }
}