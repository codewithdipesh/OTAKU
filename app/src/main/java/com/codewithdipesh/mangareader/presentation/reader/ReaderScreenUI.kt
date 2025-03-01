package com.codewithdipesh.mangareader.presentation.reader

import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.ReadMode

data class ReaderScreenUI(
    val highQualityImageList : List<String> = emptyList(),
    val lowQualityImageList : List<String> = emptyList(),
    val hash : String = "",
    val chapter : Chapter? = null,
    val currentPage : Int = 1,
    val pageSize : Int = 1,
    val isLoading : Boolean = false,
    val isHighQuality : Boolean = false,
    val isPreloadComplete : Boolean = false,
    val readMode :ReadMode = ReadMode.Vertical,
    val manualTrigger : Boolean = false
)