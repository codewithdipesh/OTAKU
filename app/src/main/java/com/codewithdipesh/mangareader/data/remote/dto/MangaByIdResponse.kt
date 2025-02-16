package com.codewithdipesh.mangareader.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MangaByIdResponse(
    val data: Data,
    val response: String,
    val result: String,
)