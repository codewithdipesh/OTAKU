package com.codewithdipesh.mangareader.data.mappers

import com.codewithdipesh.mangareader.data.remote.dto.ChapterData
import com.codewithdipesh.mangareader.data.remote.dto.chapterResult
import com.codewithdipesh.mangareader.domain.model.Chapter


fun ChapterData.toChapter(optionalSize : Double =  0.0): Chapter {
    return Chapter(
        id = id,
        chapterNumber = if(attributes.chapter != null) attributes.chapter.toDouble()
                        else optionalSize,
        title = attributes.title ?: "",
        pages = attributes.pages,
        mangaId = relationships.find { it.type == "manga" }?.id ?: "",
        createdAt = attributes.createdAt,
        version = attributes.version
    )
}