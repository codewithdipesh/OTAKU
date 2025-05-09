package com.codewithdipesh.mangareader.domain.model

data class Chapter(
    val id : String,
    val chapterNumber: Double,
    val title : String?,
    val pages : Int,
    val mangaId : String,
    val createdAt : String,
    val version : Int,
    val isVisited :Boolean
)
