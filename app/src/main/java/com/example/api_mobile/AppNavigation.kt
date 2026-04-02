package com.example.api_mobile

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.api_mobile.ui.screens.CartScreen
import com.example.api_mobile.ui.screens.CategoryOffersScreen
import com.example.api_mobile.ui.screens.HomeScreen
import com.example.api_mobile.ui.screens.LoginScreen
import com.example.api_mobile.ui.screens.RegisterScreen
import com.example.api_mobile.ui.viewmodel.AuthViewModel
import com.example.mercadodani.ui.theme.MercadoDaniTheme

sealed class Screen(val route: String) {
    object Login          : Screen("login")
    object Register       : Screen("register")
    object Home           : Screen("home")
    object Cart           : Screen("cart")
    object CategoryOffers : Screen("category_offers/{displayName}/{dbName}") {
        fun createRoute(displayName: String, dbName: String) = "category_offers/$displayName/$dbName"
    }
}

@Composable
fun AppNavigation() {
    MercadoDaniTheme {
        val navController = rememberNavController()
        val authViewModel: AuthViewModel = viewModel()

        NavHost(navController = navController, startDestination = Screen.Home.route) {

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess      = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                    onFirstAccess       = { navController.navigate(Screen.Register.route) },
                    onForgotPassword    = { },
                    onBottomNavSelected = { handleBottomNav(it, navController) },
                    authViewModel       = authViewModel
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess    = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Register.route) { inclusive = true } } },
                    onAlreadyHaveAccount = { navController.navigate(Screen.Login.route) { popUpTo(Screen.Register.route) { inclusive = true } } },
                    onBottomNavSelected  = { handleBottomNav(it, navController) },
                    authViewModel        = authViewModel
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onCategoryClick     = { category -> navController.navigate(Screen.CategoryOffers.createRoute(category.name, category.dbName)) },
                    onBottomNavSelected = { handleBottomNav(it, navController) },
                    onNavigateToLogin   = { navController.navigate(Screen.Login.route) }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    onBottomNavSelected = { handleBottomNav(it, navController) },
                    onNavigateToLogin   = { navController.navigate(Screen.Login.route) }
                )
            }

            composable(Screen.CategoryOffers.route) { backStack ->
                val displayName = backStack.arguments?.getString("displayName") ?: "Categoria"
                val dbName      = backStack.arguments?.getString("dbName") ?: displayName
                CategoryOffersScreen(
                    categoryName        = displayName,
                    dbCategoryName      = dbName,
                    onBack              = { navController.popBackStack() },
                    onBottomNavSelected = { handleBottomNav(it, navController) },
                    onNavigateToLogin   = { navController.navigate(Screen.Login.route) }
                )
            }
        }
    }
}

private fun handleBottomNav(index: Int, navController: NavController) {
    when (index) {
        0 -> navController.navigate(Screen.Cart.route) { popUpTo(Screen.Home.route) }
        1 -> navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } }
        2 -> navController.navigate(Screen.Login.route)
    }
}