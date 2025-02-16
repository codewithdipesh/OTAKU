package com.codewithdipesh.mangareader.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Rating(val name : String){
    object Safe : Rating("safe")
    object Suggestive : Rating("suggestive")
    object Erotica : Rating("erotica")
    object Pornographic : Rating("pornographic")

    companion object{
        fun fromString(name : String) : Rating{
            return when(name){
                "safe" -> Safe
                "suggestive" -> Suggestive
                "erotica" -> Erotica
                "pornographic" -> Pornographic
                else -> Safe
            }
            }
    }
}
