package com.example.application.ui

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Post
import com.example.application.data.Usuario
import com.example.application.data.UserRepository
import kotlinx.coroutines.launch

typealias PostId = Int

data class PostWithAuthor(
    val post: Post,
    val author: Usuario,
)

class UserViewModel(
    private val repository: UserRepository,
    appContext: Context,
) : ViewModel() {

    companion object {
        private const val SESSION_PREFS = "app_session"
        private const val KEY_LOGGED_USER_ID = "logged_user_id"
    }

    val usuarios = mutableStateListOf<Usuario>()
    val posts = mutableStateListOf<PostWithAuthor>()
    val savedPostIds = mutableStateListOf<PostId>()

    val currentScreen = mutableStateOf(AppDestination.Welcome)
    val selectedUserId = mutableStateOf<UserId?>(null)
    val selectedPostId = mutableStateOf<PostId?>(null)
    val selectedNotificationId = mutableStateOf<Int?>(null)
    val currentUserId = mutableStateOf<UserId?>(null)
    val errorMessage = mutableStateOf<String?>(null)
    val recipeFormToken = mutableStateOf(0)

    private val sessionPrefs = appContext.applicationContext
        .getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                if (repository.obtenerTodosLosUsuarios().isEmpty()) {
                    repository.insertarMultiplesUsuarios(seedUsers())
                }
                refreshUsers()

                if (repository.obtenerTodosLosPosts().isEmpty()) {
                    repository.insertarMultiplesPosts(seedRecipes(usuarios))
                }
                normalizeSeedRecipeImages()
                refreshPosts()
                restoreSessionIfAvailable()
            } catch (ex: Exception) {
                errorMessage.value = "Error inicializando datos: ${ex.message ?: "desconocido"}"
            }
        }
    }

    private fun restoreSessionIfAvailable() {
        val savedUserId = sessionPrefs.getInt(KEY_LOGGED_USER_ID, -1)
        if (savedUserId <= 0) return

        val user = usuarios.firstOrNull { it.idUser == savedUserId }
        if (user == null) {
            clearSession()
            return
        }

        currentUserId.value = user.idUser
        selectedUserId.value = user.idUser
        if (currentScreen.value == AppDestination.Welcome) {
            currentScreen.value = AppDestination.Home
        }
    }

    private fun saveSession(userId: Int) {
        sessionPrefs.edit().putInt(KEY_LOGGED_USER_ID, userId).apply()
    }

    private fun clearSession() {
        sessionPrefs.edit().remove(KEY_LOGGED_USER_ID).apply()
    }

    private suspend fun normalizeSeedRecipeImages() {
        val desiredByTitle = mapOf(
            "Ensalada tibia de quinoa" to "stock_salad",
            "Pan de masa madre" to "stock_bread",
            "Tarta de limon" to "stock_tart",
            "Pasta con pesto casero" to "stock_pasta",
            "Tacos de pollo crujiente" to "stock_tacos",
            "Sopa cremosa de calabaza" to "stock_soup",
            "Salmon al horno con verduras" to "stock_salmon",
            "Bowl de frutas y yogurt" to "stock_bowl",
        )

        repository.obtenerTodosLosPosts().forEach { post ->
            val desired = desiredByTitle[post.title] ?: return@forEach
            if (post.image != desired) {
                repository.actualizarPost(post.copy(image = desired))
            }
        }
    }

    private suspend fun refreshUsers() {
        usuarios.clear()
        usuarios.addAll(repository.obtenerTodosLosUsuarios())
    }

    private suspend fun refreshPosts() {
        val byId = usuarios.associateBy { it.idUser }
        posts.clear()
        posts.addAll(
            repository.obtenerTodosLosPosts().mapNotNull { post ->
                val author = byId[post.idUser] ?: return@mapNotNull null
                PostWithAuthor(post, author)
            }
        )
    }

    private fun seedUsers(): List<Usuario> {
        return listOf(
            Usuario(name = "Valeria Chef", email = "valeria@demo.com", password = "123456", descrip = "Recetas faciles y saludables", image = "img_ex"),
            Usuario(name = "Martin Pan", email = "martin@demo.com", password = "123456", descrip = "Panaderia artesanal", image = "img2_ex"),
            Usuario(name = "Sofia Dulce", email = "sofia@demo.com", password = "123456", descrip = "Postres caseros", image = "img_ex"),
        )
    }

    private fun seedRecipes(users: List<Usuario>): List<Post> {
        if (users.isEmpty()) return emptyList()

        val a = users[0].idUser
        val b = users.getOrNull(1)?.idUser ?: a
        val c = users.getOrNull(2)?.idUser ?: a

        return listOf(
            Post(idUser = a, title = "Ensalada tibia de quinoa", description = "Lista en 20 minutos con verduras salteadas.", category = "Saludable", image = "stock_salad"),
            Post(idUser = b, title = "Pan de masa madre", description = "Fermentacion lenta con corteza crujiente.", category = "Panaderia", image = "stock_bread"),
            Post(idUser = c, title = "Tarta de limon", description = "Base crocante y crema citrica equilibrada.", category = "Postres", image = "stock_tart"),
            Post(idUser = a, title = "Pasta con pesto casero", description = "Salsa fresca con albahaca y nueces.", category = "Almuerzo", image = "stock_pasta"),
            Post(idUser = b, title = "Tacos de pollo crujiente", description = "Tortillas calientes, salsa fresca y pollo especiado.", category = "Cenas", image = "stock_tacos"),
            Post(idUser = c, title = "Sopa cremosa de calabaza", description = "Suave, reconfortante y con semillas tostadas.", category = "Sopas", image = "stock_soup"),
            Post(idUser = a, title = "Salmon al horno con verduras", description = "Plato completo con glaseado de limon y eneldo.", category = "Cena ligera", image = "stock_salmon"),
            Post(idUser = b, title = "Bowl de frutas y yogurt", description = "Desayuno fresco con granola y miel.", category = "Desayuno", image = "stock_bowl"),
        )
    }

    fun goToOnboarding1() {
        currentScreen.value = AppDestination.Onboarding1
    }

    fun goToOnboarding2() {
        currentScreen.value = AppDestination.Onboarding2
    }

    fun goToLogin() {
        currentScreen.value = AppDestination.Login
    }

    fun goToSignUp() {
        currentScreen.value = AppDestination.SignUp
    }

    fun goToForgotPassword() {
        currentScreen.value = AppDestination.ForgotPassword
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                errorMessage.value = "Completa correo y contrasena"
                return@launch
            }

            val user = repository.obtenerUsuarioPorEmail(email)
            if (user == null || user.password != password) {
                errorMessage.value = "Credenciales incorrectas"
                return@launch
            }

            currentUserId.value = user.idUser
            selectedUserId.value = user.idUser
            currentScreen.value = AppDestination.Home
            saveSession(user.idUser)
        }
    }

    fun register(user: Usuario) {
        viewModelScope.launch {
            val exists = repository.obtenerUsuarioPorEmail(user.email)
            if (exists != null) {
                errorMessage.value = "Este correo ya existe"
                return@launch
            }

            val id = (usuarios.maxOfOrNull { it.idUser } ?: 0) + 1
            val created = user.copy(idUser = id)
            repository.insertarUsuario(created)
            refreshUsers()

            currentUserId.value = created.idUser
            selectedUserId.value = created.idUser
            currentScreen.value = AppDestination.Home
            saveSession(created.idUser)
        }
    }

    fun goHome() {
        currentScreen.value = AppDestination.Home
    }

    fun goDiscover() {
        currentScreen.value = AppDestination.Discover
    }

    fun goNotifications() {
        selectedNotificationId.value = null
        currentScreen.value = AppDestination.Notifications
    }

    fun openNotification(notificationId: Int) {
        selectedNotificationId.value = notificationId
        currentScreen.value = AppDestination.NotificationDetail
    }

    fun goSaved() {
        currentScreen.value = AppDestination.Saved
    }

    fun openPost(postId: PostId) {
        selectedPostId.value = postId
        currentScreen.value = AppDestination.RecipeDetail
    }

    fun openCurrentProfile() {
        selectedUserId.value = currentUserId.value
        currentScreen.value = AppDestination.MyProfile
    }

    fun openUserProfile(userId: UserId) {
        selectedUserId.value = userId
        currentScreen.value = if (userId == currentUserId.value) AppDestination.MyProfile else AppDestination.UserProfile
    }

    fun goCreateRecipe() {
        selectedPostId.value = null
        recipeFormToken.value += 1
        currentScreen.value = AppDestination.CreateRecipe
    }

    fun goEditRecipe(postId: PostId) {
        selectedPostId.value = postId
        recipeFormToken.value += 1
        currentScreen.value = AppDestination.EditRecipe
    }

    fun createRecipe(title: String, description: String, category: String, image: String) {
        viewModelScope.launch {
            val userId = currentUserId.value
            if (userId == null) {
                errorMessage.value = "Inicia sesion para publicar"
                return@launch
            }

            if (title.isBlank() || description.isBlank()) {
                errorMessage.value = "Completa titulo y descripcion"
                return@launch
            }

            val id = (posts.maxOfOrNull { it.post.idPost } ?: 0) + 1
            val post = Post(
                idPost = id,
                idUser = userId,
                title = title.trim(),
                description = description.trim(),
                category = category.ifBlank { "General" },
                image = image.ifBlank { "img_ex" },
            )

            repository.insertarPost(post)
            refreshPosts()
            openPost(post.idPost)
        }
    }

    fun updateRecipe(title: String, description: String, category: String, image: String) {
        viewModelScope.launch {
            val postId = selectedPostId.value ?: return@launch
            val current = repository.obtenerPostPorId(postId) ?: return@launch
            val updated = current.copy(
                title = title.trim(),
                description = description.trim(),
                category = category.ifBlank { current.category },
                image = image.ifBlank { current.image }
            )
            repository.actualizarPost(updated)
            refreshPosts()
            openPost(postId)
        }
    }

    fun deleteSelectedRecipe() {
        viewModelScope.launch {
            val postId = selectedPostId.value ?: return@launch
            val post = repository.obtenerPostPorId(postId) ?: return@launch
            if (post.idUser != currentUserId.value) {
                errorMessage.value = "Solo puedes eliminar tus recetas"
                return@launch
            }

            repository.eliminarPost(post)
            savedPostIds.remove(postId)
            refreshPosts()
            currentScreen.value = AppDestination.MyProfile
        }
    }

    fun toggleSaved(postId: PostId) {
        if (savedPostIds.contains(postId)) {
            savedPostIds.remove(postId)
        } else {
            savedPostIds.add(postId)
        }
    }

    fun updateCurrentUser(name: String, bio: String) {
        viewModelScope.launch {
            val id = currentUserId.value ?: return@launch
            val current = usuarios.firstOrNull { it.idUser == id } ?: return@launch
            val updated = current.copy(name = name.trim(), descrip = bio.trim())
            repository.actualizarUsuario(updated)
            refreshUsers()
            refreshPosts()
            selectedUserId.value = id
            currentScreen.value = AppDestination.MyProfile
        }
    }

    fun logout() {
        currentUserId.value = null
        selectedUserId.value = null
        selectedPostId.value = null
        selectedNotificationId.value = null
        currentScreen.value = AppDestination.Welcome
        clearSession()
    }

    fun recoverPassword(email: String) {
        errorMessage.value = if (email.isBlank()) {
            "Ingresa un correo"
        } else {
            "Si el correo existe, te enviamos instrucciones"
        }
        currentScreen.value = AppDestination.Login
    }

    fun back() {
        currentScreen.value = when (currentScreen.value) {
            AppDestination.Onboarding1 -> AppDestination.Welcome
            AppDestination.Onboarding2 -> AppDestination.Onboarding1
            AppDestination.Login -> AppDestination.Welcome
            AppDestination.SignUp -> AppDestination.Login
            AppDestination.ForgotPassword -> AppDestination.Login
            AppDestination.Discover -> AppDestination.Home
            AppDestination.Notifications -> AppDestination.Home
            AppDestination.NotificationDetail -> AppDestination.Notifications
            AppDestination.Saved -> AppDestination.Home
            AppDestination.RecipeDetail -> AppDestination.Home
            AppDestination.UserProfile -> AppDestination.Discover
            AppDestination.MyProfile -> AppDestination.Home
            AppDestination.CreateRecipe -> AppDestination.MyProfile
            AppDestination.EditRecipe -> AppDestination.RecipeDetail
            else -> AppDestination.Home
        }
    }

    fun dismissError() {
        errorMessage.value = null
    }
}