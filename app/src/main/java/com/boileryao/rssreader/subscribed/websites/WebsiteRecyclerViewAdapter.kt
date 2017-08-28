package com.boileryao.rssreader.subscribed.websites

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.subscribed.websites.SubscribedFragment.OnWebsiteListInteraction

/**
 * [RecyclerView.Adapter] that can display a [Article] and makes a call to the
 * specified [OnWebsiteListInteraction].
 */
class WebsiteRecyclerViewAdapter(private val values: MutableMap<Website, List<Article>>
                                 , private val listener: OnWebsiteListInteraction?)
    : RecyclerView.Adapter<WebsiteRecyclerViewAdapter.ViewHolder>() {

    fun load(data: Map<Website, List<Article>>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        values.clear()
        values.putAll(data)
        notifyDataSetChanged()
    }


    fun getItem(position: Int): Pair<Website, List<Article>?> {
        val pos = position % values.size
        for ((index, key) in values.keys.withIndex()) {
            if (index == pos)
                return key to values[key]
        }
        throw RuntimeException()  // This is NOT gonna to happen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_website, parent, false)
        view.isClickable = true
        view.isFocusable = true
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.keys.elementAt(position)
        holder.view.setOnClickListener {
            listener?.onWebsiteListFragmentInteraction(item to values[item])
        }

        holder.item = item
        holder.title.text = item.title
        holder.description.text = Html.fromHtml(item.description)
        holder.url.text = item.url
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val description: TextView
        val url: TextView
        var item: Website? = null

        init {
            title = view.findViewById<View>(R.id.title) as TextView
            description = view.findViewById<View>(R.id.description) as TextView
            url = view.findViewById<View>(R.id.url) as TextView
        }

        override fun toString(): String {
            return item.toString()
        }
    }
}