package com.codewithdipesh.mangareader.presentation.homescreen

import com.codewithdipesh.mangareader.domain.model.Manga

data class HomeUiState(
    val topMangaList: List<Manga> = emptyList(),
    val allMangas : List<Manga> = emptyList(),
)
