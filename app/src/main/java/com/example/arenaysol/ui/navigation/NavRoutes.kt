package com.example.arenaysol.ui.navigation

sealed class NavRoutes(val route: String) {
    object RoleSelection : NavRoutes("role_selection")
    object AdminDashboard : NavRoutes("admin_dashboard")
    object WaiterInterface : NavRoutes("waiter_interface")
    object KitchenDisplay : NavRoutes("kitchen_display")
    object DoormanEntry : NavRoutes("doorman_entry")
    object CashierDashboard : NavRoutes("cashier_dashboard")
}
