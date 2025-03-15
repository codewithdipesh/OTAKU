package com.codewithdipesh.mangareader.domain.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

object DisplayUtils {
    @Composable
    fun calculateGridColumns(): Int {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        return when {
            screenWidth > 840.dp -> 4  // Large tablets
            screenWidth > 600.dp -> 3  // Regular tablets
            else -> 2                  // Phones
        }
    }
}