package com.codewithdipesh.mangareader.data.mappers

import com.codewithdipesh.mangareader.data.remote.dto.Data
import com.codewithdipesh.mangareader.data.remote.dto.MangaResponse
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status

fun Data.toManga(coverImage : String):Manga{
    val genres : List<String> = attributes.tags
        .filter{ it.attributes.group == "genre"}
        .map { it.attributes.name.en ?: "Unknown"}

    val themes : List<String> = attributes.tags
        .filter{ it.attributes.group == "theme"}
        .map { it.attributes.name.en ?: "Unknown"}

    val japaneseTitle : String? = attributes.altTitles.firstNotNullOfOrNull { it.ja ?: it.ja_ro ?: it.ko ?: it.zh }

    return Manga(
       id = id,
       title = attributes.title.en,
       description = attributes.description.en,
       year = attributes.year,
       createdAt = attributes.createdAt,
       genres = genres,
       themes = themes,
       contentRating = Rating.fromString(attributes.contentRating),
       status = Status.fromString(attributes.status),
       isFavourite = false,
       altTitle = japaneseTitle,
       coverImage = if(coverImage != "") "https://uploads.mangadex.org/covers/${id}/${coverImage}"
                    else  null
    )
}