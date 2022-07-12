package com.gamest.notebook.repo

import com.gamest.notebook.notes.models.ratings.Ratings
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRatingsRepository : PagingAndSortingRepository<Ratings, Long> {
}