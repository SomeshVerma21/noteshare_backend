package com.gamest.notebook.user.models

data class ProfileData(
    var id:Long=1,

    val firstname:String,

    val lastname:String,

    val email:String,

    val profileImage:String,

    val isEmailverified:Boolean?
)
