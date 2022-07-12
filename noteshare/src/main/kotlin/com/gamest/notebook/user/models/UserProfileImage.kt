package com.gamest.notebook.user.models

import org.springframework.data.mongodb.core.mapping.Document

@Document("profileimage")
data class UserProfileImage(
    val id:Long,

    val userId:Long,

    var filePath:String,

    )
