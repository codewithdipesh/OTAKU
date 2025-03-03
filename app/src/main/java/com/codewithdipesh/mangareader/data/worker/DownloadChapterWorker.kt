package com.codewithdipesh.mangareader.data.worker

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil3.Bitmap
import coil3.ImageLoader
import android.graphics.Bitmap.CompressFormat
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import com.codewithdipesh.mangareader.data.local.MangaDatabase
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DownloadChapterWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context,workerParams) {
    override suspend fun doWork(): Result {
        val chapterId = inputData.getString("chapterId") ?: return Result.failure()
        val chapterTitle = inputData.getString("chapterTitle") ?: return Result.failure()
        val pageUrls = inputData.getStringArray("pageUrls") ?: return Result.failure()
        val mangaId = inputData.getString("mangaId") ?: return Result.failure()
        val mangaName = inputData.getString("mangaName") ?: "unknown"
        val coverImage = inputData.getString("coverImage") ?: ""
        val chapterNumber = inputData.getDouble("chapterNumber",0.0)
        val pages = inputData.getInt("pages",0)

        try {
            val savedPages = mutableListOf<String>()
            //saving pages
            for((index,url) in pageUrls.withIndex()){
                val fileName ="${chapterId}_page_${index + 1}" //ex:- 287482972753257hh3_page_1 ,287482972753257hh3_page_2
                val savedPath = saveImage(url,fileName)
                savedPath?.let { savedPages.add(it) }
            }
            //saving coverImage
            val coverImageFile = "${mangaId}_coverImage"
            val coverImagePath = saveImage(coverImage,coverImageFile)

            //saving in room
            val dao = MangaDatabase.getInstance(applicationContext).mangaDao()
            dao.addDownloadedChapter(
                DownloadedChapterEntity(
                    id = chapterId,
                    title = chapterTitle ?: "",
                    chapterNumber = chapterNumber,
                    pages = pages,
                    mangaId = mangaId,
                    mangaName = mangaName,
                    coverImage = coverImagePath ?: "",
                    content = savedPages
                )
            )

            return Result.success()
        }catch (e:Exception){
            return Result.failure()
        }
    }

    private suspend fun saveImage(imageUrl: String, fileName: String): String? {
        return withContext(Dispatchers.IO){
            try {
                val file = File(applicationContext.filesDir, "$fileName.jpg")

                val loader = ImageLoader(applicationContext)
                val request = ImageRequest.Builder(applicationContext)
                    .data(imageUrl)
                    .memoryCacheKey(fileName)
                    .diskCacheKey(fileName)
                    .allowHardware(false)
                    .build()

                val result = loader.execute(request)
                if (result is SuccessResult) {
                    val bitmap = (result.image as? BitmapDrawable)?.bitmap
                    if(bitmap != null){
                        FileOutputStream(file).use { fos ->
                            bitmap.compress(CompressFormat.JPEG, 100, fos)
                        }
                        return@withContext file.absolutePath  // Image saved successfully
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }

}