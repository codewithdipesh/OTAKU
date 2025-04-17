package com.codewithdipesh.mangareader.presentation.elements

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SwipingCardAnimation(
    mangaList : List<Manga>,
     onClick : (Manga)->Unit,
    animatedVisibilityScope : AnimatedVisibilityScope,
    onSuccessSwipe : (List<Manga>) -> Unit = {},
    onSuccessLoading : () -> Unit = {}
) {
    var cards by remember{ mutableStateOf(mangaList.toList()) }
    val animatedOffsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val dragOffset by animateFloatAsState(
        targetValue = animatedOffsetX.value,
        animationSpec = tween(durationMillis = 50),
        label = "dragOffset"
    )


    LaunchedEffect(mangaList){
        cards = mangaList
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
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(200.dp, 300.dp)
                        .graphicsLayer(
                            translationX = if (index == cards.lastIndex) dragOffset else if(index%2 == 0) -150f else 150f,
                            rotationZ = if (index == cards.lastIndex) animatedOffsetX.value / 10f else if (index % 2 == 0) -15f else 15f,  // Rotation tied to swipe
                            scaleX = if(index == cards.lastIndex) 1f else 0.90f,
                            scaleY = if(index == cards.lastIndex) 1f else 0.90f
                        )
                        .offset(y = if(index == cards.lastIndex) 0.dp else 10.dp)
                        .pointerInput(Unit){
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    scope.launch {
                                        when {
                                            animatedOffsetX.value > 200f -> {
                                                // Swipe left(last card to first card
                                                cards = cards.toMutableList().apply {
                                                    val lastCard = removeAt(lastIndex)
                                                    add(0,lastCard)
                                                }
                                                onSuccessSwipe(cards)
                                                animatedOffsetX.snapTo(0f)
                                            }
                                            animatedOffsetX.value < -200f -> {
                                                // Swipe right(first card to last)
                                                cards = cards.drop(1) + cards.first()
                                                onSuccessSwipe(cards)
                                                animatedOffsetX.snapTo(0f)
                                            }
                                            else -> {
                                                // Return to center
                                                animatedOffsetX.animateTo(0f,
                                                    animationSpec = spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = Spring.StiffnessLow
                                                    )
                                                )
                                            }
                                        }
                                    }
                                },
                                onHorizontalDrag = {change,dragAmount->
                                    change.consume()
                                    scope.launch {
                                        animatedOffsetX.snapTo(animatedOffsetX.value + dragAmount)
                                    }

                                }
                            )
                        }
                ) {
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onClick(card)
                        }
                        .sharedBounds(
                            rememberSharedContentState(key = "mangaImage/${card.coverImage}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            enter = fadeIn(),
                            exit = ExitTransition.None,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        ),
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
                                    .clip(RoundedCornerShape(16.dp))
                                    .alpha(if (index == cards.lastIndex) 1f else 0.65f)
                                    .then(
                                        if (index == cards.lastIndex) Modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
            }
    }
}

