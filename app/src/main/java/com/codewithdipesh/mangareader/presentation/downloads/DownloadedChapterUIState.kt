package com.codewithdipesh.mangareader.presentation.downloads

import com.codewithdipesh.mangareader.domain.model.ReadMode

//consumed in Reader Screen
data class DownloadedChapterUIState(
    val chapterId : String = "",
    val chapterName : String = "",
    val chapterNumber : Double =0.0,
    val pages : List<String> = emptyList(),
    val currentPage : Int = 1,
    val pageSize : Int = 1,
    val readMode : ReadMode = ReadMode.Vertical,
    val isAutoScroll : Boolean = false,
    val isLoading : Boolean = false
)
