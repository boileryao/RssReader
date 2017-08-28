package com.boileryao.rssreader.subscribed

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

class NetworkTask : AsyncTask<Any, Void, Map<Website, List<Article>>>() {
    private lateinit var listener: OnResultListener
    override fun doInBackground(vararg params: Any?): Map<Website, List<Article>>? {
        val urls = params[0] as List<*>
        listener = params[1] as OnResultListener
        val result = mutableMapOf<Website, List<Article>>()

        urls.forEach {
            try {
                val input = SyndFeedInput()
                val feed = input.build(XmlReader(it as URL))
                result.put(Website(feed), feed.entries.map(::Article))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return result
    }

    override fun onPostExecute(result: Map<Website, List<Article>>?) {
        result?.forEach {
            Log.d("TAG", "${it.key} -> ${it.value.size}")
        }
        listener.action(result)
    }
}

interface OnResultListener {
    fun action(data: Map<Website, List<Article>>?)
}