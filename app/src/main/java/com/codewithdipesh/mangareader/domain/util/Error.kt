package com.codewithdipesh.mangareader.domain.util

sealed  class AppError(val message :String){
    data class NetworkError(val errorMessage : String = "No internet connection") : AppError(errorMessage)
    data class ServerError(val errorMessage : String = "Server Error") : AppError(errorMessage)
    data class UnknownError(val errorMessage : String = "Something went wrong") : AppError(errorMessage)
}
