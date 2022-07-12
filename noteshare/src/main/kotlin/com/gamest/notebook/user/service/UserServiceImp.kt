package com.gamest.notebook.user.service

import com.gamest.notebook.Utils
import com.gamest.notebook.repo.CreateUserRepo
import com.gamest.notebook.repo.ProfileImageRepository
import com.gamest.notebook.user.models.*
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.Filters
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

@Service
 class UserServiceImp(@Autowired private val userRepo: CreateUserRepo,
                      @Autowired private val template: MongoTemplate,
                      @Autowired private val profileImageRepository:ProfileImageRepository,
                      @Autowired private val mongoOperations: MongoOperations): UserService {

    val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )
    override fun addNewUser( user:NewUserInput):Response{
        val checkEmail = userRepo.findByEmail(user.email)
        return if (checkEmail.isNotEmpty())
            Response(status = "failed", message = "user already exits",null).also { println("user already exits") }
        else{
            val response = userRepo.save(NewUser(id = getSequenceNumber("usersid"), fullName = user.fullName,
                email = user.email , password = user.password , isemailverified = false, mobile = 0 ))
            return Response(status = "success", message = "user created", null)
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
                Response(status = "success", message = "success",UserLoginOP(id = list[0].id.toInt()
                , fullName = list[0].fullName, email = list[0].email))
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
                result = ProfileData(
                    id = response.get().id,
                    fullName = response.get().fullName,
                    email = response.get().email,
                    mobile = response.get().mobile,
                    profileImage = getProfileUrl(userId),
                    isEmailverified = response.get().isemailverified
                )
            }
            result
        }catch (e:Exception){
            null
        }
    }

    private fun getProfileUrl(userId: Int):String{
        return try {
            val result = profileImageRepository.getByUserId(userId = userId.toLong())
            Utils.BASE_URL_PROFILE_IMAAGE+result.filePath
        }catch (e:Exception){
            ""
        }
    }

    override fun uploadProfileImage(filePath:String, userId: Int): Boolean{
        return try {
            val result = profileImageRepository.save(UserProfileImage(getSequenceNumber("profileImage"),
                userId = userId.toLong(), filePath = filePath))
            true
        }catch (e:Exception){
            false
        }
    }

    override fun updateProfileImage(filePath:String, userId: Int): Boolean{
        return try {
            val result = profileImageRepository.getByUserId(userId = userId.toLong())
            result.filePath = filePath
            profileImageRepository.save(result)
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