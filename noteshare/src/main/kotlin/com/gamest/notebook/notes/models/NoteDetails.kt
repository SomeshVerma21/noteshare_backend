package com.gamest.notebook.notes.models

data class NoteDetails(
    var id: Long,

    val name:String,

    val desc:String,

    val uploaderName:String,

    var fileUrl:String?,

    val category:String,

    val subCategory:String,

    val tags:List<String>,

    var likesCount:Long?,

    var downloads:Long?
)
