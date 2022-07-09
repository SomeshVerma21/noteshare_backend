package com.gamest.notebook.repo

import com.gamest.notebook.notes.models.comments.UserComment
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
@EnableMongoRepositories
interface CommentsRepository : PagingAndSortingRepository<UserComment, Long>{
    fun findByNoteId(note_id : Int): List<UserComment>
}