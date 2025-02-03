package com.codewithdipesh.mangareader.data.repository.dto

import com.google.gson.annotations.SerializedName

data class Description(
    val en: String,
    @SerializedName("es-la")
    val es_la: String,
    val ja: String,
    @SerializedName("pt-br")
    val pt_br: String,
    val ru: String,
    val vi: String
)