package com.codewithdipesh.mangareader.data.mappers

import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status

fun MangaEntity.toManga(
    genres : List<Map<String,String>>,
    themes : List<Map<String,String>>
) : Manga{

    return Manga(
        id = id,
        title = title,
        altTitle = altTitle,
        description = description,
        status = Status.fromString(status),
        year = year,
        contentRating = Rating.fromString(contentRating),
        createdAt = createdAt,
        isFavourite = isFavourite,
        coverImage = coverImage,
        genres = genres,
        themes = themes,
        authorId = authorId,
        chapters = chapters
    )
}

fun Manga.toEntity(isTopManga : Boolean = false):MangaEntity {
    return MangaEntity(
       id = id,
        title = title,
        altTitle = altTitle,
        description = description,
        status = status.name,
        year = year,
        contentRating = contentRating.name,
        createdAt = createdAt,
        isFavourite = isFavourite,
        coverImage = coverImage,
        lastUpdated = System.currentTimeMillis(),
        topManga = isTopManga,
        authorId = authorId,
        chapters = chapters
    )
}