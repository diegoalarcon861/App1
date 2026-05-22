package com.example.application.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val idPost: Int = 0,
    val idUser: Int,
    val title: String,
    val description: String,
    val category: String,
    val image: String,
)