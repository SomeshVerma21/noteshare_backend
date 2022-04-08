package com.gamest.notebook.notes.models

import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "dbnotesequence")
data class DbNoteSequence(
    val id:String,
    val seq:Long
)