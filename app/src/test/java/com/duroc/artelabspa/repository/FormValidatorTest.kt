package com.duroc.artelabspa.repository

import com.duroc.artelabspa.model.Producto
import org.junit.Assert.*
import org.junit.Test

class FormValidatorTest {

    @Test
    fun validarProductoConDatosValidosDebeRetornarSuccess() {
        val producto = Producto(
            nombre = "Pincel",
            descripcion = "Pincel PRO",
            precio = 1000,
            categoria = "PINCEL PRO",
            fotoUri = "https://example.com/foto.jpg"
        )

        val resultado = FormValidator.validarProducto(producto)

        assertTrue(resultado.isSuccess)
    }

    @Test
    fun validarProductoConNombreVacioDebeRetornarFailure() {
        val producto = Producto(
            nombre = "",
            descripcion = "Pincel PRO",
            precio = 1000,
            categoria = "PINCEL PRO",
            fotoUri = "https://example.com/foto.jpg"
        )

        val resultado = FormValidator.validarProducto(producto)

        assertTrue(resultado.isFailure)
        assertEquals("El nombre es obligatorio.", resultado.exceptionOrNull()?.message)
    }

    @Test
    fun validarProductoConPrecioCeroDebeRetornarFailure() {
        val producto = Producto(
            nombre = "PINCEL PRO NACHI",
            descripcion = "Descripción",
            precio = 0,
            categoria = "PINCEL PRO",
            fotoUri = "https://example.com/foto.jpg"
        )

        val resultado = FormValidator.validarProducto(producto)

        assertTrue(resultado.isFailure)
        assertEquals("El precio debe ser mayor a 0.", resultado.exceptionOrNull()?.message)
    }
}
