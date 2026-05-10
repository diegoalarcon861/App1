package com.example.application.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val descrip: String,
    val image: String
)
