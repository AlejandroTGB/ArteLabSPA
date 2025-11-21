package com.duroc.artelabspa.repository

import com.duroc.artelabspa.data.ProductoDao
import com.duroc.artelabspa.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    val productos: Flow<List<Producto>> = productoDao.getAll()

    suspend fun insertar(producto: Producto) {
        productoDao.insert(producto)
    }

    suspend fun actualizar(producto: Producto) {
        productoDao.update(producto)
    }

    suspend fun eliminar(producto: Producto) {
        productoDao.delete(producto)
    }

    suspend fun obtenerPorId(id: Int): Producto? {
        return productoDao.getById(id)
    }
}