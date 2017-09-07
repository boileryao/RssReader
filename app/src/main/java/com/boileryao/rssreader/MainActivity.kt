package com.boileryao.rssreader

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.subscribed.articles.ArticlesFragment
import com.boileryao.rssreader.subscribed.websites.SubscribedFragment
import com.boileryao.rssreader.util.database.WebsitesDbHelper
import com.boileryao.rssreader.util.handleMenuItemClick
import com.boileryao.rssreader.util.replaceMainFragmentTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
        , SubscribedFragment.OnWebsiteListInteraction {
    val TAG = "MainActivity"

    private lateinit var websiteList: List<Website>

    override fun onWebsiteListFragmentInteraction(item: Pair<Website, List<Article>?>) {
        if (item.second == null) {
            return
        }
        // prepare Website Articles Fragment
        val articlesFragment = ArticlesFragment.newInstance()
        articlesFragment.arguments
                .putSerializable(ArticlesFragment.ARG_ARTICLE_LIST, item.second as Serializable)
        supportFragmentManager replaceMainFragmentTo articlesFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        websiteList = WebsitesDbHelper.getInstance(this).all()

        // prepare Subscribed Websites Fragment
        val subscribedFragment = SubscribedFragment.newInstance()
        subscribedFragment.arguments
                .putSerializable(SubscribedFragment.ARG_WEBSITE_LIST, websiteList as Serializable)

        supportFragmentManager replaceMainFragmentTo subscribedFragment
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (handleMenuItemClick(item)) {  //the boolean is just a flag for closing drawer
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        return true
    }
}
