package com.codewithdipesh.mangareader.presentation.elements

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

fun setScreenBrightness(
    brightness : Float,
    activity : Activity?
) {
    val layout: WindowManager.LayoutParams? = activity?.window?.attributes
    layout?.screenBrightness = brightness
    activity?.window?.attributes = layout

}

fun resetToSystemBrightness(activity: Activity?) {
    val window = activity?.window
    val layoutParams = window?.attributes
    layoutParams?.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    window?.attributes = layoutParams
}