package com.example.application.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.application.data.DAO.userDAO

@Database(
    entities = [Usuario::class],
    version = 1,
    exportSchema = true
)

abstract class Scheduler : RoomDatabase() {
    abstract fun userDao(): userDAO

    companion object {
        @Volatile
        private var instance: Scheduler? = null
        fun getInstance(context: Context): Scheduler {
            return instance ?: synchronized(this){
                instance?:Room.databaseBuilder(
                    context.applicationContext,
                    Scheduler::class.java,
                    "DbApp.db"
                ).build().also { instance = it }

            }
        }
    }
}