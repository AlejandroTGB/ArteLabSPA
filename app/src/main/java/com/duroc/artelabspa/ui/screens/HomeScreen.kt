package com.duroc.artelabspa.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duroc.artelabspa.ui.NavRoutes
import com.duroc.artelabspa.ui.components.ProductCard
import com.duroc.artelabspa.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val productosFiltrados by viewModel.productosFiltrados.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Artelab",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                if (isSearchActive) {
                    TextField(
                        value = searchText,
                        onValueChange = { viewModel.actualizarTextoBusqueda(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Buscar productos...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.cerrarBusqueda() }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda")
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Vender",
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    },
                    label = { Text("Vender") },
                    selected = false,
                    onClick = {
                        navController.navigate(NavRoutes.ADD_PRODUCT + "/0")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    label = { Text("Buscar") },
                    selected = false,
                    onClick = { viewModel.toggleBusqueda() }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(productosFiltrados) { producto ->
                ProductCard(
                    producto = producto,
                    onDeleteClick = { viewModel.eliminarProducto(producto) },
                    onEditClick = {
                        navController.navigate(NavRoutes.ADD_PRODUCT + "/${producto.id}")
                    }
                )
            }

            if (productosFiltrados.isEmpty()) {
                item {
                    Text(
                        text = if (searchText.isEmpty()) {
                            "       No hay productos disponibles :(  "
                        } else {
                            "       No se encontraron productos con '$searchText'"
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
