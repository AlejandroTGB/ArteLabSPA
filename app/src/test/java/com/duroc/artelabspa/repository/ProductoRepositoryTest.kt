package com.duroc.artelabspa.repository

import com.duroc.artelabspa.data.ProductoDao
import com.duroc.artelabspa.model.Producto
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductoRepositoryTest {

    private lateinit var productoDao: ProductoDao
    private lateinit var repository: ProductoRepository

    @Before
    fun setup() {
        productoDao = mockk()
        every { productoDao.getAll() } returns flowOf(emptyList())
        repository = ProductoRepository(productoDao)
    }

    @Test
    fun insertarProductoDebeInvocarDaoInsert() = runTest {
        val producto = Producto(
            nombre = "Masaje Test",
            descripcion = "Descripción test",
            precio = 10000,
            categoria = "Test",
            fotoUri = "https://example.com/test.jpg"
        )

        coEvery { productoDao.insert(producto) } just Runs

        repository.insertar(producto)

        coVerify(exactly = 1) { productoDao.insert(producto) }
    }

    @Test
    fun obtenerTodosLosProductosDebeRetornarFlowDelDao() = runTest {
        val productos = listOf(
            Producto(
                id = 1,
                nombre = "Producto 1",
                descripcion = "Desc 1",
                precio = 5000,
                categoria = "Cat 1",
                fotoUri = "url1"
            ),
            Producto(
                id = 2,
                nombre = "Producto 2",
                descripcion = "Desc 2",
                precio = 8000,
                categoria = "Cat 2",
                fotoUri = "url2"
            )
        )

        val daoConProductos = mockk<ProductoDao>()
        every { daoConProductos.getAll() } returns flowOf(productos)
        val repoConProductos = ProductoRepository(daoConProductos)

        val result = repoConProductos.productos

        result.collect { listaProductos ->
            assertEquals(2, listaProductos.size)
            assertEquals("Producto 1", listaProductos[0].nombre)
            assertEquals("Producto 2", listaProductos[1].nombre)
        }
    }

    @Test
    fun eliminarProductoDebeInvocarDaoDelete() = runTest {
        val producto = Producto(
            id = 1,
            nombre = "Producto a eliminar",
            descripcion = "Será eliminado",
            precio = 3000,
            categoria = "Test",
            fotoUri = "url"
        )

        coEvery { productoDao.delete(producto) } just Runs

        repository.eliminar(producto)

        coVerify(exactly = 1) { productoDao.delete(producto) }
    }
}
