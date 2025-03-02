package com.codewithdipesh.mangareader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visited_chapters")
data class VisitedChapter(
    @PrimaryKey
    val chapterid :String,
    val mangaId : String,
    val coverImageurl : String
)