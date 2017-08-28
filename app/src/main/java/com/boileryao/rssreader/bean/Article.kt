package com.boileryao.rssreader.bean

import com.rometools.rome.feed.synd.SyndEntry
import java.io.Serializable
import java.util.*

/**
 * Created by boileryao on 8/27/2017.
 * Class: Article
 */

class Article(feed: SyndEntry?) : Serializable {
    var title = unknown
    var description = unknown
    var url = unknown
    var author = unknown
    var date = 0L

    init {
        if (feed != null) {
            title = feed.title
            description = feed.description.value.replace("<img.+?>".toRegex(), "")
            url = feed.link
            author =
                    if (feed.authors != null && feed.authors.size > 0) feed.authors[0].name
                    else feed.author ?: ""
            date = (feed.publishedDate ?: feed.updatedDate ?: Date()).time
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