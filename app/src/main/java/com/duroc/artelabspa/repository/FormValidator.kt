package com.duroc.artelabspa.repository

import com.duroc.artelabspa.model.Producto

object FormValidator {

    fun validarProducto(producto: Producto): Result<Boolean> {
        if (producto.nombre.isBlank()) {
            return Result.failure(Exception("El nombre es obligatorio."))
        }
        if (producto.precio <= 0) {
            return Result.failure(Exception("El precio debe ser mayor a 0."))
        }
        if (producto.descripcion.isBlank()) {
            return Result.failure(Exception("La descripción es obligatoria."))
        }
        if (producto.fotoUri.isBlank()) {
            return Result.failure(Exception("Debes seleccionar o tomar una foto."))
        }

        return Result.success(true)
    }
}