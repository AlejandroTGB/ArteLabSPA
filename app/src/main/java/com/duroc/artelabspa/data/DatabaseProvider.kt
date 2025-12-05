package com.duroc.artelabspa.data

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duroc.artelabspa.model.Producto
import kotlin.text.insert


object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null
    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val callback = object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        insertInitialData(INSTANCE!!.productoDao())
                    }
                }
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_artelab_db_v2"
            )
                .fallbackToDestructiveMigration()
                .addCallback(callback)
                .build()
            INSTANCE = instance
            instance
        }
    }
    private suspend fun insertInitialData(dao: ProductoDao) {
        val producto1 = Producto(
            nombre = "Cuadro Besto Shonen",
            precio = 20000,
            descripcion = "Cuadro del besto shonen jeje",
            categoria = "Cuadro",
            fotoUri = "https://tanoshii.pe/wp-content/uploads/2023/08/BLACK-CLOVER-03.jpg"
        )
        val producto2 = Producto(
            nombre = "Kit de Arte",
            precio = 35000,
            descripcion = "Kit de arte completo",
            categoria = "Kit",
            fotoUri = "https://m.media-amazon.com/images/I/81BDFEDdG2L._AC_SL1500_.jpg"
        )
        dao.insert(producto1)
        dao.insert(producto2)
    }
}