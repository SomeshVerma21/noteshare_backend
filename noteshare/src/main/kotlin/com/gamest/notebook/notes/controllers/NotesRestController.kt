package com.gamest.notebook.notes.controllers

import com.gamest.notebook.notes.dataresource.Categories
import com.gamest.notebook.notes.models.NoteDetails
import com.gamest.notebook.notes.models.NoteResponse
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.services.NoteServiceImp
import com.gamest.notebook.notes.storageService.FileServiceImp
import com.mongodb.client.gridfs.model.GridFSFile
import org.aspectj.util.FileUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/notes")
class NotesRestController {
    @Autowired private lateinit var noteServiceImp: NoteServiceImp
    @Autowired private lateinit var fileService: FileServiceImp

    @PostMapping("/upload")
    fun upload(@RequestParam("file")file: MultipartFile,@ModelAttribute notesMain: NotesMain ):ResponseEntity<NoteResponse>{
        val result =  fileService.saveFile(file,notesMain)
        return if (result!=null){
            val response = NoteResponse(status = "success","file uploaded", listOf(result))
            ResponseEntity(response,HttpStatus.OK)
        }else{
            val response = NoteResponse(status = "failed","wrong arguments", null)
            ResponseEntity(response,HttpStatus.OK)
        }
    }

    @GetMapping("/download")
    fun download(@RequestParam("path") path:String):  ResponseEntity<ByteArrayResource>{
        val loadFile =  fileService.downloadFile(path)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(loadFile.fileType ))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.filename + "\"")
            .body( ByteArrayResource(loadFile.file))
    }

    @PostMapping("/findbycategory")
    fun findByCategory(@RequestParam("category")category: String):ResponseEntity<NoteResponse>{
        val list = noteServiceImp.getByCategory(category)
        return if (list.isNotEmpty()){
            ResponseEntity(NoteResponse("success","found",list),HttpStatus.OK)
        }else{
            ResponseEntity(NoteResponse(status = "failed","No file found", listOf()),HttpStatus.OK)
        }
    }

    @GetMapping("/recommendednotes")
    fun findAllNotes():ResponseEntity<NoteResponse>{
        val result = noteServiceImp.findAllRecommended()
        return if (result.isNotEmpty()){
            ResponseEntity(NoteResponse(status = "success", message = "recommended",result),HttpStatus.OK)
        }else{
            ResponseEntity(NoteResponse(status = "failed", message = "no file found", listOf()),HttpStatus.OK)
        }
    }

    @PostMapping("/searchbyname")
    fun searchByName(@RequestParam("name") name:String):ResponseEntity<NoteResponse>{
        val result = noteServiceImp.findByName(name)
        return if (result.isNotEmpty())
            ResponseEntity(NoteResponse("success","notes by name",result),HttpStatus.OK)
        else{
            ResponseEntity(NoteResponse("failed","Nothing found", listOf()),HttpStatus.OK)
        }
    }

    @GetMapping("/getnotedetails")
    fun getNoteDetails(@RequestParam("noteid") noteId:Int): ResponseEntity<NoteResponse>{
        val result = noteServiceImp.getNoteDetails(noteId)
        return if (result != null){
            ResponseEntity(NoteResponse("success","note details", listOf(result)),HttpStatus.OK)
        }else{
            ResponseEntity(NoteResponse("failed","Nothing found", listOf() ),HttpStatus.OK)
        }
    }

    @PostMapping("/topDownloads")
    fun topDownloads():ResponseEntity<NoteResponse>{
        val result = noteServiceImp.findBydownloads()
        return if (result != null) {
            ResponseEntity(NoteResponse("success", "found top notes", result), HttpStatus.OK)
        }else{
            ResponseEntity(NoteResponse("failed", "nothing found", listOf()), HttpStatus.OK)
        }
    }

    @GetMapping("/getallcategories")
    fun getAllCategories():ResponseEntity<MutableList<Categories>>{
        val list = fileService.getAllCategories()
        return ResponseEntity(list,HttpStatus.OK)
    }
}