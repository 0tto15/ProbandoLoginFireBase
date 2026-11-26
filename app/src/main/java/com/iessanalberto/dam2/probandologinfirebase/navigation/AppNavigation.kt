package com.iessanalberto.dam2.probandologinfirebase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iessanalberto.dam2.probandologinfirebase.screens.LoginScreen
import com.iessanalberto.dam2.probandologinfirebase.screens.MenuScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route){
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(
                // Le pasamos solamente la ruta, no el nav completo.
                // Una funcion
                onLoginSuccess = {navController.navigate(AppScreens.MenuScreen.route)},
                // si hubieses mas pantallas
                // otraPantallaFunciones = {navController.navigate(AppScreens.otraPantalla.route)}
            ) }
        // Forma antigua
        composable(route = AppScreens.MenuScreen.route){ MenuScreen(navController = navController) }
    }
}