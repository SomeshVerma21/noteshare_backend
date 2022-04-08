package com.gamest.notebook.user.models

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.*

@Document("usermain")
data class NewUser(

    @Id
    var id:Long=1,

    @Column(name = "firstname")
    val firstname:String,

    @Column(name = "lastname")
    val lastname:String,

    @Column(name = "email")
    val email:String,

    @Column(name = "password")
    val password:String,

    @Column(name = "isemailverified")
    val isemailverified:String?
)