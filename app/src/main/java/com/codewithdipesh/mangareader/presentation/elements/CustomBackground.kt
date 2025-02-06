package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.dottedBackground(
    color: Color = androidx.compose.ui.graphics.Color.Black,
    gap: Float = 15f,
    dotSize:Float=3f
)=this
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Black.copy(alpha = 1f),
                Color.Black.copy(alpha = 0.8f),
                Color.Black.copy(alpha = 0.5f),
                Color.Black.copy(alpha = 0.3f),
                Color.Black.copy(alpha = 0.2f),
                Color.Black.copy(alpha = 0.1f),
            )
        )

    )
    .drawBehind{
        val width = size.width
        val height = size.height

        //number of rows and columns
        val rows = (height/gap).toInt()
        val columns = (width/gap).toInt()

        for(row in 0..rows){
            for(column in 0..columns){
                val alphaFactor = 1f - (row.toFloat() / rows)

                drawCircle(
                    color =color.copy(alpha = alphaFactor),
                    radius = dotSize,
                    center = androidx.compose.ui.geometry.Offset(
                        x = (column)*gap,
                        y = (row)*gap
                    )
                )
            }
        }
    }
