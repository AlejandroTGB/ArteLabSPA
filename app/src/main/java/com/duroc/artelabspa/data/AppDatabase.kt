package com.duroc.artelabspa.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duroc.artelabspa.model.Producto

@Database(
    entities = [Producto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}