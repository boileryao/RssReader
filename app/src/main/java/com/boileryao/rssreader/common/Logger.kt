package com.boileryao.rssreader.common

import android.util.Log
import com.boileryao.rssreader.BuildConfig

/**
 * Created by boileryao on 10/16/2017.
 * Class: Logger
 */

class Logger {
    companion object {
        fun debug(tag: String = "%%RSS", msg: String) {
            if (!BuildConfig.DEBUG) {
                return
            }

            val distinguishableTag =
                    if (tag.startsWith("%%")) tag else tag.prependIndent("%%")
            Log.d(distinguishableTag, msg)
        }

        fun error(tag: String = "%%RSS", msg: String) {
            if (!BuildConfig.DEBUG) {
                return
            }

            val distinguishableTag =
                    if (tag.startsWith("%%")) tag else tag.prependIndent("%%")
            Log.e(distinguishableTag, msg)
        }
    }
}