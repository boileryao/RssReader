package com.boileryao.rssreader.bean

import com.rometools.rome.feed.synd.SyndFeed
import java.io.Serializable

/**
 * Created by boileryao on 8/28/2017.
 * Class: Website
 */

class Website(entry: SyndFeed?) : Serializable {
    var title = unknown
    var description = unknown
    var url = unknown
    var author = unknown

    init {
        if (entry != null) {
            title = entry.title
            description = entry.description.replace("<img.+?>".toRegex(), "")
            url = entry.link
            author =
                    if (entry.authors != null && entry.authors.size > 0) entry.authors[0].name
                    else entry.author ?: ""
        }
    }


    constructor(title: String, description: String, url: String) : this(null) {
        this.title = title
        this.description = description
        this.url = url
    }

    constructor(url: String) : this(null) {
        this.url = url
    }

    override fun toString(): String {
        return "Article: $title, $description, $url, $author"
    }

    companion object {
        val unknown = "Unknown"
    }
}