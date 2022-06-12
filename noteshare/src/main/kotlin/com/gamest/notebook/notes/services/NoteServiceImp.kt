package com.gamest.notebook.notes.services

import com.gamest.notebook.notes.models.DbNoteSequence
import com.gamest.notebook.notes.models.NoteDetails
import com.gamest.notebook.notes.models.NotesMain
import com.gamest.notebook.notes.models.comments.UserComment
import com.gamest.notebook.repo.NotesRepository
import com.mongodb.DBObject
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.not
import com.mongodb.client.model.Updates
import org.apache.catalina.User
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList


@Service
class NoteServiceImp: NotesService {
    @Autowired private lateinit var notesRepository: NotesRepository
    @Autowired private lateinit var mongoOperations:MongoOperations
    @Autowired private lateinit var template: MongoTemplate

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

    override fun addComment(comment: UserComment): Boolean{
        val collection = template.db.getCollection("notesmain")
        val pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )
//        val settings = MongoClientSettings.builder()
//            .codecRegistry(pojoCodecRegistry)
//            .build()
//        val mongoClient = MongoClients.create(settings)
        return try {
            collection.withCodecRegistry(pojoCodecRegistry)
                .updateOne(eq("_id",comment.note_id),Updates.push("comments",comment))
            true
        }catch (e:Exception){
            false
        }finally {
            getAllComments(noteId = comment.note_id)
        }
    }

    override fun getAllComments(noteId: Int): MutableList<UserComment>? {
        val pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )
        return try {
            val collection = template.db.getCollection("notesmain")
            val result = collection.find(eq("_id",noteId))
            val commentsDocument = result.first()?.get("comments",ArrayList<Document>())
            val list = mutableListOf<UserComment>()
            if (commentsDocument != null){
                for (i in commentsDocument){
                    list.add(UserComment(
                        comment_id = Integer.parseInt(i.get("comment_id").toString()),
                        note_id = Integer.parseInt(i.get("note_id").toString()),
                        user_id = Integer.parseInt(i.get("user_id").toString()),
                        comment = i.get("comment").toString(),
                        date = i.get("date").toString()
                    ))
                }
                list
            }else{
                null
            }
        }catch (e:Exception){
            null
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
                result.userId,
                result.fileUrl,
                result.category,
                result.subCategory,
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