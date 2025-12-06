package com.duroc.artelabspa.model

import org.junit.Assert.*
import org.junit.Test
import kotlin.compareTo

class ProductoTest {

    @Test
    fun crearProductoConDatosValidosDebeCrearseCorrectamente() {
        val producto = Producto(
            id = 1,
            nombre = "Masaje Relajante",
            descripcion = "Masaje de 60 minutos",
            precio = 25000,
            categoria = "Masajes",
            fotoUri = "https://example.com/masaje.jpg"
        )

        assertEquals(1, producto.id)
        assertEquals("Masaje Relajante", producto.nombre)
        assertEquals("Masaje de 60 minutos", producto.descripcion)
        assertEquals(25000, producto.precio)
        assertEquals("Masajes", producto.categoria)
        assertEquals("https://example.com/masaje.jpg", producto.fotoUri)
    }

    @Test
    fun crearProductoSinIdDebeUsarCeroPorDefecto() {
        val producto = Producto(
            nombre = "Pincel Pro",
            descripcion = "Pincel profesional",
            precio = 1500,
            categoria = "Pinceles",
            fotoUri = "https://example.com/pincel.jpg"
        )

        assertEquals(0, producto.id)
        assertEquals("Pincel Pro", producto.nombre)
    }

    @Test
    fun crearProductoConPrecioValidoDebeFuncionar() {
        val producto = Producto(
            nombre = "Test",
            descripcion = "Test",
            precio = 5000,
            categoria = "Test",
            fotoUri = "https://example.com/test.jpg"
        )

        assertTrue(producto.precio > 0)
    }
}
