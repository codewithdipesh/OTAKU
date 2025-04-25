package com.codewithdipesh.mangareader.domain.model

import kotlinx.serialization.Serializable

data class Manga(
    val id : String,
    val title : String?,
    val altTitle : String?,
    val description : String?,
    val status : Status,
    val year : Int,
    val contentRating : Rating,
    val genres : List<Map<String,String>>,//id,name
    val themes : List<Map<String,String>>,
    val createdAt : String,
    val isFavourite : Boolean,
    val coverImage : String?,
    val authorId : String?,
    val chapters :Int
)
