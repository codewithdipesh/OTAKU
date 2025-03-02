package com.codewithdipesh.mangareader.domain.model

data class Downloads(
    val downloads : Map<MangaDownloadedDetails,List<DownloadedChapter>> = emptyMap()  //mangatoplevel -> chapterlist
)
