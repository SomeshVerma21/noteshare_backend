package com.gamest.notebook.repo

import com.gamest.notebook.user.models.UserProfileImage
import org.springframework.data.repository.PagingAndSortingRepository

interface ProfileImageRepository : PagingAndSortingRepository<UserProfileImage, Long>{
    fun getByUserId(userId:Long):UserProfileImage
}