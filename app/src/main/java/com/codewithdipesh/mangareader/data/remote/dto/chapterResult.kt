package com.codewithdipesh.mangareader.data.remote.dto

data class chapterResult(
    val data: List<ChapterData>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)