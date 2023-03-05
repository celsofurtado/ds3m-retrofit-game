package br.senai.sp.gamesenairetrofit.model

data class Game(
    var id: Long = 0,
    var title: String = "",
    var studio: String = "",
    var description: String = "",
    var releaseYear: Int
){
    override fun toString(): String {
        return "Game(id=$id, title='$title', studio='$studio', description='$description', releaseYear=$releaseYear)"
    }
}


