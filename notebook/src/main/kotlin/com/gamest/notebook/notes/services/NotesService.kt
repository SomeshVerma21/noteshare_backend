package com.gamest.notebook.notes.services

import com.gamest.notebook.notes.models.NotesMain

interface NotesService {
    fun saveNoteInfo(note:NotesMain): NotesMain?
    fun deleteInfo(noteId: String): String?
    fun getByCategory(category:String):List<NotesMain>
    fun findAllRecommended():List<NotesMain>
    fun findByName(name: String): List<NotesMain>
}