package com.codewithdipesh.mangareader.domain.Preferences

interface preference {
    fun saveHistory(searchTerm: String)

    fun loadHistory(): List<String>

    companion object{
        const val KEY_HISTORY ="history"
    }
}