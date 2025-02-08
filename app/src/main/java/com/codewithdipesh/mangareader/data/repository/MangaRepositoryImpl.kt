package com.codewithdipesh.mangareader.data.repository

import android.util.Log
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.data.mappers.toEntity
import com.codewithdipesh.mangareader.data.mappers.toManga
import com.codewithdipesh.mangareader.data.remote.MangaApi
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.AppError
import com.codewithdipesh.mangareader.domain.util.Result
import okio.IOException

class MangaRepositoryImpl(
   private val api : MangaApi,
    private val dao : MangaDao
):MangaRepository{

    override suspend fun getTopMangas(): Result<List<Manga>> {

        //got in local db
        val cachedMangas = dao.getCachedTopMangas()
        val isCacheValid = cachedMangas.isNotEmpty() &&
                (System.currentTimeMillis() - cachedMangas.first().lastUpdated < 6 * 60 * 60 * 1000)
        Log.d("repository ","top: cache valid $isCacheValid")
        return if(isCacheValid){
            val mangaList = cachedMangas.map {
                val genres = dao.getGenresForManga(it.id)
                val themes = dao.getThemesForManga(it.id)
                it.toManga(genres,themes)
            }
            Result.Success(mangaList)
        }else{
            //fetch api call and update local db
            return try {
                val response = api.getTopManga()

                if (response.isSuccessful) {
                    val mangaList = response.body()?.data ?: emptyList()

                    val resultMangaList = mangaList.map { mangaData ->
                        val coverImage = try {
                            val coverResponse = api.getCoverImage(mangaData.id)
                            if (coverResponse.isSuccessful) {
                                coverResponse.body()?.data?.firstOrNull()?.attributes?.fileName ?: ""
                            } else {
                                ""
                            }
                        } catch (e: Exception) {
                            ""
                        }
                        mangaData.toManga(coverImage)
                    }
                    //add manga theme and genre to locally then return
                    dao.insertMangas(resultMangaList.map { it.toEntity(isTopManga = true) })
                    dao.insertGenre(resultMangaList.flatMap {
                        it.genres.map { genre ->
                            GenreEntity(mangaId = it.id, name = genre)
                        }
                    })
                    dao.insertTheme(resultMangaList.flatMap {
                        it.themes.map { theme ->
                            ThemeEntity(mangaId = it.id, name = theme)
                        }
                    })

                    return Result.Success(resultMangaList)
                } else {
                    Result.Error(AppError.ServerError("Empty Response from server"))
                }
            }
            catch (e: IOException) {
                Result.Error(AppError.NetworkError())
            } catch (e: Exception) {
                Result.Error(AppError.UnknownError(e.message ?: "Something went wrong"))
            }

        }
    }

    //TODO Same as above but with pagination
    override suspend fun getAllMangas(): Result<List<Manga>> {
        val cachedMangas = dao.getCachedAllMangas()
        val isCacheValid = cachedMangas.isNotEmpty() &&
                (System.currentTimeMillis() - cachedMangas.first().lastUpdated < 12 * 60 * 60 * 1000)
        Log.d("repository ","getallManga: cache valid $isCacheValid")
        return if(isCacheValid){
            val mangaList = cachedMangas.map {
                val genres = dao.getGenresForManga(it.id)
                val themes = dao.getThemesForManga(it.id)
                it.toManga(genres,themes)
            }
            Result.Success(mangaList)

        }else{
            //fetch api call and update local db
            return try {
                val response = api.getAllManga()

                if (response.isSuccessful) {
                    val mangaList = response.body()?.data ?: emptyList()

                    val resultMangaList = mangaList.map { mangaData ->
                        val coverImage = try {
                            val coverResponse = api.getCoverImage(mangaData.id)
                            if (coverResponse.isSuccessful) {
                                coverResponse.body()?.data?.firstOrNull()?.attributes?.fileName ?: ""
                            } else {
                                ""
                            }
                        } catch (e: Exception) {
                            ""
                        }
                        mangaData.toManga(coverImage)
                    }
                    //add manga theme and genre to locally then return
                    dao.insertMangas(resultMangaList.map { it.toEntity(isTopManga = false) })
                    dao.insertGenre(resultMangaList.flatMap {
                        it.genres.map { genre ->
                            GenreEntity(mangaId = it.id, name = genre)
                        }
                    })
                    dao.insertTheme(resultMangaList.flatMap {
                        it.themes.map { theme ->
                            ThemeEntity(mangaId = it.id, name = theme)
                        }
                    })

                    return Result.Success(resultMangaList)
                } else {
                    Result.Error(AppError.ServerError("Empty Response from server"))
                }
            }
            catch (e: IOException) {
                Result.Error(AppError.NetworkError())
            } catch (e: Exception) {
                Result.Error(AppError.UnknownError(e.message ?: "Something went wrong"))
            }

        }
    }
}