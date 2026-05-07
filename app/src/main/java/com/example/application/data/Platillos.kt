package com.example.application.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Platillos",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns =  ["idUser"],
        onDelete = ForeignKey.CASCADE

    )],
    indices = [Index(value = ["idUser"])]
)

data class Platillos(
    @PrimaryKey (autoGenerate = true)
    val idPlatillos: Long = 0,
    val idUser : Long,
    val name: String ,
    val image : String ,
    val descrip : String,
    val type : String,
    val difficult : String ,
    val time : String ,

)
