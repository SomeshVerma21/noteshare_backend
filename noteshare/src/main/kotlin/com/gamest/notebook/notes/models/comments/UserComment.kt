package com.gamest.notebook.notes.models.comments

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Column

@Document("user_comments")
data class UserComment(
    @Column(name = "comment_id")
    var commentId:Int,

    @Column(name = "note_id")
    val noteId:Int,

    @Column(name = "user_id")
    val userId:Int,

    @Column(name = "comment")
    val comment:String,

    @Column(name = "date")
    val date:String
)
