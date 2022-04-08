package com.gamest.notebook.notes.models

import org.springframework.data.mongodb.core.mapping.Document

@Document("notesmain")
data class NotesMain(
    var id: Long,

    val name:String,

    val desc:String,

    val userId:String,

    var fileUrl:String?,

    val category:String,

    val subCategory:String,

    val tags:List<String>,

    var likesCount:Long?,

    var comments:List<CommentUser>?,

    var downloads:Long?
)