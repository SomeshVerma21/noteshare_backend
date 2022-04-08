package com.gamest.notebook.user.service

import com.gamest.notebook.user.models.NewUser
import com.gamest.notebook.user.models.Response
import com.gamest.notebook.user.models.UserLoginOP
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun addNewUser(user:NewUser): Response
    fun getAllUsers(): List<NewUser>

    fun loginUser(email: String, pass: String): Response
}