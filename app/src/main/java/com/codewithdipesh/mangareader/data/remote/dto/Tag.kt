package com.codewithdipesh.mangareader.data.remote.dto

data class Tag(
    val attributes: AttributesX,
    val id: String,
    val relationships: List<Any?>,
    val type: String
)