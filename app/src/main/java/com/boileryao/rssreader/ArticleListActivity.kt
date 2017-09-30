package com.boileryao.rssreader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.boileryao.rssreader.common.replaceMainFragmentTo
import com.boileryao.rssreader.modules.articles.ArticlesFragment
import kotlinx.android.synthetic.main.activity_article_list.*

class ArticleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        // hide fab and setup title
        val bar = supportActionBar
        if (bar != null) {
            bar.title = intent.getStringExtra(ARG_TITLE)
            bar.setDisplayShowHomeEnabled(true)
            bar.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        } else {
            Log.w("ArticleListActivity", "No Support ActionBar")
        }

        // prepare Website Articles Fragment
        val articlesFragment = ArticlesFragment.newInstance()
        val articleList = intent.getSerializableExtra(ArticlesFragment.ARG_ARTICLE_LIST)
        articlesFragment.arguments.putSerializable(ArticlesFragment.ARG_ARTICLE_LIST, articleList)
        supportFragmentManager replaceMainFragmentTo articlesFragment

        val foo = findViewById<TextView>(R.id.detail_text)
        val text = StringBuilder()
        for (i in 1..1000) text.append("Boom voyage #$i\n")
        foo.text = text.toString()
    }


    companion object {
        val ARG_TITLE = "title"
    }
}
