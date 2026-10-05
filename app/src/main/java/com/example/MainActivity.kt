package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.navigation.RekiyaApp
import com.example.ui.theme.RekiyaSoyamuTheme
import com.example.ui.viewmodel.JobViewModel

class MainActivity : ComponentActivity() {

    private val jobViewModel: JobViewModel by viewModels {
        JobViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RekiyaSoyamuTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RekiyaApp(viewModel = jobViewModel)
                }
            }
        }
    }
}
