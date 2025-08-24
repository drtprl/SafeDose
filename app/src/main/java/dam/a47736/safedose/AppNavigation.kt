package dam.a47736.safedose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam.a47736.safedose.ui.InitPage
import dam.a47736.safedose.ui.LoginPage
import dam.a47736.safedose.ui.SignupPage
import dam.a47736.safedose.ui.bottomnavbarpages.CenterPage
import dam.a47736.safedose.viewmodel.AuthViewModel
import dam.a47736.safedose.viewmodel.MapViewModel
import dam.a47736.safedose.viewmodel.ProfileViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, profileViewModel: ProfileViewModel, mapViewModel: MapViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login",
        builder={
            composable("login"){
                LoginPage(modifier,navController,authViewModel)
            }
            composable("signup"){
                SignupPage(modifier,navController,authViewModel)
            }
            composable("home"){
                InitPage(modifier,navController,authViewModel, profileViewModel, mapViewModel)
            }
            composable("centerPage") {
                val selectedCenter by mapViewModel.uiState.collectAsState()
                selectedCenter.selectedCenter?.let{center ->
                    CenterPage(item = center, onClose = {navController.popBackStack()})
                }
            }
        }
    )
}