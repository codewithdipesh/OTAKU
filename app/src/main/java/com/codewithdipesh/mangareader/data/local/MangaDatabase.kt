package com.codewithdipesh.mangareader.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codewithdipesh.mangareader.data.converter.Converters
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.data.local.entity.VisitedChapter

@Database(entities = [
    MangaEntity::class,
    GenreEntity::class,
    ThemeEntity::class ,
    VisitedChapter::class,
    DownloadedChapterEntity::class],
    version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MangaDatabase : RoomDatabase(){
    abstract fun mangaDao() : MangaDao

    companion object{
        @Volatile
        private var INSTANCE : MangaDatabase? = null

        fun getInstance(context:Context) : MangaDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MangaDatabase::class.java,
                    "manga_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}