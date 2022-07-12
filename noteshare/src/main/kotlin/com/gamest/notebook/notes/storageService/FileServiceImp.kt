package com.gamest.notebook.notes.storageService

import com.gamest.notebook.notes.dataresource.Categories
import com.gamest.notebook.notes.models.LoadFile
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.services.NoteServiceImp
import com.gamest.notebook.user.models.ProfileImage
import com.gamest.notebook.user.service.UserServiceImp
import com.mongodb.BasicDBObject
import org.apache.poi.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileServiceImp: FileService {
    @Autowired private lateinit var template:GridFsTemplate
    @Autowired private lateinit var noteService:NoteServiceImp
    @Autowired private lateinit var mongoTemplate: MongoTemplate
    @Autowired private lateinit var userService:UserServiceImp

    override fun saveFile(uploadFile: MultipartFile, notesMain: NotesMain): NotesMain? {
        return try {
            val metadata = BasicDBObject()
            metadata["fileSize"] = uploadFile.size
            metadata["contentType"] = uploadFile.contentType

            val fileID = template.store(
                uploadFile.inputStream, uploadFile.originalFilename, uploadFile.contentType, metadata
            )
            notesMain.fileUrl = fileID.toString()
            // make all count to initial as it is just uploaded
            return noteService.saveNoteInfo(notesMain)
        }catch (e:Exception){
            null
        }

    }

    override fun downloadFile(path: String): LoadFile? {
        return try{
            val gridFile = template.findOne(Query.query(Criteria.where("_id").`is`(path)))
            LoadFile(
                gridFile.filename,
                gridFile.metadata?.get("_contentType").toString(),
                gridFile.metadata?.get("fileSize").toString(),
                IOUtils.toByteArray(template.getResource(gridFile).inputStream)
            ).also {
                val resutl = noteService.updateDownloads(path)
                if (!resutl)
                    throw Exception("something went wrong")
            }
        }catch (e:Exception){
            null
        }
    }

    fun getAllCategories():MutableList<Categories>?{
        return try {
            val res = mongoTemplate.findAll(Categories::class.java)
            if (res.isNotEmpty())
                return res
            else
                throw Exception("no data available")
        }catch (e:Exception){
            null
        }
    }

    override fun uploadProfileImage(uploadFile: MultipartFile, userId:Int): Boolean{
        return try {
            val metadata = BasicDBObject()
            metadata["fileSize"] = uploadFile.size
            metadata["contentType"] = uploadFile.contentType

            val fileID = template.store(
                uploadFile.inputStream, uploadFile.originalFilename, uploadFile.contentType, metadata
            )
            val fileId = fileID.toString() + "/" + uploadFile.originalFilename
            userService.uploadProfileImage(fileId ,userId)
        }catch (e:Exception){
            false
        }
    }

    override fun updateProfileImage(uploadFile: MultipartFile, userId:Int): Boolean{
        return try {
            val metadata = BasicDBObject()
            metadata["fileSize"] = uploadFile.size
            metadata["contentType"] = uploadFile.contentType

            val fileID = template.store(
                uploadFile.inputStream, uploadFile.originalFilename, uploadFile.contentType, metadata
            )
            val fileId = fileID.toString() + "/" + uploadFile.originalFilename
            userService.updateProfileImage(fileId ,userId)
        }catch (e:Exception){
            false
        }
    }

    override fun loadProfileImage(path: String): ProfileImage? {
        return try{
            val gridFile = template.findOne(Query.query(Criteria.where("_id").`is`(path)))
            ProfileImage(
                gridFile.filename,
                gridFile.metadata?.get("_contentType").toString(),
                gridFile.metadata?.get("fileSize").toString(),
                IOUtils.toByteArray(template.getResource(gridFile).inputStream)
            )
        }catch (e:Exception){
            null
        }
    }
}