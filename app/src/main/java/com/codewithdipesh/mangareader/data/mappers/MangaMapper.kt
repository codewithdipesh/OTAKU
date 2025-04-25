package com.codewithdipesh.mangareader.data.mappers

import android.util.Log
import com.codewithdipesh.mangareader.data.remote.dto.Data
import com.codewithdipesh.mangareader.data.remote.dto.MangaResponse
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status

fun Data.toManga(coverImage : String):Manga{
    val genres : List<Map<String,String>> = attributes.tags
        .filter{ it.attributes.group == "genre"}
        .map { tag ->
            mapOf(tag.id to (tag.attributes.name.en ?: "Unknown"))
        }

    val themes : List<Map<String,String>> = attributes.tags
        .filter{ it.attributes.group == "theme"}
        .map {tag ->
            mapOf(tag.id to (tag.attributes.name.en ?: "Unknown"))
        }

    val titles = attributes.altTitles.firstOrNull()
    val japaneseTitle : String? = titles?.ja ?: titles?.ja_ro ?: titles?.ko?:
                                  titles?.ko_ro?:titles?.vi ?:titles?.id ?:
                                  titles?.tl ?: titles?.ru ?: titles?.zh ?: titles?.es_la

    Log.e("Chapter Size", "repo(api)-> toManga() -> ${attributes.lastChapter}")
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
                    else  null,
       authorId = relationships.find{ it.type == "author" }?.id,
       chapters = attributes.lastChapter?.toDoubleOrNull()?.toInt() ?: 0
    )
}