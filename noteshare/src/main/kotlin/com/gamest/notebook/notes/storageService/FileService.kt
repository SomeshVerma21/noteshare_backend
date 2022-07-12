package com.gamest.notebook.notes.storageService

import com.gamest.notebook.notes.models.LoadFile
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.user.models.ProfileImage
import com.mongodb.client.gridfs.model.GridFSFile
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun saveFile(uploadFile: MultipartFile, notesMain: NotesMain): NotesMain?

    fun downloadFile(path:String) : LoadFile?

    fun uploadProfileImage(uploadFile: MultipartFile, userId:Int): Boolean

    fun loadProfileImage(path:String) : ProfileImage?
    fun updateProfileImage(uploadFile: MultipartFile, userId: Int): Boolean
}