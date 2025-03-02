package com.codewithdipesh.mangareader.data.mappers

import com.codewithdipesh.mangareader.data.remote.dto.ChapterData
import com.codewithdipesh.mangareader.data.remote.dto.ChapterDataAttribute
import com.codewithdipesh.mangareader.data.remote.dto.DataXXX
import com.codewithdipesh.mangareader.domain.model.Chapter


fun ChapterData.toChapter(optionalSize : Double =  0.0,visitedChapterList:List<String> = emptyList()): Chapter {
    return Chapter(
        id = id,
        chapterNumber = if(attributes.chapter != null) attributes.chapter.toDouble()
                        else optionalSize,
        title = attributes.title ?: "Chapter ${attributes.chapter?.toDouble() ?: optionalSize}",
        pages = attributes.pages,
        mangaId = relationships.find { it.type == "manga" }?.id ?: "",
        createdAt = attributes.createdAt,
        version = attributes.version,
        isVisited = visitedChapterList.contains(id)
    )
}
//chapter by Id -> response data
fun DataXXX.toChapter(optionalSize : Double =  0.0,isVisited:Boolean = false) : Chapter{
    return Chapter(
        id = id,
        chapterNumber = if(attributes.chapter != null) attributes.chapter.toDouble()
        else optionalSize,
        title = attributes.title ?: "Chapter ${attributes.chapter?.toDouble() ?: optionalSize}",
        pages = attributes.pages,
        mangaId = relationships.find { it.type == "manga" }?.id ?: "",
        createdAt = attributes.createdAt,
        version = attributes.version,
        isVisited = isVisited
    )
}