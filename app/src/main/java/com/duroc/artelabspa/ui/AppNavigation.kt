package com.duroc.artelabspa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duroc.artelabspa.ui.screens.FormScreen
import com.duroc.artelabspa.ui.screens.HomeScreen
import com.duroc.artelabspa.viewmodel.FormViewModel
import com.duroc.artelabspa.viewmodel.FormViewModelFactory
import com.duroc.artelabspa.viewmodel.HomeViewModel
import com.duroc.artelabspa.viewmodel.HomeViewModelFactory

@Composable
fun AppNavigation(
    homeViewModelFactory: HomeViewModelFactory,
    formViewModelFactory: FormViewModelFactory
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }

        composable(
            route = NavRoutes.ADD_PRODUCT_ROUTE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val formViewModel: FormViewModel = viewModel(factory = formViewModelFactory)

            LaunchedEffect(id) {
                formViewModel.cargarDatos(id)
            }

            FormScreen(
                navController = navController,
                viewModel = formViewModel
            )
        }
    }
}