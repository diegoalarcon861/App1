package com.example.application.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.application.data.Usuario

typealias UserId = Int

enum class AppDestination {
    List,
    Create,
    Detail,
    Edit,
    Delete,
}

@Composable
fun AppTopicosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = createAppColorScheme(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen(
    users: List<Usuario>,
    destination: AppDestination,
    selectedUserId: UserId?,
    onNavigateList: () -> Unit,
    onNavigateCreate: () -> Unit,
    onOpenDetail: (UserId) -> Unit,
    onOpenEdit: (UserId) -> Unit,
    onOpenDelete: (UserId) -> Unit,
    onSaveUser: (Usuario) -> Unit,
    onUpdateUser: (Usuario) -> Unit,
    onConfirmDelete: () -> Unit,
    onCancelAction: () -> Unit,
) {
    val selectedUser = remember(users, selectedUserId) {
        selectedUserId?.let { id -> users.firstOrNull { it.idUser == id } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Gestión de usuarios", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (destination == AppDestination.List) {
                Button(
                    onClick = onNavigateCreate,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("+ Nuevo")
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (destination) {
                AppDestination.List -> UserListScreen(
                    users = users,
                    onCreate = onNavigateCreate,
                    onView = onOpenDetail,
                    onEdit = onOpenEdit,
                    onDelete = onOpenDelete
                )

                AppDestination.Create -> UserFormScreen(
                    title = "Crear usuario",
                    initialUser = null,
                    onSave = onSaveUser,
                    onCancel = onNavigateList
                )

                AppDestination.Edit -> UserFormScreen(
                    title = "Editar usuario",
                    initialUser = selectedUser,
                    onSave = onUpdateUser,
                    onCancel = onNavigateList
                )

                AppDestination.Detail -> UserDetailScreen(
                    user = selectedUser,
                    onBack = onNavigateList,
                    onEdit = { selectedUserId?.let(onOpenEdit) },
                    onDelete = { selectedUserId?.let(onOpenDelete) }
                )

                AppDestination.Delete -> DeleteConfirmationScreen(
                    user = selectedUser,
                    onConfirm = onConfirmDelete,
                    onCancel = onCancelAction
                )
            }
        }
    }
}

@Composable
private fun UserListScreen(
    users: List<Usuario>,
    onCreate: () -> Unit,
    onView: (UserId) -> Unit,
    onEdit: (UserId) -> Unit,
    onDelete: (UserId) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Usuarios registrados",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pantalla principal para listar, consultar y administrar usuarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (users.isEmpty()) {
            EmptyStateCard(onCreate = onCreate)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(users, key = { it.idUser }) { user ->
                    UserCard(
                        user = user,
                        onView = { onView(user.idUser) },
                        onEdit = { onEdit(user.idUser) },
                        onDelete = { onDelete(user.idUser) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(onCreate: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "No hay usuarios todavía",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Crea el primer registro para comenzar con el flujo básico de la app.",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onCreate,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = "+ Crear usuario")
            }
        }
    }
}

@Composable
private fun UserCard(
    user: Usuario,
    onView: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = user.descrip,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onView,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("Ver")
                }
                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("Editar")
                }
                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
private fun UserFormScreen(
    title: String,
    initialUser: Usuario?,
    onSave: (Usuario) -> Unit,
    onCancel: () -> Unit,
) {
    var name by rememberSaveable(initialUser?.idUser) { mutableStateOf(initialUser?.name.orEmpty()) }
    var email by rememberSaveable(initialUser?.idUser) { mutableStateOf(initialUser?.email.orEmpty()) }
    var password by rememberSaveable(initialUser?.idUser) { mutableStateOf(initialUser?.password.orEmpty()) }
    var descrip by rememberSaveable(initialUser?.idUser) { mutableStateOf(initialUser?.descrip.orEmpty()) }
    var image by rememberSaveable(initialUser?.idUser) { mutableStateOf(initialUser?.image.orEmpty()) }

    val canSave = name.isNotBlank() && email.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = descrip,
            onValueChange = { descrip = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descripción") },
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("URL de imagen") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    onSave(
                        Usuario(
                            idUser = initialUser?.idUser ?: 0,
                            name = name.trim(),
                            email = email.trim(),
                            password = password.trim(),
                            descrip = descrip.trim(),
                            image = image.trim()
                        )
                    )
                },
                enabled = canSave,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = if (initialUser == null) "Guardar" else "Actualizar")
            }
            OutlinedButton(
                onClick = onCancel,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancelar")
            }
        }
    }
}

@Composable
private fun UserDetailScreen(
    user: Usuario?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    if (user == null) {
        MissingUserState(onBack = onBack)
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Detalle de usuario",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailRow(label = "Nombre", value = user.name)
        DetailRow(label = "Correo", value = user.email)
        DetailRow(label = "Contraseña", value = user.password)
        DetailRow(label = "Descripción", value = user.descrip)
        DetailRow(label = "Imagen", value = user.image)
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.weight(1f)
            ) { Text("Volver") }
            OutlinedButton(
                onClick = onEdit,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Editar") }
            OutlinedButton(
                onClick = onDelete,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Eliminar") }
        }
    }
}

@Composable
private fun MissingUserState(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Usuario no encontrado", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Volver") }
        }
    }
}

@Composable
private fun DeleteConfirmationScreen(
    user: Usuario?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Eliminar usuario", fontWeight = FontWeight.Bold) },
        text = {
            Text(
                text = if (user == null) {
                    "No se encontró el usuario seleccionado."
                } else {
                    "¿Seguro que deseas eliminar a ${user.name}?"
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = user != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB3261E),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCancel,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value.ifBlank { "Sin información" })
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
