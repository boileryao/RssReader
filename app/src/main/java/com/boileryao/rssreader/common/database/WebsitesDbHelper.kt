package com.boileryao.rssreader.common.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.boileryao.rssreader.bean.Website
import com.boileryao.rssreader.common.Logger

class WebsitesDbHelper(val context: Context) {
    private lateinit var sqliteHelper: SQLiteHelper

    fun all(): List<Website> {
        val list = mutableListOf<Website>()

        val cursor = sqliteHelper.readableDatabase
                .query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val indexUrl = cursor.getColumnIndex(COL_URL)
            val indexLink = cursor.getColumnIndex(COL_LINK)
            val indexTitle = cursor.getColumnIndex(COL_TITLE)
            val indexInsertTime = cursor.getColumnIndex(COL_INSERT_TIME)
            val indexDescription = cursor.getColumnIndex(COL_DESCRIPTION)

            do {
                list.add(Website(
                        cursor.getString(indexTitle),
                        cursor.getString(indexDescription),
                        cursor.getString(indexUrl),
                        cursor.getString(indexLink)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return list
    }

    fun insert(website: Website): Boolean {
        val value = website.toContentValue()
        return try {
            val db = sqliteHelper.writableDatabase
            db.insert(TABLE_NAME, "", value)
            Logger.debug("$TABLE_NAME#I", website.toString())
            true
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            false
        }
    }

    fun update(website: Website): Boolean {
        val db = sqliteHelper.writableDatabase
        val cmd = "UPDATE $TABLE_NAME SET " +
                "$COL_TITLE = '${website.title}', " +
                "$COL_DESCRIPTION = '${website.description}' " +
                "WHERE $COL_URL = '${website.url}'"
        db.execSQL(cmd)
        Logger.debug("$TABLE_NAME#U", cmd)
        return true
    }

    fun delete(website: Website) {
        val db = sqliteHelper.writableDatabase
        val cmd = "delete from $TABLE_NAME where $COL_URL = '${website.url}'"
        db.execSQL(cmd)
        Logger.debug("$TABLE_NAME#D", cmd)
    }

    private fun isEmpty(): Boolean {
        val cursor = sqliteHelper.readableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        val isEmpty = cursor.moveToFirst()
        Log.e("TAG", "Table Website is empty: $isEmpty")
        cursor.close()
        return isEmpty
    }

    private fun Website.toContentValue(): ContentValues {
        val value = ContentValues()
        value.put(COL_TITLE, title)
        value.put(COL_URL, url)
        value.put(COL_LINK, link)
        value.put(COL_DESCRIPTION, description)
        value.put(COL_INSERT_TIME, System.currentTimeMillis())
        return value
    }


    companion object {
        val TABLE_NAME = "websites"
        val COL_URL = "url"  // primary key
        val COL_LINK = "link"
        val COL_TITLE = "title"
        val COL_INSERT_TIME = "insert_time"

        val COL_DESCRIPTION = "description"

        val CREATION =
                "CREATE TABLE $TABLE_NAME($COL_URL text primary key, " +
                        "$COL_INSERT_TIME long, " + "$COL_LINK text, " +
                        "$COL_TITLE text, $COL_DESCRIPTION text);"

        @SuppressLint("StaticFieldLeak")
        private var instance: WebsitesDbHelper? = null

        fun getInstance(context: Context): WebsitesDbHelper {
            if (instance == null || context != instance?.context) {
                instance = WebsitesDbHelper(context)
                instance?.sqliteHelper = SQLiteHelper(context, "Websites.db", CREATION)
            }
            return instance as WebsitesDbHelper
        }

    }
}