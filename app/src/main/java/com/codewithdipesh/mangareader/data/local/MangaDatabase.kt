package com.codewithdipesh.mangareader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity

@Database(entities = [MangaEntity::class, GenreEntity::class, ThemeEntity::class], version = 1, exportSchema = false)
abstract class MangaDatabase : RoomDatabase(){
    abstract val mangaDao : MangaDao
}