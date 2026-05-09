package com.example.application.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.application.data.Usuario
import com.example.application.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) {
    
    val usuarios = mutableStateListOf<Usuario>()
    val currentScreen = mutableStateOf(AppDestination.List)
    val selectedUserId = mutableStateOf<UserId?>(null)
    
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    init {
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        viewModelScope.launch {
            val usuariosDb = repository.obtenerTodosLosUsuarios()
            usuarios.clear()
            usuarios.addAll(usuariosDb)
        }
    }

    fun guardarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            val nextId = (usuarios.maxOfOrNull { it.idUser } ?: 0) + 1
            val usuarioConId = usuario.copy(idUser = nextId)
            repository.insertarUsuario(usuarioConId)
            usuarios.add(usuarioConId)
            currentScreen.value = AppDestination.List
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            repository.actualizarUsuario(usuario)
            val index = usuarios.indexOfFirst { it.idUser == usuario.idUser }
            if (index >= 0) {
                usuarios[index] = usuario
            }
            currentScreen.value = AppDestination.List
        }
    }

    fun eliminarUsuario(userId: UserId) {
        viewModelScope.launch {
            val usuario = usuarios.firstOrNull { it.idUser == userId }
            if (usuario != null) {
                repository.eliminarUsuario(usuario)
                usuarios.removeAll { it.idUser == userId }
            }
            selectedUserId.value = null
            currentScreen.value = AppDestination.List
        }
    }

    fun navegarADetalle(userId: UserId) {
        selectedUserId.value = userId
        currentScreen.value = AppDestination.Detail
    }

    fun navegarAEditar(userId: UserId) {
        selectedUserId.value = userId
        currentScreen.value = AppDestination.Edit
    }

    fun navegarAEliminar(userId: UserId) {
        selectedUserId.value = userId
        currentScreen.value = AppDestination.Delete
    }

    fun navegarALista() {
        currentScreen.value = AppDestination.List
    }

    fun navegarACrear() {
        selectedUserId.value = null
        currentScreen.value = AppDestination.Create
    }

    fun cancelar() {
        selectedUserId.value = null
        currentScreen.value = AppDestination.List
    }

    fun obtenerUsuarioSeleccionado(): Usuario? {
        return selectedUserId.value?.let { id ->
            usuarios.firstOrNull { it.idUser == id }
        }
    }
}
