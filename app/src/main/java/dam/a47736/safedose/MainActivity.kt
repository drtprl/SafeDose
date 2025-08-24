package dam.a47736.safedose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import dam.a47736.safedose.ui.theme.SafeDoseTheme
import dam.a47736.safedose.viewmodel.AuthViewModel
import dam.a47736.safedose.viewmodel.MapViewModel
import dam.a47736.safedose.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()
        val profileViewModel : ProfileViewModel by viewModels()
        val mapViewModel : MapViewModel by viewModels()
        WindowCompat.setDecorFitsSystemWindows(window, true) // Fecha status bar
        setContent {
            SafeDoseTheme {
                Scaffold (
                    modifier = Modifier.fillMaxSize()
                ){ innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding),authViewModel = authViewModel,
                        profileViewModel = profileViewModel, mapViewModel = mapViewModel)
                }
            }
        }
    }
}