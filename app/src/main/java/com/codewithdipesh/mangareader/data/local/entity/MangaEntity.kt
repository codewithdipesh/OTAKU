package com.codewithdipesh.mangareader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey
    val id : String,
    val title : String?,
    val altTitle : String?,
    val description : String?,
    val status : String,
    val year : Int,
    val contentRating : String,
    val createdAt : String,
    val isFavourite : Boolean,
    val coverImage : String?,
    val lastUpdated : Long,
    val topManga : Boolean,
)
