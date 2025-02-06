package com.codewithdipesh.mangareader.data.remote.dto

import com.google.gson.annotations.SerializedName

data class coverImageResponse(
    val data: List<DataX>,
    val limit: Int,
    val offset: Int,
    val response: String,
    val result: String,
    val total: Int
)