package com.gamest.notebook.notes.services

import com.gamest.notebook.notes.models.NoteDetails
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.models.comments.UserComment

interface NotesService {
    fun saveNoteInfo(note:NotesMain): NotesMain?

    fun deleteInfo(noteId: String): String?

    fun getByCategory(category:String):List<NotesMain>

    fun findAllRecommended():List<NotesMain>

    fun findByName(name: String): List<NotesMain>

    fun findBydownloads(): List<NotesMain>?

    fun addComment(comment: UserComment):Boolean

    fun updateDownloads(pathUrl:String):Boolean

    fun getAllComments(noteId: Int): List<UserComment>

    fun getNoteDetails(noteId: Int): NoteDetails?

    fun downloadNote(noteId: Int)
}