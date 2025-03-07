package com.codewithdipesh.mangareader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codewithdipesh.mangareader.data.converter.Converters

@Entity(tableName = "downloaded_chapter")
data class DownloadedChapterEntity(
    @PrimaryKey
    val id : String,
    val chapterNumber: Double,
    val title : String?,
    val pages : Int,
    val mangaId : String,
    val mangaName : String,
    val coverImage : String,
    @TypeConverters(Converters::class) val content : List<String>
)