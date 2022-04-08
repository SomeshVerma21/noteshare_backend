package com.gamest.notebook.notes.storageService

import com.gamest.notebook.notes.models.NotesMain
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun saveFile(uploadFile: MultipartFile, notesMain: NotesMain): NotesMain?
}