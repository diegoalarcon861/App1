package com.example.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.application.data.Scheduler
import com.example.application.data.UserRepository
import com.example.application.ui.AppDestination
import com.example.application.ui.AppRootScreen
import com.example.application.ui.AppTopicosTheme
import com.example.application.ui.UserViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val db = Room.databaseBuilder(
            applicationContext,
            Scheduler::class.java,
            "usuario_database"
        ).build()

        val repository = UserRepository(db.userDao())
        viewModel = UserViewModel(repository)

        setContent {
            AppTopicosTheme {
                AppScreen(viewModel)
            }
        }
    }
}

@Composable
private fun AppScreen(viewModel: UserViewModel) {
    AppRootScreen(
        users = viewModel.usuarios,
        destination = viewModel.currentScreen.value,
        selectedUserId = viewModel.selectedUserId.value,
        onNavigateList = { viewModel.navegarALista() },
        onNavigateCreate = { viewModel.navegarACrear() },
        onOpenDetail = { viewModel.navegarADetalle(it) },
        onOpenEdit = { viewModel.navegarAEditar(it) },
        onOpenDelete = { viewModel.navegarAEliminar(it) },
        onSaveUser = { viewModel.guardarUsuario(it) },
        onUpdateUser = { viewModel.actualizarUsuario(it) },
        onConfirmDelete = { 
            viewModel.selectedUserId.value?.let { userId ->
                viewModel.eliminarUsuario(userId)
            }
        },
        onCancelAction = { viewModel.cancelar() }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    AppTopicosTheme {
        AppRootScreen(
            users = listOf(),
            destination = AppDestination.List,
            selectedUserId = null,
            onNavigateList = {},
            onNavigateCreate = {},
            onOpenDetail = {},
            onOpenEdit = {},
            onOpenDelete = {},
            onSaveUser = {},
            onUpdateUser = {},
            onConfirmDelete = {},
            onCancelAction = {}
        )
    }
}
