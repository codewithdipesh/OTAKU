package com.codewithdipesh.mangareader.data.repository.dto

import com.google.gson.annotations.SerializedName

data class AltTitle(
    val en: String,
    @SerializedName("es-la")
    val es_la: String,
    val id: String,
    val ja: String,
    @SerializedName("ja-ro")
    val ja_ro: String,
    val ko: String,
    @SerializedName("ko-ro")
    val ko_ro: String,
    val ru: String,
    val tl: String,
    val vi: String,
    val zh: String
)