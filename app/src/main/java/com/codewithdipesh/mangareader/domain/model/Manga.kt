package com.codewithdipesh.mangareader.domain.model

data class Manga(
    val id : String,
    val title : String?,
    val altTitle : String?,
    val description : String?,
    val status : Status,
    val year : Int,
    val contentRating : Rating,
    val genres : List<String>,
    val themes : List<String>,
    val createdAt : String,
    val isFavourite : Boolean,
    val coverImage : String?
)
