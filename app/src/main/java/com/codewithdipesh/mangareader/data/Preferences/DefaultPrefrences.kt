package com.codewithdipesh.mangareader.data.Preferences

import android.content.SharedPreferences
import android.util.Log
import com.codewithdipesh.mangareader.domain.Preferences.preference

class DefaultPrefrences(
      private val prefs : SharedPreferences
) : preference {

    override fun saveHistory(searchTerm: String) {
        Log.e("history"," prefernces : $searchTerm")
        val currentHistory = loadHistory().toMutableList()
        currentHistory.remove(searchTerm)//remove to avoid duplicates
        currentHistory.add(0,searchTerm)

        //check 10 history limits
        if(currentHistory.size > 5){
            currentHistory.removeAt(currentHistory.size - 1)
        }

        prefs.edit().putString(preference.KEY_HISTORY, currentHistory.joinToString(",")).apply()

    }

    override fun loadHistory(): List<String> {
        val history = prefs.getString(preference.KEY_HISTORY,"")?.split(",")?.filter { it.isNotEmpty() }?: emptyList()
        Log.e("history"," repository : $history")
        return history
    }
}