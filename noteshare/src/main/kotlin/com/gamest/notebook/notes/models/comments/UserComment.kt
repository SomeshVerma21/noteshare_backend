package com.gamest.notebook.notes.models.comments

data class UserComment(
    val comment_id:Int,

    val note_id:Int,

    val user_id:Int,

    val comment:String,

    val date:String
)
