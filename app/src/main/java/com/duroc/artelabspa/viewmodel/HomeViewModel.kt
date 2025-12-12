package com.duroc.artelabspa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duroc.artelabspa.model.Producto
import com.duroc.artelabspa.repository.MindicadorRepository // <--- Importante: Tu nuevo repo
import com.duroc.artelabspa.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductoRepository) : ViewModel() {
    private val apiRepository = MindicadorRepository()
    private val _valorDolar = MutableStateFlow<Double?>(null)
    val valorDolar: StateFlow<Double?> = _valorDolar.asStateFlow()
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    val productosFiltrados: StateFlow<List<Producto>> = combine(
        repository.productos,
        _searchText
    ) { productos, texto ->
        if (texto.isEmpty()) {
            productos
        } else {
            productos.filter { producto ->
                producto.nombre.contains(texto, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        cargarDolar()
    }
    private fun cargarDolar() {
        viewModelScope.launch {
            try {
                val valor = apiRepository.obtenerValorDolar()
                _valorDolar.value = valor
            } catch (e: Exception) {
                e.printStackTrace()
                _valorDolar.value = null
            }
        }
    }

    fun actualizarTextoBusqueda(texto: String) {
        _searchText.value = texto
    }

    fun toggleBusqueda() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) {
            _searchText.value = ""
        }
    }

    fun cerrarBusqueda() {
        _searchText.value = ""
        _isSearchActive.value = false
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminar(producto)
        }
    }
}