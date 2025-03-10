package com.codewithdipesh.mangareader.presentation.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter

data class DownloadMangaUIState(
    val title : String ="",
    val id : String = "",
    val chapters : List<DownloadedChapter> = emptyList()
)
