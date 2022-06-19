package com.gamest.notebook.user.service

import com.gamest.notebook.notes.models.comments.UserComment
import com.gamest.notebook.repo.CreateUserRepo
import com.gamest.notebook.user.models.*
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*
import kotlin.coroutines.coroutineContext


@Service
 class UserServiceImp(@Autowired private val userRepo: CreateUserRepo,
                      @Autowired private val template: MongoTemplate,
                      @Autowired private val mongoOperations: MongoOperations): UserService {

    val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )
    override fun addNewUser( user:NewUser):Response{
        val checkEmail = userRepo.findByEmail(user.email)
        return if (checkEmail.isNotEmpty())
            Response(status = "failed", message = "user already exits",null).also { println("user already exits") }
        else{
            user.id=getSequenceNumber("user_sequence")
            val response = userRepo.save(user)
            return Response(status = "success", message = "user created",UserLoginOP(id = response.id.toString(),
                firstname = response.firstname, lastname = response.lastname
                , email = response.email))
        }

    }

    override fun getAllUsers():List<NewUser>{
        return userRepo.findAll() as List<NewUser>
    }

    override fun loginUser(email:String,pass:String): Response {
        val list = userRepo.findByEmail(email =email)
        return if (list.isNotEmpty()){
            val password = list[0].password
            if (pass == password)
                Response(status = "success", message = "success",UserLoginOP(id = list[0].id.toString()
                , firstname = list[0].firstname, lastname = list[0].lastname, email = list[0].email))
            else
                Response(status = "failed", message = "incorrect password",null)
        }else
            Response(status = "failed", message = "user not found",null)
    }

    override fun getUserProfile(userId:Int): ProfileData?{
        return try {
            val response = userRepo.findById(userId.toLong())
            var result: ProfileData? = null
            if (response.isPresent){
                result = getProfileUrl(userId)?.let {
                    ProfileData(
                        id = response.get().id,
                        firstname = response.get().firstname,
                        lastname = response.get().lastname,
                        email = response.get().email,
                        profileImage = "http://localhost:8080/api/v1/user/profile-image/$it",
                        isEmailverified = response.get().isemailverified
                    )
                }
            }
            result
        }catch (e:Exception){
            null
        }
    }

    private fun getProfileUrl(userId: Int):String?{
        return try {
            val collection = template.db.getCollection("usermain")
            val result = collection.find(Filters.eq("_id", userId))
            val document = result.first()?.get("profile_image", ArrayList<String>())
            val list = mutableListOf<String>()
            if (document != null) {
                for (i in document) {
                    list.add(i)
                }
            }
            println(list.last())
            list.last()
        }catch (e:Exception){
            null
        }
    }

    override fun uploadProfileImage(fileId:String, userId: Int): Boolean{
        return try {
            val collection = template.db.getCollection("usermain")
            collection.withCodecRegistry(pojoCodecRegistry)
                .updateOne(Filters.eq("_id", userId),Updates.push("profile_image",fileId))
            true
        }catch (e:Exception){
            false
        }
    }

    fun getSequenceNumber(sequenceName: String?): Long {
        //get sequence no
        val query = Query(Criteria.where("id").`is`(sequenceName))
        //update the sequence no
        val update = Update().inc("seq", 1)
        //modify in document
        val counter = mongoOperations.findAndModify(
            query(where("_id").`is`(sequenceName)),
            Update().inc("seq", 1), options().returnNew(true).upsert(true),
            DbSequence::class.java
        )
        return if (!Objects.isNull(counter)) counter!!.seq else 1
    }

}