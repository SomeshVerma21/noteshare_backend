package com.gamest.notebook.notes.models.ratings

import org.springframework.data.mongodb.core.mapping.Document


@Document("ratings")
data class Ratings(
    val id:Long,
    var rating:Int,
    var ratingText:String,
    var noteId:Int,
    var userId:Int
)
