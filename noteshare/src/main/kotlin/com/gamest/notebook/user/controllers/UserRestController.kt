package com.gamest.notebook.user.controllers

import com.gamest.notebook.user.models.LoginUser
import com.gamest.notebook.user.models.NewUser
import com.gamest.notebook.user.models.Response
import com.gamest.notebook.user.models.resProfile.ResponseProfile
import com.gamest.notebook.user.service.UserServiceImp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserRestController {

    @Autowired private lateinit var userService: UserServiceImp

    @GetMapping("/connect")
    fun connect():String{
        return "Connected to server"
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody user: NewUser):ResponseEntity<Response>{
        val response = userService.addNewUser(user)
        return ResponseEntity(response,HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginUser: LoginUser):ResponseEntity<Response>{
        val response = userService.loginUser(loginUser.email,loginUser.password)
        return ResponseEntity(response,HttpStatus.OK)
    }

    @GetMapping("/profile")
    fun getUserProfile(@RequestParam("user_id") userId:Int):ResponseEntity<ResponseProfile>{
        val response = userService.getUserProfile(userId)
        return if (response != null){
            ResponseEntity(ResponseProfile("success","user profile found success",response),HttpStatus.OK)
        }else{
            ResponseEntity(ResponseProfile("failed","user profile not found",null),HttpStatus.OK)
        }
    }

    @GetMapping("/uploads")
    fun getAllUploads(){

    }

    @GetMapping("/owned")
    fun getAllOwned(){

    }

    @PostMapping("/getAllUsers")
    fun getAllUser():ResponseEntity<List<NewUser>>{
        return ResponseEntity(userService.getAllUsers(),HttpStatus.OK)
    }
}