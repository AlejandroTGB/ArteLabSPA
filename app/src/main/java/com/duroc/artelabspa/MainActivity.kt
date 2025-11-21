package com.duroc.artelabspa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.duroc.artelabspa.data.DatabaseProvider
import com.duroc.artelabspa.repository.ProductoRepository
import com.duroc.artelabspa.ui.AppNavigation
import com.duroc.artelabspa.viewmodel.FormViewModelFactory
import com.duroc.artelabspa.viewmodel.HomeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseProvider.getDatabase(applicationContext)
        val repository = ProductoRepository(database.productoDao())
        val homeViewModelFactory = HomeViewModelFactory(repository)
        val formViewModelFactory = FormViewModelFactory(repository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNavigation(
                        homeViewModelFactory = homeViewModelFactory,
                        formViewModelFactory = formViewModelFactory
                    )
                }
            }
        }
    }
}