package com.example.arenaysol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arenaysol.ui.navigation.NavRoutes
import com.example.arenaysol.ui.screens.role.RoleSelectionScreen
import com.example.arenaysol.data.model.UserRole
import com.example.arenaysol.ui.theme.ArenaysolTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArenaysolTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.RoleSelection.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavRoutes.RoleSelection.route) {
                            RoleSelectionScreen(onRoleSelected = { role ->
                                when (role) {
                                    UserRole.ADMIN -> navController.navigate(NavRoutes.AdminDashboard.route)
                                    UserRole.WAITER -> navController.navigate(NavRoutes.WaiterInterface.route)
                                    UserRole.KITCHEN -> navController.navigate(NavRoutes.KitchenDisplay.route)
                                    UserRole.DOORMAN -> navController.navigate(NavRoutes.DoormanEntry.route)
                                }
                            })
                        }
                        composable(NavRoutes.AdminDashboard.route) {
                            com.example.arenaysol.ui.screens.admin.AdminDashboardScreen()
                        }
                        composable(NavRoutes.WaiterInterface.route) {
                            com.example.arenaysol.ui.screens.waiter.WaiterScreen()
                        }
                        composable(NavRoutes.KitchenDisplay.route) {
                            com.example.arenaysol.ui.screens.kitchen.KitchenScreen()
                        }
                        composable(NavRoutes.DoormanEntry.route) {
                            com.example.arenaysol.ui.screens.doorman.DoormanScreen()
                        }
                    }
                }
            }
        }
    }
}
