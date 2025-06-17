package com.example.chaletbnb

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chaletbnb.data.models.Chalet
import com.example.chaletbnb.ui.components.BottomNavBar
import com.example.chaletbnb.ui.screens.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChaletBnBApp()
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChaletBnBApp() {
    val navController = rememberNavController()
    val noBottomBarRoutes = listOf("splash", "login", "register")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in noBottomBarRoutes) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { Login(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("chaletDetail") {
                val chalet = navController.previousBackStackEntry
                    ?.savedStateHandle?.get<Chalet>("selectedChalet")

                chalet?.let {
                    ChaletDetailScreen(navController = navController, chalet = it)
                }
            }
            composable("bookings") { BookingScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("history") { BookingHistory(navController) }

        }
    }

}

