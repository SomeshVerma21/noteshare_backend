package com.gamest.notebook.user.service

import com.gamest.notebook.user.models.NewUser
import com.gamest.notebook.user.models.NewUserInput
import com.gamest.notebook.user.models.Response
import com.gamest.notebook.user.models.ProfileData
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun addNewUser(user: NewUserInput): Response

    fun getAllUsers(): List<NewUser>

    fun loginUser(email: String, pass: String): Response

    fun getUserProfile(userId:Int): ProfileData?

    fun uploadProfileImage(fileId:String, userId: Int):Boolean

    fun updateProfileImage(filePath: String, userId: Int): Boolean
}