package com.codewithdipesh.mangareader.presentation.downloads

import com.codewithdipesh.mangareader.domain.model.DownloadedChapter

data class DownloadMangaUIState(
    val title : String ="",
    val id : String = "",
    val chapters : List<DownloadedChapter> = emptyList()
)
