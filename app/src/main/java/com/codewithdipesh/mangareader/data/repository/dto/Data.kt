package com.codewithdipesh.mangareader.data.repository.dto

data class Data(
    val attributes: Attributes,
    val id: String,
    val relationships: List<Relationship>,
    val type: String
)