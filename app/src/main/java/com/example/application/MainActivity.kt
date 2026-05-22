package com.example.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            Scheduler::class.java,
            "recetas_social_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    private val repository by lazy {
        UserRepository(database.userDao(), database.postDao())
    }

    private val viewModel: UserViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UserViewModel(repository, applicationContext) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        posts = viewModel.posts,
        savedPostIds = viewModel.savedPostIds,
        destination = viewModel.currentScreen.value,
        selectedNotificationId = viewModel.selectedNotificationId.value,
        selectedUserId = viewModel.selectedUserId.value,
        selectedPostId = viewModel.selectedPostId.value,
        currentUserId = viewModel.currentUserId.value,
        recipeFormToken = viewModel.recipeFormToken.value,
        errorMessage = viewModel.errorMessage.value,
        onGoToOnboarding1 = { viewModel.goToOnboarding1() },
        onGoToOnboarding2 = { viewModel.goToOnboarding2() },
        onGoToLogin = { viewModel.goToLogin() },
        onGoToSignUp = { viewModel.goToSignUp() },
        onGoToForgotPassword = { viewModel.goToForgotPassword() },
        onLogin = { email, password -> viewModel.login(email, password) },
        onSignUp = { viewModel.register(it) },
        onRecoverPassword = { viewModel.recoverPassword(it) },
        onGoHome = { viewModel.goHome() },
        onGoDiscover = { viewModel.goDiscover() },
        onGoNotifications = { viewModel.goNotifications() },
        onGoSaved = { viewModel.goSaved() },
        onOpenRecipe = { viewModel.openPost(it) },
        onOpenProfile = { viewModel.openCurrentProfile() },
        onOpenUserProfile = { viewModel.openUserProfile(it) },
        onOpenNotification = { viewModel.openNotification(it) },
        onGoCreateRecipe = { viewModel.goCreateRecipe() },
        onGoEditRecipe = { viewModel.goEditRecipe(it) },
        onCreateRecipe = { title, description, category, image ->
            viewModel.createRecipe(title, description, category, image)
        },
        onUpdateRecipe = { title, description, category, image ->
            viewModel.updateRecipe(title, description, category, image)
        },
        onDeleteRecipe = { viewModel.deleteSelectedRecipe() },
        onToggleSaved = { viewModel.toggleSaved(it) },
        onUpdateCurrentUser = { name, bio -> viewModel.updateCurrentUser(name, bio) },
        onBack = { viewModel.back() },
        onLogout = { viewModel.logout() },
        onDismissError = { viewModel.dismissError() },
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    AppTopicosTheme {
        AppRootScreen(
            users = listOf(),
            posts = listOf(),
            savedPostIds = listOf(),
            destination = AppDestination.Welcome,
            selectedNotificationId = null,
            selectedUserId = null,
            selectedPostId = null,
            currentUserId = null,
            recipeFormToken = 0,
            errorMessage = null,
            onGoToOnboarding1 = {},
            onGoToOnboarding2 = {},
            onGoToLogin = {},
            onGoToSignUp = {},
            onGoToForgotPassword = {},
            onLogin = { _, _ -> },
            onSignUp = {},
            onRecoverPassword = {},
            onGoHome = {},
            onGoDiscover = {},
            onGoNotifications = {},
            onGoSaved = {},
            onOpenRecipe = {},
            onOpenProfile = {},
            onOpenUserProfile = {},
            onOpenNotification = {},
            onGoCreateRecipe = {},
            onGoEditRecipe = {},
            onCreateRecipe = { _, _, _, _ -> },
            onUpdateRecipe = { _, _, _, _ -> },
            onDeleteRecipe = {},
            onToggleSaved = {},
            onUpdateCurrentUser = { _, _ -> },
            onBack = {},
            onLogout = {},
            onDismissError = {},
        )
    }
}
