package com.codewithdipesh.mangareader.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    @SerializedName("`data`")
    val data: List<Data>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)