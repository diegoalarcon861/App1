package com.example.application.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.net.Uri
import com.example.application.R
import com.example.application.data.Usuario
import kotlinx.coroutines.launch

typealias UserId = Int

enum class AppDestination {
    Welcome,
    Onboarding1,
    Onboarding2,
    Login,
    SignUp,
    ForgotPassword,
    Home,
    Discover,
    RecipeDetail,
    CreateRecipe,
    EditRecipe,
    MyProfile,
    UserProfile,
    Notifications,
    NotificationDetail,
    Saved,
}

data class AppNotification(
    val id: Int,
    val type: String,
    val title: String,
    val message: String,
    val timeLabel: String,
    val recipeId: PostId?,
)

private fun buildNotifications(posts: List<PostWithAuthor>): List<AppNotification> {
    val first = posts.getOrNull(0)
    val second = posts.getOrNull(1)
    val third = posts.getOrNull(2)

    return listOf(
        AppNotification(
            id = 1,
            type = "Comentario",
            title = "Nuevo comentario",
            message = "${first?.author?.name ?: "Una cocinera"} comento en ${first?.post?.title ?: "tu receta"}: \"Se ve increible\".",
            timeLabel = "hace 5 min",
            recipeId = first?.post?.idPost,
        ),
        AppNotification(
            id = 2,
            type = "Publicacion",
            title = "Nueva receta publicada",
            message = "${second?.author?.name ?: "Martin"} publico ${second?.post?.title ?: "una receta nueva"}.",
            timeLabel = "hace 22 min",
            recipeId = second?.post?.idPost,
        ),
        AppNotification(
            id = 3,
            type = "Guardado",
            title = "Guardaron tu receta",
            message = "${third?.author?.name ?: "Sofia"} guardo ${third?.post?.title ?: "una de tus recetas"} para cocinarla despues.",
            timeLabel = "hace 1 h",
            recipeId = third?.post?.idPost,
        ),
        AppNotification(
            id = 4,
            type = "Tendencia",
            title = "Receta en tendencia",
            message = "${first?.post?.title ?: "Tu ultima receta"} esta recibiendo muchas visitas hoy.",
            timeLabel = "hace 3 h",
            recipeId = first?.post?.idPost,
        ),
        AppNotification(
            id = 5,
            type = "Recordatorio",
            title = "Hora de cocinar",
            message = "Tienes ingredientes guardados para preparar ${second?.post?.title ?: "tu receta favorita"}.",
            timeLabel = "ayer",
            recipeId = second?.post?.idPost,
        )
    )
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
    posts: List<PostWithAuthor>,
    savedPostIds: List<PostId>,
    destination: AppDestination,
    selectedNotificationId: Int?,
    selectedUserId: UserId?,
    selectedPostId: PostId?,
    currentUserId: UserId?,
    recipeFormToken: Int,
    errorMessage: String?,
    onGoToOnboarding1: () -> Unit,
    onGoToOnboarding2: () -> Unit,
    onGoToLogin: () -> Unit,
    onGoToSignUp: () -> Unit,
    onGoToForgotPassword: () -> Unit,
    onLogin: (String, String) -> Unit,
    onSignUp: (Usuario) -> Unit,
    onRecoverPassword: (String) -> Unit,
    onGoHome: () -> Unit,
    onGoDiscover: () -> Unit,
    onGoNotifications: () -> Unit,
    onGoSaved: () -> Unit,
    onOpenRecipe: (PostId) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenUserProfile: (UserId) -> Unit,
    onOpenNotification: (Int) -> Unit,
    onGoCreateRecipe: () -> Unit,
    onGoEditRecipe: (PostId) -> Unit,
    onCreateRecipe: (String, String, String, String) -> Unit,
    onUpdateRecipe: (String, String, String, String) -> Unit,
    onDeleteRecipe: () -> Unit,
    onToggleSaved: (PostId) -> Unit,
    onUpdateCurrentUser: (String, String) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onDismissError: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentUser = remember(users, currentUserId) { currentUserId?.let { id -> users.firstOrNull { it.idUser == id } } }
    val selectedUser = remember(users, selectedUserId) { selectedUserId?.let { id -> users.firstOrNull { it.idUser == id } } }
    val selectedRecipe = remember(posts, selectedPostId) { selectedPostId?.let { id -> posts.firstOrNull { it.post.idPost == id } } }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(errorMessage)
            onDismissError()
        }
    }

    val topBarVisible = destination !in setOf(
        AppDestination.Welcome,
        AppDestination.Onboarding1,
        AppDestination.Onboarding2,
        AppDestination.Login,
        AppDestination.SignUp,
        AppDestination.ForgotPassword,
    )

    ModalNavigationDrawer(
        gesturesEnabled = topBarVisible,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(label = { Text("Inicio") }, selected = destination == AppDestination.Home, onClick = { scope.launch { drawerState.close() }; onGoHome() })
                NavigationDrawerItem(label = { Text("Descubrir") }, selected = destination == AppDestination.Discover, onClick = { scope.launch { drawerState.close() }; onGoDiscover() })
                NavigationDrawerItem(label = { Text("Recetas guardadas") }, selected = destination == AppDestination.Saved, onClick = { scope.launch { drawerState.close() }; onGoSaved() })
                NavigationDrawerItem(label = { Text("Notificaciones") }, selected = destination == AppDestination.Notifications, onClick = { scope.launch { drawerState.close() }; onGoNotifications() })
                NavigationDrawerItem(label = { Text("Mi perfil") }, selected = destination == AppDestination.MyProfile, onClick = { scope.launch { drawerState.close() }; onOpenProfile() })
                NavigationDrawerItem(label = { Text("Nueva receta") }, selected = destination == AppDestination.CreateRecipe, onClick = { scope.launch { drawerState.close() }; onGoCreateRecipe() })
                if (currentUser != null) {
                    NavigationDrawerItem(label = { Text("Salir") }, selected = false, onClick = { scope.launch { drawerState.close() }; onLogout() })
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (topBarVisible) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (destination) {
                                    AppDestination.Home -> "Inicio"
                                    AppDestination.Discover -> "Descubrir"
                                    AppDestination.RecipeDetail -> "Receta"
                                    AppDestination.CreateRecipe -> "Nueva receta"
                                    AppDestination.EditRecipe -> "Editar receta"
                                    AppDestination.MyProfile -> "Mi perfil"
                                    AppDestination.UserProfile -> "Perfil"
                                    AppDestination.Notifications -> "Notificaciones"
                                    AppDestination.NotificationDetail -> "Detalle"
                                    AppDestination.Saved -> "Guardadas"
                                    else -> "Recetas"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                                    contentDescription = "Abrir menu",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        actions = {
                            if (destination != AppDestination.Home) {
                                TextButton(onClick = onBack) {
                                    Text("Volver", color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                            if (currentUser != null) {
                                TextButton(onClick = onOpenProfile) {
                                    Text("Perfil", color = MaterialTheme.colorScheme.onPrimary)
                                }
                                TextButton(onClick = onLogout) {
                                    Text("Salir", color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (destination) {
                        AppDestination.Welcome -> WelcomeScreen(onGoToOnboarding1, onGoToLogin)
                        AppDestination.Onboarding1 -> OnboardingScreen(
                            title = "Comparte tus recetas",
                            subtitle = "Publica paso a paso y conecta con personas que aman cocinar.",
                            onNext = onGoToOnboarding2,
                            onSkip = onGoToLogin
                        )

                        AppDestination.Onboarding2 -> OnboardingScreen(
                            title = "Descubre nuevos sabores",
                            subtitle = "Explora perfiles, guarda recetas y construye tu recetario social.",
                            onNext = onGoToSignUp,
                            onSkip = onGoToLogin
                        )

                        AppDestination.Login -> LoginScreen(onLogin, onGoToSignUp, onGoToForgotPassword)
                        AppDestination.SignUp -> SignUpScreen(onSignUp, onGoToLogin)
                        AppDestination.ForgotPassword -> ForgotPasswordScreen(onRecoverPassword, onGoToLogin)

                        AppDestination.Home -> HomeScreen(
                            currentUser = currentUser,
                            recipes = posts,
                            savedPostIds = savedPostIds,
                            onOpenRecipe = onOpenRecipe,
                            onToggleSaved = onToggleSaved,
                            onGoDiscover = onGoDiscover,
                            onGoNotifications = onGoNotifications,
                            onGoSaved = onGoSaved,
                            onGoCreateRecipe = onGoCreateRecipe
                        )

                        AppDestination.Discover -> DiscoverScreen(
                            recipes = posts,
                            onOpenRecipe = onOpenRecipe,
                            onOpenUser = { onOpenUserProfile(it) }
                        )

                        AppDestination.RecipeDetail -> RecipeDetailScreen(
                            recipe = selectedRecipe,
                            isSaved = selectedRecipe?.post?.idPost?.let { savedPostIds.contains(it) } == true,
                            isAuthor = selectedRecipe?.author?.idUser == currentUserId,
                            onToggleSaved = {
                                selectedRecipe?.post?.idPost?.let(onToggleSaved)
                            },
                            onOpenAuthor = {
                                selectedRecipe?.author?.idUser?.let(onOpenUserProfile)
                            },
                            onEdit = {
                                selectedRecipe?.post?.idPost?.let(onGoEditRecipe)
                            },
                            onDelete = onDeleteRecipe
                        )

                        AppDestination.CreateRecipe -> RecipeFormScreen(
                            title = "Publicar receta",
                            initial = selectedRecipe,
                            isEditing = false,
                            formSessionToken = recipeFormToken,
                            onSubmit = onCreateRecipe
                        )

                        AppDestination.EditRecipe -> RecipeFormScreen(
                            title = "Actualizar receta",
                            initial = selectedRecipe,
                            isEditing = true,
                            formSessionToken = recipeFormToken,
                            onSubmit = onUpdateRecipe
                        )

                        AppDestination.MyProfile -> MyProfileScreen(
                            user = currentUser,
                            recipes = posts.filter { it.author.idUser == currentUserId },
                            onOpenRecipe = onOpenRecipe,
                            onUpdateProfile = onUpdateCurrentUser,
                            onGoCreateRecipe = onGoCreateRecipe
                        )

                        AppDestination.UserProfile -> UserProfileScreen(
                            user = selectedUser,
                            recipes = posts.filter { it.author.idUser == selectedUser?.idUser },
                            onOpenRecipe = onOpenRecipe
                        )

                        AppDestination.Notifications -> NotificationsScreen(
                            notifications = buildNotifications(posts),
                            onOpenNotification = onOpenNotification
                        )
                        AppDestination.NotificationDetail -> NotificationDetailScreen(
                            notification = buildNotifications(posts).firstOrNull { it.id == selectedNotificationId },
                            onOpenRecipe = onOpenRecipe
                        )
                        AppDestination.Saved -> SavedScreen(
                            recipes = posts.filter { savedPostIds.contains(it.post.idPost) },
                            onOpenRecipe = onOpenRecipe
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeScreen(
    onStart: () -> Unit,
    onLogin: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF1A1A1A), Color(0xFF0E0E0E))))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = R.drawable.taza_cafe,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(230.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Recetas para compartir",
                    color = Color(0xFFF8F8F8),
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Convierte tus platos en historias y conecta con una comunidad foodie.",
                    color = Color(0xFFBEB7AD),
                    textAlign = TextAlign.Center
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(28.dp)
                ) { Text("Empezar") }
                OutlinedButton(
                    onClick = onLogin,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(28.dp)
                ) { Text("Ya tengo cuenta") }
            }
        }
    }
}

@Composable
private fun OnboardingScreen(
    title: String,
    subtitle: String,
    onNext: () -> Unit,
    onSkip: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(subtitle)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) { Text("Continuar") }
            OutlinedButton(onClick = onSkip, modifier = Modifier.fillMaxWidth()) { Text("Saltar") }
        }
    }
}

@Composable
private fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onOpenSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryActionButton(onClick = { onLogin(email.trim(), password.trim()) }, enabled = email.isNotBlank() && password.isNotBlank(), text = "Entrar")
        SecondaryActionButton(onClick = onForgotPassword, text = "Olvidé mi contraseña")
        SecondaryActionButton(onClick = onOpenSignUp, text = "Crear cuenta")
    }
}

@Composable
private fun SignUpScreen(
    onSignUp: (Usuario) -> Unit,
    onOpenLogin: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var bio by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(bio, { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                onSignUp(
                    Usuario(
                        name = name.trim(),
                        email = email.trim(),
                        password = password.trim(),
                        descrip = bio.trim(),
                        image = "img_ex"
                    )
                )
            },
            enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear cuenta") }

        SecondaryActionButton(onClick = onOpenLogin, text = "Ya tengo cuenta")
    }
}

@Composable
private fun ForgotPasswordScreen(
    onRecover: (String) -> Unit,
    onBackToLogin: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Recuperar cuenta", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        PrimaryActionButton(onClick = { onRecover(email.trim()) }, text = "Enviar")
        SecondaryActionButton(onClick = onBackToLogin, text = "Volver a iniciar sesión")
    }
}

@Composable
private fun HomeScreen(
    currentUser: Usuario?,
    recipes: List<PostWithAuthor>,
    savedPostIds: List<PostId>,
    onOpenRecipe: (PostId) -> Unit,
    onToggleSaved: (PostId) -> Unit,
    onGoDiscover: () -> Unit,
    onGoNotifications: () -> Unit,
    onGoSaved: () -> Unit,
    onGoCreateRecipe: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Hola, ${currentUser?.name ?: "chef"}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryActionButton(onClick = onGoDiscover, text = "Descubrir", modifier = Modifier.weight(1f))
            SecondaryActionButton(onClick = onGoNotifications, text = "Avisos", modifier = Modifier.weight(1f))
            SecondaryActionButton(onClick = onGoSaved, text = "Guardadas", modifier = Modifier.weight(1f))
        }
        PrimaryActionButton(onClick = onGoCreateRecipe, text = "Publicar receta")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(recipes, key = { it.post.idPost }) { item ->
                RecipeCard(
                    recipe = item,
                    saved = savedPostIds.contains(item.post.idPost),
                    onOpen = { onOpenRecipe(item.post.idPost) },
                    onSave = { onToggleSaved(item.post.idPost) }
                )
            }
        }
    }
}

@Composable
private fun DiscoverScreen(
    recipes: List<PostWithAuthor>,
    onOpenRecipe: (PostId) -> Unit,
    onOpenUser: (UserId) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(recipes, query) {
        if (query.isBlank()) recipes
        else recipes.filter {
            it.post.title.contains(query, true) || it.post.category.contains(query, true) || it.author.name.contains(query, true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(query, { query = it }, label = { Text("Buscar recetas") }, modifier = Modifier.fillMaxWidth())
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered, key = { it.post.idPost }) { item ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onOpenRecipe(item.post.idPost) }) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AuthorAvatar(item.author.name)
                        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                            Text(item.post.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(item.post.category, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        OutlinedButton(onClick = { onOpenUser(item.author.idUser) }) {
                            Text("Perfil", maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeDetailScreen(
    recipe: PostWithAuthor?,
    isSaved: Boolean,
    isAuthor: Boolean,
    onToggleSaved: () -> Unit,
    onOpenAuthor: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    if (recipe == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada")
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        RecipeImage(
            image = recipe.post.image,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(recipe.post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Categoria: ${recipe.post.category}", color = MaterialTheme.colorScheme.primary)
            Text(recipe.post.description)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onOpenAuthor, modifier = Modifier.weight(1f)) { Text("Ver autor") }
                SecondaryActionButton(onClick = onToggleSaved, text = if (isSaved) "Guardada" else "Guardar", modifier = Modifier.weight(1f))
            }
            if (isAuthor) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PrimaryActionButton(onClick = onEdit, text = "Editar", modifier = Modifier.weight(1f))
                    SecondaryActionButton(onClick = onDelete, text = "Eliminar", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RecipeFormScreen(
    title: String,
    initial: PostWithAuthor?,
    isEditing: Boolean,
    formSessionToken: Int,
    onSubmit: (String, String, String, String) -> Unit,
) {
    var recipeTitle by rememberSaveable(formSessionToken) { mutableStateOf(initial?.post?.title.orEmpty()) }
    var description by rememberSaveable(formSessionToken) { mutableStateOf(initial?.post?.description.orEmpty()) }
    var category by rememberSaveable(formSessionToken) { mutableStateOf(initial?.post?.category.orEmpty()) }
    var image by rememberSaveable(formSessionToken) { mutableStateOf(initial?.post?.image.orEmpty()) }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: SecurityException) {
            }
            image = uri.toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(recipeTitle, { recipeTitle = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(category, { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            SecondaryActionButton(onClick = { imagePicker.launch(arrayOf("image/*")) }, text = "Elegir del dispositivo", modifier = Modifier.weight(1f))
            SecondaryActionButton(onClick = { image = "" }, text = "Quitar imagen", modifier = Modifier.weight(1f))
        }
        if (image.isNotBlank()) {
            RecipeImage(
                image = image,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        PrimaryActionButton(onClick = { onSubmit(recipeTitle, description, category, image) }, enabled = recipeTitle.isNotBlank() && description.isNotBlank(), text = if (isEditing) "Actualizar" else "Publicar")
    }
}

@Composable
private fun MyProfileScreen(
    user: Usuario?,
    recipes: List<PostWithAuthor>,
    onOpenRecipe: (PostId) -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onGoCreateRecipe: () -> Unit,
) {
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Usuario no encontrado")
        }
        return
    }

    var editing by rememberSaveable { mutableStateOf(false) }
    var name by rememberSaveable(user.idUser) { mutableStateOf(user.name) }
    var bio by rememberSaveable(user.idUser) { mutableStateOf(user.descrip) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuthorAvatar(user.name)
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(user.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(user.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            if (editing) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(bio, { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    PrimaryActionButton(onClick = { onUpdateProfile(name, bio); editing = false }, text = "Guardar", modifier = Modifier.weight(1f))
                    SecondaryActionButton(onClick = { editing = false }, text = "Cancelar", modifier = Modifier.weight(1f))
                }
            } else {
                Text(user.descrip.ifBlank { "Sin bio" })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    PrimaryActionButton(onClick = { editing = true }, text = "Editar perfil", modifier = Modifier.weight(1f))
                    SecondaryActionButton(onClick = onGoCreateRecipe, text = "Nueva receta", modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Text("Mis recetas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        items(recipes, key = { it.post.idPost }) {
            RecipeCard(recipe = it, saved = false, onOpen = { onOpenRecipe(it.post.idPost) }, onSave = {})
        }
    }
}

@Composable
private fun UserProfileScreen(
    user: Usuario?,
    recipes: List<PostWithAuthor>,
    onOpenRecipe: (PostId) -> Unit,
) {
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Perfil no encontrado")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuthorAvatar(user.name)
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(user.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(user.descrip)
                }
            }
        }
        items(recipes, key = { it.post.idPost }) {
            RecipeCard(recipe = it, saved = false, onOpen = { onOpenRecipe(it.post.idPost) }, onSave = {})
        }
    }
}

@Composable
private fun NotificationsScreen(
    notifications: List<AppNotification>,
    onOpenNotification: (Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(notifications, key = { it.id }) { notification ->
            Card(modifier = Modifier.fillMaxWidth().clickable { onOpenNotification(notification.id) }) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(notification.type, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Text(notification.title, fontWeight = FontWeight.Bold)
                    Text(notification.message, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(notification.timeLabel, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun NotificationDetailScreen(
    notification: AppNotification?,
    onOpenRecipe: (PostId) -> Unit,
) {
    if (notification == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Notificacion no encontrada")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(notification.type, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        Text(notification.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(notification.message, style = MaterialTheme.typography.bodyLarge)
        Text("Recibida ${notification.timeLabel}", color = MaterialTheme.colorScheme.onSurfaceVariant)

        if (notification.recipeId != null) {
            PrimaryActionButton(
                onClick = { onOpenRecipe(notification.recipeId) },
                text = "Abrir receta relacionada"
            )
        }
    }
}

@Composable
private fun SavedScreen(
    recipes: List<PostWithAuthor>,
    onOpenRecipe: (PostId) -> Unit,
) {
    if (recipes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes recetas guardadas")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(recipes, key = { it.post.idPost }) {
            RecipeCard(recipe = it, saved = true, onOpen = { onOpenRecipe(it.post.idPost) }, onSave = {})
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: PostWithAuthor,
    saved: Boolean,
    onOpen: () -> Unit,
    onSave: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            RecipeImage(
                image = recipe.post.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(recipe.post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(recipe.post.description, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("${recipe.author.name} - ${recipe.post.category}", color = MaterialTheme.colorScheme.primary)
                    OutlinedButton(onClick = onSave, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)) {
                        Text(if (saved) "Guardada" else "Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthorAvatar(name: String) {
    val initials = remember(name) {
        name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "?" }
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
    }
}

private fun resolveImage(image: String): Int {
    return when (image) {
        "img2_ex" -> R.drawable.img2_ex
        "stock_salad" -> R.drawable.stock_salad
        "stock_bread" -> R.drawable.stock_bread
        "stock_tart" -> R.drawable.stock_tart
        "stock_pasta" -> R.drawable.stock_pasta
        "stock_tacos" -> R.drawable.stock_tacos
        "stock_soup" -> R.drawable.stock_soup
        "stock_salmon" -> R.drawable.stock_salmon
        "stock_bowl" -> R.drawable.stock_bowl
        else -> R.drawable.img_ex
    }
}

@Composable
private fun PrimaryActionButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text)
    }
}

@Composable
private fun SecondaryActionButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text)
    }
}

@Composable
private fun RecipeImage(
    image: String,
    modifier: Modifier = Modifier,
) {
    val model: Any = when {
        image.isBlank() -> R.drawable.img_ex
        image.startsWith("content://") || image.startsWith("file://") || image.startsWith("android.resource://") -> Uri.parse(image)
        image.startsWith("stock_") || image == "img_ex" || image == "img2_ex" -> resolveImage(image)
        else -> resolveImage(image)
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = null,
        error = null,
        fallback = null
    )
}
