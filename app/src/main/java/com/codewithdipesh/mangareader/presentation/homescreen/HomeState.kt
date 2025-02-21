package com.codewithdipesh.mangareader.presentation.homescreen

import com.codewithdipesh.mangareader.domain.model.Manga

data class HomeUiState(
    val topMangaList: List<Manga> = emptyList(),
    val allMangas : List<Manga> = emptyList(),
    val searchValue : String = "",
    val isloading : Boolean = false,//it is only for search screen
    val hasErrorOccured : Boolean = false,
    val searchResult : List<Manga> = emptyList(),
    val history : List<String> = emptyList()
)
