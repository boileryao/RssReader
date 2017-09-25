package com.boileryao.rssreader.common.widgets

import android.app.AlertDialog
import android.content.Context

/**
 * Created by boileryao on 9/25/2017.
 * Class: PopupMenu
 */

class PopupMenu(private val context: Context) {
    private val mEntries = mutableMapOf<String, () -> Unit>()

    fun addEntries(vararg entries: Pair<String, () -> Unit>): PopupMenu {
        mEntries.putAll(entries)
        return this
    }

    fun show() {
        var items = arrayOf<String>()
        mEntries.forEach { items = items.plus(it.key) }

        val dialog = AlertDialog.Builder(context)
                .setItems(items, { _, pos ->
                    val key = items[pos]
                    mEntries[key]?.invoke()
                })
                .setCancelable(true).
                create()
        dialog.show()
    }
}