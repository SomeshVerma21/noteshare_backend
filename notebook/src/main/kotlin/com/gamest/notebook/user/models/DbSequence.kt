package com.gamest.notebook.user.models

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "dbusersequence")
data class DbSequence(
    val id:String,
    val seq:Long
)
