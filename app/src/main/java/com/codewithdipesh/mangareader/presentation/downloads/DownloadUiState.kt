package com.codewithdipesh.mangareader.presentation.downloads

import com.codewithdipesh.mangareader.domain.model.Downloads

data class DownloadUiState(
    val downloads: Downloads = Downloads(),
    val isLoading :Boolean = false
)
