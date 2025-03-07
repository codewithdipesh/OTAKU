package com.codewithdipesh.mangareader.data.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil3.ImageLoader
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.colorspace.connect
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import com.codewithdipesh.mangareader.data.local.MangaDatabase
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadChapterWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context,workerParams) {
    override suspend fun doWork(): Result {
        val chapterId = inputData.getString("chapterId") ?: return Result.failure()
        val chapterTitle = inputData.getString("chapterTitle") ?: return Result.failure()
        val pageNames = inputData.getStringArray("pageNames") ?: return Result.failure()
        val mangaId = inputData.getString("mangaId") ?: return Result.failure()
        val mangaName = inputData.getString("mangaName") ?: "unknown"
        val coverImage = inputData.getString("coverImage") ?: ""
        val chapterNumber = inputData.getDouble("chapterNumber",0.0)
        val pages = inputData.getInt("pages",0)
        val hash = inputData.getString("hash") ?: return Result.failure()
        val BASEURL = inputData.getString("BASEURL") ?: return Result.failure()
        Log.d("download","lol trigered work manager")
        try {
            val savedPages = mutableListOf<String>()
            //saving pages
            for((index,imageName) in pageNames.withIndex()){
                val fileName ="${chapterId}_page_${index + 1}" //ex:- 287482972753257hh3_page_1 ,287482972753257hh3_page_2
                val imageUrl = "$BASEURL/$hash/${imageName}" //url
                val savedPath = saveImage(imageUrl,fileName)
                savedPath?.let { savedPages.add(it) }
            }
            //saving coverImage
            val coverImageFile = "${mangaId}_coverImage"
            val coverImagePath = saveImage(coverImage,coverImageFile)

            //saving in room
            val dao = MangaDatabase.getInstance(applicationContext).mangaDao()

            val savedChapter = DownloadedChapterEntity(
                id = chapterId,
                title = chapterTitle ?: "",
                chapterNumber = chapterNumber,
                pages = pages,
                mangaId = mangaId,
                mangaName = mangaName,
                coverImage = coverImagePath ?: "",
                content = savedPages
            )
            dao.addDownloadedChapter(savedChapter)

            Log.d("download", savedChapter.toString())
            val chapterCount = dao.getDownloadedChaptersCount()
            Log.d("download", chapterCount.toString())

            return Result.success()
        }catch (e:Exception){
            return Result.failure()
        }
    }

    private suspend fun saveImage(imageUrl: String, fileName: String): String? {
        return withContext(Dispatchers.IO){
            try {
                val file = File(applicationContext.filesDir, "$fileName.jpg")
                Log.d("download", "Saving image: $imageUrl -> $file")

                val bitmap = getBitmapFromUrl(imageUrl)

                if (bitmap != null) {
                    FileOutputStream(file).use { fos ->
                        bitmap.compress(CompressFormat.JPEG, 85, fos)
                    }
                    Log.d("download", "Image saved successfully: ${file.absolutePath}")
                    return@withContext file.absolutePath  // Image saved successfully
                }else{
                    Log.d("download", "bitmap is null for URL $imageUrl")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }

    suspend fun getBitmapFromUrl(imageUrl: String): Bitmap?{
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(imageUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

}