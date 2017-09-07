package com.boileryao.rssreader.subscribed.articles

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.boileryao.rssreader.R
import com.boileryao.rssreader.bean.Article

/**
 * [RecyclerView.Adapter] that can display a [Article] and makes a call to the
 * specified OnWebsiteListInteraction.
 */
class ArticleRecyclerViewAdapter(private val values: MutableList<Article>)
    : RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>() {

    fun load(data: List<Article>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        values.clear()
        values.addAll(data)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Article {
        return values[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        view.isClickable = true
        view.isFocusable = true
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

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
        var item: Article? = null

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