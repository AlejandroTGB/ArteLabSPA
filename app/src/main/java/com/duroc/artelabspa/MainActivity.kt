package com.duroc.artelabspa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duroc.artelabspa.ui.screens.PostScreen
import com.duroc.artelabspa.ui.theme.ArteLabSPATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ArteLabSPATheme() {
                val postViewModel: com.duroc.artelabspa.viewmodel.PostViewModel = viewModel()

                PostScreen(viewModel = postViewModel)
            }
        }
    }
}
