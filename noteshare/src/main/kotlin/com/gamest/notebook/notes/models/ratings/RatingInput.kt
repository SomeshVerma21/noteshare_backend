package com.gamest.notebook.notes.models.ratings

data class RatingInput(
    var userId:Int,
    var noteId:Int,
    var rating:Int,
    var ratingText:String
)
