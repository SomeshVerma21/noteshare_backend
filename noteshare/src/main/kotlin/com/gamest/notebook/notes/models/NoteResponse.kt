package com.gamest.notebook.notes.models


data class NoteResponse(
    val status: String,
    val message:String,
    val data:List<Any>?
)
