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

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun consulta(email: String): Usuario?

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVariedad(usuarios: List<Usuario>)

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE idUser = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Update
    suspend fun actualizar(usuario: Usuario)
}
