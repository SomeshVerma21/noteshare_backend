package com.gamest.notebook.notes.storageService

import com.gamest.notebook.notes.dataresource.Categories
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.services.NoteServiceImp
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileServiceImp: FileService {
    @Autowired private lateinit var template:GridFsTemplate
    @Autowired private lateinit var gridOperations:GridFsOperations
    @Autowired private lateinit var noteService:NoteServiceImp

    override fun saveFile(uploadFile: MultipartFile, notesMain: NotesMain): NotesMain? {
        val metadata = BasicDBObject()
        metadata["fileSize"] = uploadFile.size

        val fileID = template.store(
            uploadFile.inputStream, uploadFile.originalFilename, uploadFile.contentType, metadata
        )
        notesMain.fileUrl = fileID.toString()
        // make all count to initial as it is just uploaded
        notesMain.likesCount=0
        notesMain.comments=null
        notesMain.downloads=0
        return noteService.saveNoteInfo(notesMain)
    }


    fun getAllCategories():MutableList<Categories>?{
        val mongoTemplate = MongoTemplate(MongoClients.create("mongodb://localhost:27017"),"noteshare")
        val res = mongoTemplate.findAll(Categories::class.java)
        return if (res.isNotEmpty()){
            res
        }else {
            null
        }
    }
}