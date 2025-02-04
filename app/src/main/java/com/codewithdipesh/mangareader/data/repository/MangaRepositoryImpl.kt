package com.codewithdipesh.mangareader.data.repository

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
      return try{
          val response = api.getTopManga()
          if(response.isSuccessful){
              val mangaList = response.body()?.data
              if(mangaList != null){
                  Result.Success(mangaList.map { it.toManga() })
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

    override suspend fun getAllMangas(): Result<List<Manga>> {
        return try{
            val response = api.getAllManga()
            if(response.isSuccessful){
                val mangaList = response.body()?.data
                if(mangaList != null){
                    Result.Success(mangaList.map { it.toManga() })
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