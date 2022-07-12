package com.gamest.notebook.repo

import com.gamest.notebook.user.models.NewUser
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
@EnableMongoRepositories
interface CreateUserRepo: PagingAndSortingRepository<NewUser,Long> {

    fun findByEmail(email: String):List<NewUser>


}