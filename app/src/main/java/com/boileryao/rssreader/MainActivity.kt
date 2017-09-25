package com.boileryao.rssreader

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.boileryao.rssreader.bean.Article
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.modules.articles.ArticlesFragment
import com.boileryao.rssreader.common.widgets.SourceInfoDialog
import com.boileryao.rssreader.modules.sources.SubscribedFragment
import com.boileryao.rssreader.common.handleMenuItemClick
import com.boileryao.rssreader.common.replaceMainFragmentTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
        , SubscribedFragment.OnWebsiteListInteraction {
    val TAG = "MainActivity"

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
        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        supportActionBar!!.setTitle(R.string.app_name)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { SourceInfoDialog.show(this) }

        // prepare Subscribed Websites Fragment
        val subscribedFragment = SubscribedFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, subscribedFragment).commit()
    }

    var lastBackPress = 0L
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)  // dismiss draw
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()  // pop up stack
        } else {  // SubscribeFragment(MAIN), double click to exit
            val curr = System.currentTimeMillis()
            if (curr - lastBackPress > 2000) {  // 2 seconds
                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
            }
            lastBackPress = curr
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val startSettingIntent = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingIntent)
                true
            }
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
