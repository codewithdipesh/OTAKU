package com.codewithdipesh.mangareader.presentation.elements

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SwipingCardAnimation(
    mangaList : List<Manga>,
    onClick : (Manga)->Unit,
    onSuccessLoading : () -> Unit = {}
) {
    val cards = remember{ mutableStateListOf<Manga>() }
    val animatedOffsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mangaList){
        cards.clear()
        cards.addAll(mangaList)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(310.dp)
        .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
            //swipe cards
            cards.reversed().forEachIndexed{ index, card ->
                Card(
                    shape = RectangleShape,
                    modifier = Modifier
                        .size(200.dp, 300.dp)
                        .graphicsLayer(
                            translationX = if (index == cards.lastIndex) animatedOffsetX.value else if(index%2 == 0) -150f else 150f,
                            rotationZ = if (index == cards.lastIndex) animatedOffsetX.value / 10f else if (index % 2 == 0) -15f else 15f,  // Rotation tied to swipe
                            scaleX = if(index == cards.lastIndex) 1f else 0.90f,
                            scaleY = if(index == cards.lastIndex) 1f else 0.90f
                        )
                        .offset(y = if(index == cards.lastIndex) 0.dp else 10.dp)
                        .draggable(
                            state = rememberDraggableState { delta ->
                                scope.launch {
                                    animatedOffsetX.snapTo(animatedOffsetX.value + delta)
                                }
                            },
                            orientation = Orientation.Horizontal,
                            onDragStopped = {
                                when { //.removeLast() function works on real card and .reversed() is only applied for approach view purpose
                                    animatedOffsetX.value > 200 -> { // Swipe Right
                                        val lastCard = cards.removeAt(cards.lastIndex)
                                        cards.add(0, lastCard)
                                        animatedOffsetX.snapTo(0f)
                                    }
                                    animatedOffsetX.value < -200 -> { // Swipe Left
                                         val firstCardIndex = cards.indexOfFirst { it == card }
                                        val firstCard = cards.removeAt(firstCardIndex)
                                        cards.add(firstCard)
                                        animatedOffsetX.snapTo(0f)
                                    }
                                    else -> { // Reset Position if swipe isn't enough
                                        animatedOffsetX.animateTo(0f)
                                    }
                                }
                            }
                        )
                ) {
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            onClick(card)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        card.coverImage?.let {
                            AsyncImage(
                                model = card.coverImage,
                                contentDescription = card.title,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.defaultmangacover),
                                error = painterResource(R.drawable.defaultmangacover),
                                onSuccess = {
                                    onSuccessLoading()
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(if (index == cards.lastIndex) 1f else 0.65f)
                            )
                        }
                    }
                }
            }
    }
}

