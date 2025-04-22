package com.codewithdipesh.mangareader.data.mappers

import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import com.codewithdipesh.mangareader.domain.model.DownloadStatus
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter

fun DownloadedChapterEntity.toDownloadedChapter(): DownloadedChapter{
    return DownloadedChapter(
        id = id,
        chapterNumber = chapterNumber,
        title = title,
        pages = pages,
        mangaId = mangaId,
        coverImage = coverImage,
        content = content,
        status = DownloadStatus.fromString(downloadStatus)
    )
}

