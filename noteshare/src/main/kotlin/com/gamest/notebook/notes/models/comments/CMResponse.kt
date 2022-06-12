package com.gamest.notebook.notes.models.comments

data class CMResponse(
    val status:String,
    val message:String,
    val comments:List<UserComment>
)
