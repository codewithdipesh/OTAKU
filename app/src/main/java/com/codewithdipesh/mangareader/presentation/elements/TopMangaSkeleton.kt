package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun TopMangaSkeleton(
    modifier: Modifier = Modifier,
    brush : Brush
) {
    val animatedOffsetX = remember { Animatable(0f) }
    val emptyCards = listOf(1,2,3)
    Box(modifier = modifier
        .fillMaxWidth()
        .height(310.dp)
        .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        //swipe cards
        emptyCards.reversed().forEachIndexed{ index ,_->
            Box(
                modifier = Modifier
                    .size(200.dp, 300.dp)
                    .graphicsLayer(
                        translationX = if (index == emptyCards.lastIndex) animatedOffsetX.value else if(index%2 == 0) -150f else 150f,
                        rotationZ = if (index == emptyCards.lastIndex) animatedOffsetX.value / 10f else if (index % 2 == 0) -15f else 15f,  // Rotation tied to swipe
                        scaleX = if(index == emptyCards.lastIndex) 1f else 0.90f,
                        scaleY = if(index == emptyCards.lastIndex) 1f else 0.90f
                    )
                    .offset(y = if(index == emptyCards.lastIndex) 0.dp else 10.dp)
                    .background(brush)
            ){

            }
        }
    }

}