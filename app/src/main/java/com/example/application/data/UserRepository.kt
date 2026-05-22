package com.example.application.data

import com.example.application.data.DAO.userDAO
import com.example.application.data.DAO.postDAO

class UserRepository(
    private val userDao: userDAO,
    private val postDao: postDAO,
) {
    
    suspend fun insertarUsuario(usuario: Usuario): Long {
        return userDao.insertar(usuario)
    }

    suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        return userDao.consulta(email)
    }

    suspend fun eliminarUsuario(usuario: Usuario) {
        userDao.eliminar(usuario)
    }

    suspend fun insertarMultiplesUsuarios(usuarios: List<Usuario>) {
        userDao.insertarVariedad(usuarios)
    }

    suspend fun obtenerTodosLosUsuarios(): List<Usuario> {
        return userDao.obtenerTodos()
    }

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        return userDao.obtenerPorId(id)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        userDao.actualizar(usuario)
    }

    suspend fun insertarPost(post: Post): Long {
        return postDao.insertar(post)
    }

    suspend fun insertarMultiplesPosts(posts: List<Post>) {
        postDao.insertarVariedad(posts)
    }

    suspend fun obtenerTodosLosPosts(): List<Post> {
        return postDao.obtenerTodos()
    }

    suspend fun obtenerPostPorId(id: Int): Post? {
        return postDao.obtenerPorId(id)
    }

    suspend fun obtenerPostsPorUsuario(idUser: Int): List<Post> {
        return postDao.obtenerPorUsuario(idUser)
    }

    suspend fun actualizarPost(post: Post) {
        postDao.actualizar(post)
    }

    suspend fun eliminarPost(post: Post) {
        postDao.eliminar(post)
    }
}
