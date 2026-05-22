package com.example.application.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.application.data.Post

@Dao
interface postDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(post: Post): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVariedad(posts: List<Post>)

    @Query("SELECT * FROM posts ORDER BY idPost DESC")
    suspend fun obtenerTodos(): List<Post>

    @Query("SELECT * FROM posts WHERE idPost = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Post?

    @Query("SELECT * FROM posts WHERE idUser = :idUser ORDER BY idPost DESC")
    suspend fun obtenerPorUsuario(idUser: Int): List<Post>

    @Update
    suspend fun actualizar(post: Post)

    @Delete
    suspend fun eliminar(post: Post)
}