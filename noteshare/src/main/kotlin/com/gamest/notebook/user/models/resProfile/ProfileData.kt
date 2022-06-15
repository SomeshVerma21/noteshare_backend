package com.gamest.notebook.user.models.resProfile

data class ProfileData(
    var id:Long=1,

    val firstname:String,

    val lastname:String,

    val email:String,

    val isEmailverified:Boolean?
)
