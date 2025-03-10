package com.codewithdipesh.mangareader.domain.model

sealed class DownloadStatus(val name : String){
    object Downloading : DownloadStatus("Downloading")
    object Downloaded : DownloadStatus("Downloaded")
    data class Error(val message : String = "Error Happened") : DownloadStatus(message)
    object NotDownloading : DownloadStatus("NotDownloading")

    companion object {
        fun fromString(name: String): DownloadStatus {
            return when (name) {
                "Downloading" -> Downloading
                "Downloaded" -> Downloaded
                "NotDownloading" -> NotDownloading
                else -> Error(name)
            }
        }
    }
}
