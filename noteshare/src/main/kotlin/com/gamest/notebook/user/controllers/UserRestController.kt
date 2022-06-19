package com.gamest.notebook.user.controllers

import com.gamest.notebook.notes.models.NoteResponse
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.storageService.FileServiceImp
import com.gamest.notebook.user.models.LoginUser
import com.gamest.notebook.user.models.NewUser
import com.gamest.notebook.user.models.Response
import com.gamest.notebook.user.service.UserServiceImp
import org.apache.poi.ss.formula.functions.T
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/user")
class UserRestController {
    @Autowired private lateinit var userService: UserServiceImp
    @Autowired private lateinit var fileService: FileServiceImp

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
    fun getUserProfile(@RequestParam("user_id") userId:Int):ResponseEntity<Response>{
        val response = userService.getUserProfile(userId)
        return if (response != null){
            ResponseEntity(Response("success","user profile found success",response),HttpStatus.OK)
        }else{
            ResponseEntity(Response("failed","user profile not found",null),HttpStatus.OK)
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

    @PostMapping("/upload/profile-image")
    fun uploadProfileImage(@RequestParam("file")file: MultipartFile,@RequestParam("user_id") userId: Int):ResponseEntity<Response>{
        val result =  fileService.uploadProfileImage(file, userId)
        return if (result){
            ResponseEntity(Response("success","uploaded", listOf<T>()),HttpStatus.OK)
        }else{
            ResponseEntity(Response("success","uploaded", listOf<T>()),HttpStatus.OK)
        }
    }

    @GetMapping("/profile-image/{path}/{name}")
    fun loadProfileImage(@PathVariable("path") path:String): ResponseEntity<ByteArrayResource>{
        val loadFile =  fileService.loadProfileImage(path)
        return if (loadFile != null) {
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.fileType ))
                .body( ByteArrayResource(loadFile.file))
        }else{
            ResponseEntity.notFound()
                .build()
        }
    }
}