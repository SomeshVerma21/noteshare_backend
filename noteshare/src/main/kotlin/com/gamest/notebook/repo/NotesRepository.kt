package com.gamest.notebook.repo

import com.gamest.notebook.notes.models.NotesMain
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
@EnableMongoRepositories
interface NotesRepository : PagingAndSortingRepository<NotesMain,Long> {

    fun findByCategory(category:String):List<NotesMain>

    fun findByName(name:String):List<NotesMain>

}