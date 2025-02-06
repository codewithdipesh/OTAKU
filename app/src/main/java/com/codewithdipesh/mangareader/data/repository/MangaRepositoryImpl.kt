package com.codewithdipesh.mangareader.data.repository

import android.util.Log
import com.codewithdipesh.mangareader.data.mappers.toManga
import com.codewithdipesh.mangareader.data.remote.MangaApi
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.AppError
import com.codewithdipesh.mangareader.domain.util.Result
import okio.IOException

class MangaRepositoryImpl(
   private val api : MangaApi
):MangaRepository{

    override suspend fun getTopMangas(): Result<List<Manga>> {
        return try {
            val response = api.getTopManga()
            Log.d("MangaRepositoryImpl", "response: ${response.body()}")

            if (response.isSuccessful) {
                val mangaList = response.body()?.data ?: emptyList()
                Log.d("MangaRepositoryImpl", "getTopMangas: ${mangaList.size}")

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
                Log.d("MangaRepositoryImpl", "resultMangaList: ${resultMangaList}")
                return Result.Success(resultMangaList)
            } else {
                Result.Error(AppError.ServerError("Empty Response from server"))
            }
        } catch (e: IOException) {
            Result.Error(AppError.NetworkError())
        } catch (e: Exception) {
            Result.Error(AppError.UnknownError(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun getAllMangas(): Result<List<Manga>> {
        return try{
            val response = api.getAllManga()
            var resultMangaList : List<Manga> = emptyList()

            if(response.isSuccessful){
                val mangaList = response.body()?.data
                if(mangaList != null){
                    mangaList.forEach {
                        var coverImage :String
                        val coverResponse = api.getCoverImage(it.id)
                        if(coverResponse.isSuccessful){
                            coverImage = coverResponse.body()!!.data.first().attributes.fileName
                        }else{
                            coverImage = ""
                        }
                        resultMangaList += it.toManga(coverImage)
                    }
                    Result.Success(resultMangaList)
                }
                else{
                    Result.Error(AppError.ServerError("Empty Response from server"))
                }
            }else{
                Result.Error(AppError.ServerError())
            }
        }catch (e:IOException){
            Result.Error(AppError.NetworkError())
        }catch (e:Exception){
            Result.Error(AppError.UnknownError())
        }
    }
}