package com.gamest.notebook.notes.models

data class NoteDetails(
    var id: Long,

    val name:String,

    val desc:String,

    val userName:String,

    val uploader_id:Int,

    var fileUrl:String?,

    val category:String,

    val subCategory:String,

    val price:String,

    val tags:List<String>,

    var likesCount:Long?,

    var downloads:Long?
)
