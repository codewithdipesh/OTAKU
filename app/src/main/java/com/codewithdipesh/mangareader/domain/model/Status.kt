package com.codewithdipesh.mangareader.domain.model

sealed class Status(val name : String){
    object Ongoing : Status("ongoing")
    object Completed : Status("completed")
    object cancelled : Status("cancelled")
    object hiatus : Status("hiatus")

    companion object{
        fun fromString(name : String) : Status{
            return when(name){
                "ongoing" -> Ongoing
                "completed" -> Completed
                "cancelled" -> cancelled
                "hiatus" -> hiatus
                else -> Ongoing
            }
        }
    }
}
