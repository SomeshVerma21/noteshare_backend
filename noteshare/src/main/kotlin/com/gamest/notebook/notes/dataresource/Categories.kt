package com.gamest.notebook.notes.dataresource

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Id

@Document("notecategories")
data class Categories(
    val _id : String,
    val category : String,
    val subcategory : List<String>
)
