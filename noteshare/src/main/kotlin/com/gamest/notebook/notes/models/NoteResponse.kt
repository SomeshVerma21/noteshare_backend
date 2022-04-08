package com.gamest.notebook.notes.models

import java.sql.ClientInfoStatus

data class NoteResponse(
    val status: String,
    val message:String,
    val list:List<NotesMain>?
)
