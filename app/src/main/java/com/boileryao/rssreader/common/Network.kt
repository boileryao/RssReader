package com.boileryao.rssreader.common

import android.os.AsyncTask
import android.util.Log
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

/**
 * Created by boileryao on 8/27/2017.
 * Class: Network
 */

class NetworkTask : AsyncTask<Any, Void, Pair<Website, List<Article>>>() {
    private lateinit var listener: OnResultListener
    override fun doInBackground(vararg params: Any?): Pair<Website, List<Article>>? {
        val website = params[0] as Website
        listener = params[1] as OnResultListener

        try {
            val input = SyndFeedInput()
            val url = URL(website.url)
            val feed = input.build(XmlReader(url))
            val newWebsite = Website(feed)
            newWebsite.url = website.url

            return newWebsite to feed.entries.map(::Article)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return website to listOf()
    }

    override fun onPostExecute(result: Pair<Website, List<Article>>) {
        Log.d("TAG", "Fetching: ${result.first}, Item Count: ${result.second.size}")
        listener.action(result)
    }
}

interface OnResultListener {
    fun action(data: Pair<Website, List<Article>>)
}