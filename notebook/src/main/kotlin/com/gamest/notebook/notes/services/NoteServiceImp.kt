package com.gamest.notebook.notes.services

import com.gamest.notebook.notes.models.DbNoteSequence
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.repo.NotesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*

@Service
class NoteServiceImp: NotesService {
    @Autowired private lateinit var notesRepository: NotesRepository
    @Autowired private lateinit var mongoOperations:MongoOperations

    override fun saveNoteInfo(note:NotesMain):NotesMain?{
        note.id = getSequenceNumber("note_sequence")
        return try {
            notesRepository.save(note)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteInfo(noteId:String):String?{
        return try {
            val response = notesRepository.findById(noteId.toLong()).also {
                notesRepository.deleteById(noteId.toLong())
            }
            response.get().fileUrl
        }catch (e:Exception){
            "false"
        }

    }
    override fun getByCategory(category: String): List<NotesMain> {
        return notesRepository.findByCategory(category)
    }

    override fun findAllRecommended(): List<NotesMain> {
        return notesRepository.findAll() as List<NotesMain>
    }

    override fun findByName(name:String):List<NotesMain>{
        val res =  notesRepository.findByName(name)
        println(res.size)
        return res
    }

    fun getSequenceNumber(sequenceName: String?): Long {
        //get sequence no
        val query = Query(Criteria.where("id").`is`(sequenceName))
        //update the sequence no
        val update = Update().inc("seq", 1)
        //modify in document
        val counter = mongoOperations.findAndModify(
            Query.query(Criteria.where("_id").`is`(sequenceName)),
            Update().inc("seq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
            DbNoteSequence::class.java
        )
        return if (!Objects.isNull(counter)) counter!!.seq else 1
    }
}