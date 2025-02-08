package com.codewithdipesh.mangareader.presentation.elements

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun MangaCard(
    modifier: Modifier = Modifier,
    manga:Manga?,
    cardWidth: Int = 150,
    cardHeight: Int = 225,
) {
    Box(
        modifier = modifier
            .width(cardWidth.dp)
            .background(Color.DarkGray)
            .wrapContentHeight()
    ){
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier
                    .width(cardWidth.dp)
                    .height(cardHeight.dp)
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
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal ,
                        color = Color.White
                    ),
                    maxLines = 2

                )
            }
        }
    }



}