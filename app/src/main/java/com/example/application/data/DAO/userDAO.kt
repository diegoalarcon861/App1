package com.example.application.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.application.data.Usuario

@Dao
interface userDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVariedad(usuarios: List<Usuario>): List<Long>

    @Update
    suspend fun actualizar(usuario: Usuario)
    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT * FROM USERS WHERE email =:email LIMIT 1")
    suspend fun consulta(email: String): Usuario?



}