package com.example.application.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.application.data.DAO.postDAO
import com.example.application.data.DAO.userDAO

@Database(
    entities = [Usuario::class, Post::class],
    version = 2,
    exportSchema = false
)
abstract class Scheduler : RoomDatabase() {
    abstract fun userDao(): userDAO
    abstract fun postDao(): postDAO
}
