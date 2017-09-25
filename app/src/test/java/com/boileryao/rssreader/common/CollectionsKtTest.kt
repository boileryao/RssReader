package com.boileryao.rssreader.common

import org.junit.Assert
import org.junit.Test

/**
 * Created by boileryao on 9/5/2017.
 * Class: CollectionsKtTest
 */
class CollectionsKtTest {
    @Test
    fun putAll() {
        val map = mutableMapOf(1 to "one", 2 to "two")
        val sub = mutableMapOf(1 to "single")

        map.putAll(sub)
        Assert.assertEquals(map.size, 2)
        Assert.assertEquals(map[1], "single")
    }

    @Test
    fun mapTest() {
        val map = mutableMapOf<String, String>()
        map["one"] = "ppp"
        Assert.assertEquals(map.size, 1)
    }
}