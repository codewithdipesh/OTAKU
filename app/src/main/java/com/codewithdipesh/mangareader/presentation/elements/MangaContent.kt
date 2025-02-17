package com.codewithdipesh.mangareader.presentation.elements

sealed  class MangaContent(val name : String){
    object Chapter : MangaContent("Chapters")
    object Details : MangaContent("Details")
    object Similar : MangaContent("Similar")
}
