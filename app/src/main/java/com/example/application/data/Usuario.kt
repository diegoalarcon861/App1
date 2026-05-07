package com.example.application.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Users",
    indices = [Index(value =["email"], unique = true)]
)
data class Usuario(
@PrimaryKey(autoGenerate = true)
    val idUser: Long = 0,
    val name:String,
    val email: String,
    val password : String,
    val descrip : String,
    val image : String,
    val date : Long = System.currentTimeMillis()

)
