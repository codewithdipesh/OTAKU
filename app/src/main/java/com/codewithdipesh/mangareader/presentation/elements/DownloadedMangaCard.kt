package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import com.codewithdipesh.mangareader.domain.model.MangaDownloadedDetails
import com.codewithdipesh.mangareader.ui.theme.regular
import java.io.File

@Composable
fun DownloadedMangaCard(
    modifier: Modifier = Modifier,
    mangaDownloadedDetails: MangaDownloadedDetails,
    onClick : (String) ->Unit
) {
     Box(
         modifier = Modifier.fillMaxWidth()
             .clickable { onClick(mangaDownloadedDetails.id) },
         contentAlignment = Alignment.Center
     ){
         Row(
             modifier = Modifier.fillMaxWidth()
                 .padding(vertical = 8.dp),
             horizontalArrangement = Arrangement.Start,
             verticalAlignment = Alignment.CenterVertically
         ) {
             //image icon
             Box(modifier = Modifier
                 .size(150.dp),
                 contentAlignment = Alignment.TopCenter
             ){
                 AsyncImage(
                     model = File(mangaDownloadedDetails.coverImage),
                     contentDescription = "${mangaDownloadedDetails.title} coverImage",
                     contentScale = ContentScale.Crop
                 )
             }
             Spacer(modifier =Modifier.width(16.dp))
             //details
             Row(modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween,
                 verticalAlignment = Alignment.CenterVertically
             ){
                 //title ,chapters
                 Column {
                     Text(
                         text = mangaDownloadedDetails.title ?: "",
                         style = TextStyle(
                             color = Color.White,
                             fontSize = 16.sp,
                             fontFamily = regular,
                             fontWeight = FontWeight.Bold
                         )
                     )
                     Text(
                         text = "${mangaDownloadedDetails.totalChaptersDownloaded} Chapters",
                         style = TextStyle(
                             color = Color.Gray,
                             fontSize = 14.sp,
                             fontFamily = regular,
                             fontWeight = FontWeight.Bold
                         )
                     )
                 }
                 //show more
                 Icon(
                     painter = painterResource(R.drawable.show_more_icon),
                     contentDescription = "show ${mangaDownloadedDetails.title} chapters",
                     tint = Color.White,
                     modifier = Modifier.padding(8.dp)
                 )
             }
         }
     }
}