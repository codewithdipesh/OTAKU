package com.codewithdipesh.mangareader.data.cache

import android.util.Log
import com.codewithdipesh.mangareader.domain.model.Manga
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MangaCache {
    private val cache = mutableMapOf<String, Manga>()
    private val cachedTimeStamps = mutableMapOf<String,Long>()
    private val expirationTime = 10 * 60 * 1000L //10 minutes

    init {
        startCleanUpJob()
    }

    fun put(mangaId : String,manga: Manga){
        cache[mangaId] = manga
        cachedTimeStamps[mangaId] = System.currentTimeMillis()
        Log.d("cache", "saving in RAM $cache + $cachedTimeStamps  ->")
    }

    fun get(mangaId: String):Manga?{
        Log.d("cache", "searching in RAM  ->")
        return cache[mangaId]
    }

    private fun startCleanUpJob(){
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(2* 60 * 1000) // Run cleanup every 2 minute
                val currentTime = System.currentTimeMillis()

                val expiredKeys = cachedTimeStamps.filter { (key, timestamp) ->
                    (currentTime - timestamp) > expirationTime
                }.keys

                expiredKeys.forEach { key ->
                    cache.remove(key)
                    cachedTimeStamps.remove(key)
                }
            }
        }
    }
}