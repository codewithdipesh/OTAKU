package com.codewithdipesh.mangareader.data.remote.dto

data class ChapterDataAttribute(
    val chapter: String?,
    val createdAt: String,
    val externalUrl: String?,
    val pages: Int,
    val publishAt: String,
    val readableAt: String,
    val title: String?,
    val translatedLanguage: String,
    val updatedAt: String,
    val uploader: String,
    val version: Int,
    val volume: String?
)