package com.codewithdipesh.mangareader.presentation.elements

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

fun setScreenBrightness(
    brightness : Float,
    activity : Activity?
) {
    activity?.window?.attributes = activity?.window?.attributes?.apply {
            screenBrightness = brightness
    }

}