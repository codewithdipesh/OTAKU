package com.codewithdipesh.mangareader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_manga")
data class FavouriteManga(
    @PrimaryKey
    val id :String,
    val title : String,
    val coverImage : String,
    val authorId : String
)
