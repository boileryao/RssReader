package com.boileryao.rssreader.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by boileryao on 8/29/2017.
 * Class: databases
 */

class SQLiteHelper
internal constructor(context: Context, db: String, private val cmd: String, version: Int = 1) :
        SQLiteOpenHelper(context, db, null, version) {
    override fun onCreate(sqlite: SQLiteDatabase?) {
        sqlite?.execSQL(cmd)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // nothing
    }

    companion object {
        val DB_WEBSITES = "websites.db"
        val DB_ARTICLES = "articles.db"
    }
}

