package com.codewithdipesh.mangareader.domain.model

data class DownloadedChapter(
    val id : String,
    val chapterNumber: Double,
    val title : String?,
    val pages : Int,
    val mangaId : String,
    val coverImage : String,
    val content : List<String>
)
