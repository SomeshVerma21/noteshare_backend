package com.gamest.notebook.user.models

data class ProfileData(
    var id:Long,

    val fullName:String,

    val email:String,

    val mobile:Int,

    val profileImage:String,

    val isProfileVerified:Boolean?,

    val isEmailverified:Boolean?
)
