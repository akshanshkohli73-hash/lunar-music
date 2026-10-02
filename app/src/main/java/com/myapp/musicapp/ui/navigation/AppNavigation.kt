package com.myapp.musicapp.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.myapp.musicapp.player.PlayerConnection
import com.myapp.musicapp.ui.screens.album.AlbumDetailScreen
import com.myapp.musicapp.ui.screens.artist.ArtistDetailScreen
import com.myapp.musicapp.ui.screens.home.HomeScreen
import com.myapp.musicapp.ui.screens.library.LibraryScreen
import com.myapp.musicapp.ui.screens.playlist.PlaylistDetailScreen
import com.myapp.musicapp.ui.screens.search.SearchScreen
import com.myapp.musicapp.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    playerConnection: PlayerConnection
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.path,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
            slideInHorizontally(
                initialOffsetX = { it / 4 },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) +
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) +
            slideOutHorizontally(
                targetOffsetX = { it / 4 },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        }
    ) {
        composable(Route.Home.path) { HomeScreen(navController, playerConnection) }
        composable(Route.Search.path) { SearchScreen(navController, playerConnection) }
        composable(Route.Library.path) { LibraryScreen(navController, playerConnection) }
        composable(Route.Settings.path) { SettingsScreen(navController) }
        composable(
            route = "album/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            AlbumDetailScreen(
                albumId = backStackEntry.arguments?.getString("id") ?: "",
                navController = navController,
                playerConnection = playerConnection
            )
        }
        composable(
            route = "artist/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            ArtistDetailScreen(
                artistId = backStackEntry.arguments?.getString("id") ?: "",
                navController = navController,
                playerConnection = playerConnection
            )
        }
        composable(
            route = "playlist/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            PlaylistDetailScreen(
                playlistId = backStackEntry.arguments?.getString("id") ?: "",
                navController = navController,
                playerConnection = playerConnection
            )
        }
    }
}
