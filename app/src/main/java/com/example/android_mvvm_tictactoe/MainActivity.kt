
package com.example.android_mvvm_tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import com.example.android_mvvm_tictactoe.viewmodel.GameViewModel
import com.example.android_mvvm_tictactoe.view.GameScreen

class MainActivity : ComponentActivity() {

    // Obtiene el ViewModel asociado a esta Activity.
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la interfaz de usuario con Jetpack Compose.
        setContent {
            MaterialTheme {
                Surface {
                    GameScreen(viewModel = viewModel)
                }
            }
        }
    }
}
