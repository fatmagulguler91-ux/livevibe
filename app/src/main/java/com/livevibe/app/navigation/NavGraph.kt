package com.livevibe.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.livevibe.app.data.model.User
import com.livevibe.app.ui.auth.AuthScreen
import com.livevibe.app.ui.profile.ProfileScreen

object Routes {
    const val AUTH = "auth"
    const val PROFILE = "profile"
}

@Composable
fun LiveVibeNavGraph(
    navController: NavHostController = rememberNavController()
) {
    // Faz 1'de tek kullanıcı state'i burada tutuluyor; kullanıcı sayısı/ekran
    // sayısı arttıkça bu bir shared ViewModel'e (örn. SessionViewModel) taşınmalı.
    var currentUser by remember { mutableStateOf<User?>(null) }

    NavHost(navController = navController, startDestination = Routes.AUTH) {
        composable(Routes.AUTH) {
            AuthScreen(
                onSignedIn = { user ->
                    currentUser = user
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.PROFILE) {
            currentUser?.let { user ->
                ProfileScreen(user = user)
            }
        }
    }
}
