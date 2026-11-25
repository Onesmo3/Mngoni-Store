package com.mngoni.store.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mngoni.store.app.AppContainer
import com.mngoni.store.app.ui.screens.CartScreen
import com.mngoni.store.app.ui.screens.CatalogScreen
import com.mngoni.store.app.ui.screens.CheckoutScreen
import com.mngoni.store.app.ui.screens.HomeScreen
import com.mngoni.store.app.ui.screens.OrderStatusScreen
import com.mngoni.store.app.ui.viewmodels.CartViewModel
import com.mngoni.store.app.ui.viewmodels.CatalogViewModel
import com.mngoni.store.app.ui.viewmodels.CheckoutViewModel
import com.mngoni.store.app.ui.viewmodels.MngoniVmFactory
import com.mngoni.store.app.ui.viewmodels.OrderStatusVmFactory
import com.mngoni.store.app.ui.viewmodels.OrderStatusViewModel

@Composable
fun MngoniNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember { MngoniVmFactory(container) }

    Scaffold(
        bottomBar = { MngoniBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("home") {
                HomeScreen(
                    onBrowse = { navController.navigate("catalog") },
                    onCart = { navController.navigate("cart") },
                )
            }
            composable("catalog") {
                val vm: CatalogViewModel = viewModel(factory = factory)
                val cartVm: CartViewModel = viewModel(factory = factory)
                CatalogScreen(
                    vm = vm,
                    onAdd = { productId -> cartVm.add(productId) },
                    onCart = { navController.navigate("cart") },
                )
            }
            composable("cart") {
                val vm: CartViewModel = viewModel(factory = factory)
                CartScreen(
                    vm = vm,
                    onCheckout = { navController.navigate("checkout") },
                    onBrowse = { navController.navigate("catalog") },
                )
            }
            composable("checkout") {
                val cartVm: CartViewModel = viewModel(factory = factory)
                val checkoutVm: CheckoutViewModel = viewModel(factory = factory)
                val total by cartVm.total.collectAsState()
                CheckoutScreen(
                    vm = checkoutVm,
                    total = total,
                    onDone = { orderId ->
                        navController.navigate("order_status/$orderId") {
                            popUpTo("home")
                        }
                    },
                )
            }
            composable("order_status/{orderId}") { entry ->
                val orderId = entry.arguments?.getString("orderId").orEmpty()
                val vm: OrderStatusViewModel = viewModel(
                    factory = OrderStatusVmFactory(orderId, container.orderRepository),
                )
                OrderStatusScreen(
                    vm = vm,
                    onDone = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun MngoniBottomBar(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route
    val items = listOf(
        Triple("home", "Nyumbani", Icons.Filled.Home),
        Triple("catalog", "Bidhaa", Icons.AutoMirrored.Filled.List),
        Triple("cart", "Cart", Icons.Filled.ShoppingCart),
    )

    NavigationBar {
        items.forEach { (destination, label, icon) ->
            NavigationBarItem(
                selected = route == destination,
                onClick = {
                    if (route != destination) {
                        navController.navigate(destination) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
            )
        }
    }
}
