package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.data.local.entity.FavouriteManga
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun MangaCard(
    modifier: Modifier = Modifier,
    manga:Manga?,
    cardWidth: Int = 150,
    cardHeight: Int = 225,
    onClick :()->Unit = {}
) {
    Box(
        modifier = modifier
            .width(cardWidth.dp)
            .clip(RoundedCornerShape(16.dp))
            .wrapContentHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier
                    .width(cardWidth.dp)
                    .height(cardHeight.dp)
                    .clip(RoundedCornerShape(12.dp))
            ){
                if (manga != null) {
                    AsyncImage(
                        model = manga.coverImage,
                        contentDescription = manga.title,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.defaultmangacover),
                        error = painterResource(R.drawable.defaultmangacover),
                    )
                }
            }

            if (manga != null) {
                Text(
                    text = manga.title?.take(30) ?: "",
                    modifier = Modifier.padding(4.dp)
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    textAlign =TextAlign.Start,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold ,
                        color = colorResource(R.color.white)
                    ),
                    maxLines = 2,

                    )
            }
        }


    }



}

@Composable
fun MangaCard(
    modifier: Modifier = Modifier,
    manga:FavouriteManga,
    cardWidth: Int = 150,
    cardHeight: Int = 225,
    onClick :()->Unit = {},
    onFavouriteToggle : ()->Unit = {}
) {
    Box(
        modifier = modifier
            .width(cardWidth.dp)
            .clip(RoundedCornerShape(16.dp))
            .wrapContentHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier
                    .width(cardWidth.dp)
                    .height(cardHeight.dp)
                    .clip(RoundedCornerShape(12.dp))
            ){
                if (manga != null) {
                    AsyncImage(
                        model = manga.coverImage,
                        contentDescription = manga.title,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.defaultmangacover),
                        error = painterResource(R.drawable.defaultmangacover),
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.favourite_small_icon),
                    contentDescription = "favourite",
                    tint = colorResource(R.color.yellow),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clickable {
                            onFavouriteToggle()
                        }
                )
            }

            if (manga != null) {
                Text(
                    text = manga.title?.take(30) ?: "",
                    modifier = Modifier.padding(4.dp)
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    textAlign =TextAlign.Start,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold ,
                        color = colorResource(R.color.white)
                    ),
                    maxLines = 2,

                    )
            }
        }
    }



}