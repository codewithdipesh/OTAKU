package com.codewithdipesh.mangareader.domain.Preferences

interface preference {
    fun saveHistory(string: String)

    fun loadHistory(): List<String>

    companion object{
        const val KEY_HISTORY ="history"
    }
}