package com.gamest.notebook.notes.services

import com.gamest.notebook.notes.models.DbNoteSequence
import com.gamest.notebook.notes.models.NoteDetails
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.models.comments.UserComment
import com.gamest.notebook.notes.models.ratings.RatingInput
import com.gamest.notebook.notes.models.ratings.Ratings
import com.gamest.notebook.repo.CommentsRepository
import com.gamest.notebook.repo.NotesRepository
import com.gamest.notebook.repo.UserRatingsRepository
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*


@Service
class NoteServiceImp: NotesService {
    @Autowired private lateinit var notesRepository: NotesRepository
    @Autowired private lateinit var commentsRepository: CommentsRepository
    @Autowired private lateinit var userRatingsRepository: UserRatingsRepository
    @Autowired private lateinit var mongoOperations:MongoOperations
    @Autowired private lateinit var template: MongoTemplate

    val pojoCodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

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

    override fun updateDownloads(pathUrl:String):Boolean{
        return try {
            val collection = template.db.getCollection("notesmain")
            collection.withCodecRegistry(pojoCodecRegistry)
                .updateOne(eq("fileUrl",pathUrl),Updates.inc("downloads",1))
            true
        }catch (e:Exception){
            false
        }
    }

    override fun addComment(comment: UserComment): Boolean{
        return try {
            comment.commentId = getSequenceNumber("comment_sequence").toInt()
            val result = commentsRepository.save(comment)
            println(result.toString())
            true
        }catch (e:Exception){
            false
        }
    }

    override fun addRating(ratingInput: RatingInput): Boolean{
        return try {
            val result = userRatingsRepository.save(Ratings(getSequenceNumber("ratings"), rating = ratingInput.rating,
                ratingText = ratingInput.ratingText, userId = ratingInput.userId, noteId = ratingInput.noteId))
            true
        }catch (e:Exception){
            false
        }
    }

    override fun getAllComments(noteId: Int): List<UserComment> {
        return commentsRepository.findByNoteId(note_id = noteId)
    }

    override fun getByCategory(category: String): List<NotesMain> {
        return notesRepository.findByCategory(category)
    }

    override fun findAllRecommended(): List<NotesMain> {
        return notesRepository.findAll() as List<NotesMain>
    }

    override fun findByName(name:String):List<NotesMain>{
        return try {
            val result =  notesRepository.findAll().toList()
            println(result.size)
            result.filter { s -> s.name == name }
        }catch (e:Exception){
            println(e.message)
            listOf()
        }
    }

    override fun findBydownloads(): List<NotesMain>? {
        return try {
            val res = notesRepository.findAll()
            res.sortedByDescending { it.downloads }
        }catch (e:Exception){
            null
        }
    }

    override fun getNoteDetails(noteId: Int): NoteDetails? {
        val res = notesRepository.findById(noteId.toLong())
        if (res.isPresent){
            val result = res.get()
            return NoteDetails(
                result.id,
                result.name,
                result.desc,
                result.userName,
                result.userId,
                result.fileUrl,
                result.category,
                result.subCategory,
                result.price,
                result.tags,
                result.likesCount,
                result.downloads,
            )
        }else{
            return null
        }
    }

    override fun downloadNote(noteId: Int) {

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