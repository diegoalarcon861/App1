package com.example.application.data

import com.example.application.data.DAO.userDAO

class UserRepository(private val userDao: userDAO) {
    
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
}
