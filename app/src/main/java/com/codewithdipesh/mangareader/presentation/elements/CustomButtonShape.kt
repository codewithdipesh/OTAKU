package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier
import kotlin.io.path.Path

class CustomButtonShape(): Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(40f,0f)
            lineTo(size.width,0f)
            lineTo(size.width - 40f,size.height)
            lineTo(0f,size.height)
            close()
        }
        return Outline.Generic(path)
    }

}

@Preview
@Composable
fun ShapePreview(){
    Box(
       modifier =  androidx.compose.ui.Modifier.size(70.dp,60.dp)
           .clip(CustomButtonShape())
           .background(color = Color.Black)
    )
}