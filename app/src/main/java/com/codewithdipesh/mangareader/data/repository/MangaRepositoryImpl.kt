package com.codewithdipesh.mangareader.data.repository

import android.util.Log
import com.codewithdipesh.mangareader.data.Preferences.DefaultPrefrences
import com.codewithdipesh.mangareader.data.cache.MangaCache
import com.codewithdipesh.mangareader.data.local.MangaDatabase
import com.codewithdipesh.mangareader.domain.constants.MangaQuotes
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.data.local.entity.VisitedChapter
import com.codewithdipesh.mangareader.data.mappers.toChapter
import com.codewithdipesh.mangareader.data.mappers.toDownloadedChapter
import com.codewithdipesh.mangareader.data.mappers.toEntity
import com.codewithdipesh.mangareader.data.mappers.toManga
import com.codewithdipesh.mangareader.data.remote.MangaApi
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.ChapterDetails
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter
import com.codewithdipesh.mangareader.domain.model.Downloads
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.MangaDownloadedDetails
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.AppError
import com.codewithdipesh.mangareader.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import java.net.UnknownHostException
import kotlin.random.Random

class MangaRepositoryImpl(
   private val api : MangaApi,
    private val dao : MangaDao,
    private val pref: DefaultPrefrences
):MangaRepository{

    override suspend fun getTopMangas(): Result<List<Manga>> {

        //got in local db
        val cachedMangas = dao.getCachedTopMangas()
        val isCacheValid = cachedMangas.isNotEmpty() &&
                (System.currentTimeMillis() - cachedMangas.first().lastUpdated < 12 * 60 * 60 * 1000)
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
                Log.d("repository", "response - $response")
                if (response.isSuccessful) {
                    val mangaList = response.body()?.data ?: emptyList()

                    val resultMangaList = mangaList.map { mangaData ->
                        //cover Image
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
                        Log.d("repository", "response - $coverImage")
                        mangaData.toManga(coverImage)
                    }
                    //add manga theme and genre to locally then return

                    Log.d("repository", "Saving manga: lastUpdated = ${System.currentTimeMillis()}")
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

    override suspend fun getAllMangas(): Result<List<Manga>> {
        val cachedMangas = dao.getCachedAllMangas()
        val isCacheValid = cachedMangas.isNotEmpty() &&
                (System.currentTimeMillis() - cachedMangas.first().lastUpdated < 24 * 60 * 60 * 1000)
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
                        //coverImage
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

    override suspend fun searchManga(title: String): Result<List<Manga>> {
        return try {

            val response = api.searchManga(title = title)

            if (response.isSuccessful) {
                val mangaList = response.body()?.data ?: emptyList()
                Log.d("repository","search ${mangaList}")
                if(mangaList.isEmpty()){
                    return Result.Error(AppError.ServerError("No Manga Found"))
                }
                val resultMangaList = mangaList.map { mangaData ->
                    val coverImage = try {
                        val coverResponse = api.getCoverImage(mangaData.id)
                        val coverData = coverResponse.body()?.data
                        if (coverResponse.isSuccessful) {
                            coverData?.firstOrNull()?.attributes?.fileName ?: ""
                        } else {
                            ""
                        }
                    } catch (e: Exception) {
                        Log.e("MangaRepository", "searchManga: Error fetching cover", e)
                        ""
                    }
                    mangaData.toManga(coverImage)
                }
                Log.d("repository","${resultMangaList.first().chapters}")

                return Result.Success(resultMangaList)
            } else {
                Result.Error(AppError.ServerError("Empty Response from server"))
            }
        } catch (e: IOException) {
            Result.Error(AppError.NetworkError())
        } catch (e: UnknownHostException) {
            Result.Error(AppError.NetworkError())
        } catch (e: Exception) {
            Result.Error(AppError.UnknownError(e.message ?: "Unknown error occured"))
        }
    }



    override fun getSearchHistory(): List<String> {
        return pref.loadHistory()
    }

    override fun saveSearchHistory(searchTerm: String) {
        Log.e("history"," repository : $searchTerm")
        pref.saveHistory(searchTerm)
    }

    override suspend fun getAuthor(authorId: String): Result<String> {
        val response = api.getAuthor(authorId)
        if (response.isSuccessful){
            val author = response.body()?.data?.attributes?.name ?: ""
            Log.d("repo","getAuthor: $author")
            return Result.Success(author)
        }
        else{
            Log.d("repo","getAuthor: error $response")
            return  Result.Error(AppError.UnknownError("No author found $response"))
        }
    }

    override suspend fun getMangaById(mangaId: String): Result<Manga> {

        //in memory cache(RAM) -> disk cache -> api
        val memoryCachedManga = MangaCache.get(mangaId)
        Log.d("cache", "RAM ${memoryCachedManga} ->")
        if(memoryCachedManga != null){
            Log.d("cache", "got RAM ->")
            return Result.Success(memoryCachedManga)
        }
        //no cache available in RAM
          //search in disk Room db
        val diskCachedManga = dao.getMangaById(mangaId)
        if (diskCachedManga.isNotEmpty()) {
            Log.d("cache", "got in disk  ->")
            Log.e("Chapter Size", "repo (cached) -> ${diskCachedManga.first().chapters}")
            val genres = dao.getGenresForManga(mangaId)
            val themes = dao.getThemesForManga(mangaId)
            val result = diskCachedManga.first().toManga(genres, themes)
            //save in in memory cache
            MangaCache.put(result.id,result)
            return Result.Success(result)
        } else {
            try {
                val response = api.getMangaById(mangaId)
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()?.data != null) {
                        Log.d("MangaRepository", "manga japanese title:  ${response.body()!!.data.attributes.altTitles[0].ja}")
                        val updatedManga = response.body()!!.data.toManga("")
                        Log.e("Chapter Size", "repo(api)-> toManga() -> in repo after mapping ${updatedManga.chapters}")
                        //put in RAM first
                        MangaCache.put(updatedManga.id,updatedManga)
                        return Result.Success(updatedManga)
                    } else {
                        return Result.Error(AppError.ServerError("Unknown Server Error"))
                    }
                } else {
                    return Result.Error(AppError.UnknownError("No Manga Found"))
                }
            } catch (e: IOException) {
                return Result.Error(AppError.NetworkError())
            } catch (e: Exception) {
                return Result.Error(AppError.UnknownError(e.message ?: "Something went wrong"))
            }
        }
    }

    override suspend fun getChapters(mangaId: String,limit:Int,offset:Int,order:String): Result<List<Chapter>> {
        return  try {
            val visitedChapters = dao.getVisitedChapterForManga(mangaId).map { it.chapterid }

            val response = api.getChapters(mangaId = mangaId, limit = limit, offset = offset, chapterOrder = order, volumeOrder = order)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body.data.map { it.toChapter(body.data.size.toDouble(),visitedChapters) }) //bcz many time manga has 1 chapter but it has null from Api response
                } else {
                    Result.Error(AppError.ServerError("Unknown Server Error"))
                }
            } else {
               Result.Error(AppError.NetworkError())
            }
        } catch (e: IOException) {
            Result.Error(AppError.NetworkError())
        } catch (e: Exception) {
            Result.Error(AppError.UnknownError(e.localizedMessage ?: "Something went wrong"))
        }
    }

    override suspend fun getChapterPages(chapterId: String): Result<ChapterDetails> {
        return try {
            val response = api.getChapterPages(chapterId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(ChapterDetails(
                        hash = body.chapter.hash,
                        data = body.chapter.data,
                        dataSaver = body.chapter.dataSaver
                    ))
                } else {
                    Result.Error(AppError.ServerError("Unknown Server Error"))
                }
            } else {
                Result.Error(AppError.NetworkError())
            }
        }catch (e:IOException){
            Result.Error(AppError.NetworkError())
        }catch (e:Exception){
            Result.Error(AppError.UnknownError(e.localizedMessage ?: "Something went wrong"))
        }
    }

    override suspend fun getChapterById(chapterId: String): Result<Chapter> {
        return try {
            val isVisited = dao.getVisitedOrNot(chapterId)

            val response = api.getChapterById(chapterId)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Result.Success(response.body()!!.data.toChapter(isVisited = isVisited))
                } else {
                    Result.Error(AppError.ServerError())
                }
            }else{
                Result.Error(AppError.UnknownError("No Chapter Found"))
            }
        }catch (e:IOException){
            Result.Error(AppError.NetworkError())
        }catch (e:Exception){
            Result.Error(AppError.UnknownError())
        }
    }

    override suspend fun getAllVisitedChapters(): Result<List<VisitedChapter>> {
        TODO("Not yet implemented")
    }

    override suspend fun addVisitedChapter(chapter: Chapter,coverImage :String) {
        val visitedChapter = VisitedChapter(
            chapterid = chapter.id,
            mangaId = chapter.mangaId,
            coverImageurl = coverImage
        )
        dao.addVisitedChapter(visitedChapter)
    }

    override suspend fun getAllDownloads(): Result<Downloads> {
        return try {
            // First, check the count of downloaded chapters
            val chapterCount = dao.getDownloadedChaptersCount()
            Log.d("DownloadRepository", "Total downloaded chapters count: $chapterCount")

            val allChapters = dao.getAllDownloadedChapters()
            Log.d("DownloadRepository", "Total downloaded chapters: ${allChapters.size}")

            if (allChapters.isNotEmpty()) {
                val specificMangaChapters = allChapters.groupBy { it.mangaId }
                Log.d("DownloadRepository", "Unique manga count: ${specificMangaChapters.size}")

                val ans = mutableMapOf<MangaDownloadedDetails, List<DownloadedChapter>>()

                specificMangaChapters.forEach { (mangaId, mangaChapters) ->
                    // Log details for each manga group
                    Log.d("DownloadRepository", "Manga ID: $mangaId")
                    Log.d("DownloadRepository", "Manga Name: ${mangaChapters.first().mangaName}")
                    Log.d("DownloadRepository", "Chapters in this manga: ${mangaChapters.size}")

                    val mangaDetail = MangaDownloadedDetails(
                        id = mangaId,
                        title = mangaChapters.first().mangaName,
                        totalChaptersDownloaded = mangaChapters.size,
                        coverImage = mangaChapters.first().coverImage
                    )

                    val chapterList = mangaChapters.map { it.toDownloadedChapter() }
                    ans[mangaDetail] = chapterList
                }

                Log.d("DownloadRepository", "Final downloads map size: ${ans.size}")
                return Result.Success(Downloads(downloads = ans))
            } else {
                Log.d("DownloadRepository", "No downloaded chapters found")
                return Result.Success(Downloads())
            }
        } catch (e: Exception) {
            Log.e("DownloadRepository", "Error retrieving downloads", e)
            return Result.Error(AppError.UnknownError(e.message ?: "UnknownError Happened"))
        }
    }

    override suspend fun getDownloadedChapterForManga(mangaId: String): Flow<List<DownloadedChapter>> {
        return dao.getDownloadedChapterForManga(mangaId)
            .map { list -> list.map { it.toDownloadedChapter() } }
            .catch { emit(emptyList()) } // Handle errors within Flow
    }


}