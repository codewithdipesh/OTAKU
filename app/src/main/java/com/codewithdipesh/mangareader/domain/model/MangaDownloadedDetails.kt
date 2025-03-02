package com.codewithdipesh.mangareader.domain.model

data class MangaDownloadedDetails(
    val id : String,
    val title:String?,
    val totalChaptersDownloaded : Int = 0,
    val coverImage :String = ""
)
