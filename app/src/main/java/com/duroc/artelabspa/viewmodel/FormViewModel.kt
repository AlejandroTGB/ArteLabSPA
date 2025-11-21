package com.duroc.artelabspa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duroc.artelabspa.model.Producto
import com.duroc.artelabspa.repository.FormValidator
import com.duroc.artelabspa.repository.ProductoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Producto(0, "", "", 0, "General", ""))
    val state = _state.asStateFlow()

    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        data class Failure(val error: String) : ValidationEvent()
    }

    fun cargarDatos(id: Int) {
        if (id == 0) {
            _state.value = Producto(0, "", "", 0, "General", "")
            return
        }
        viewModelScope.launch {
            val productoEncontrado = repository.obtenerPorId(id)
            if (productoEncontrado != null) {
                _state.value = productoEncontrado
            }
        }
    }

    fun onNameChange(text: String) {
        _state.update { it.copy(nombre = text) }
    }

    fun onPriceChange(text: String) {
        val precio = text.toIntOrNull() ?: 0
        _state.update { it.copy(precio = precio) }
    }

    fun onDescriptionChange(text: String) {
        _state.update { it.copy(descripcion = text) }
    }

    fun onPhotoChange(newUri: String) {
        _state.update { it.copy(fotoUri = newUri) }
    }

    fun onSave() {
        val productoActual = _state.value
        val resultado = FormValidator.validarProducto(productoActual)

        if (resultado.isSuccess) {
            viewModelScope.launch {
                if (productoActual.id == 0) {
                    repository.insertar(productoActual)
                } else {
                    repository.actualizar(productoActual)
                }
                _validationEvent.send(ValidationEvent.Success)
                _state.value = Producto(0, "", "", 0, "General", "")
            }
        } else {
            val errorMsg = resultado.exceptionOrNull()?.message ?: "Error desconocido"
            viewModelScope.launch {
                _validationEvent.send(ValidationEvent.Failure(errorMsg))
            }
        }
    }
}