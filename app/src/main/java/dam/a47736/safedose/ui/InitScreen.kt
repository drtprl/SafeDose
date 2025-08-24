package dam.a47736.safedose.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dam.a47736.safedose.ui.bottomnavbarpages.CentersListPage
import dam.a47736.safedose.ui.bottomnavbarpages.DosePage
import dam.a47736.safedose.ui.bottomnavbarpages.MapPage
import dam.a47736.safedose.ui.bottomnavbarpages.NavItem
import dam.a47736.safedose.ui.bottomnavbarpages.ProfilePage
import dam.a47736.safedose.viewmodel.AuthViewModel
import dam.a47736.safedose.viewmodel.MapViewModel
import dam.a47736.safedose.viewmodel.ProfileViewModel

/*
In this screen, we are displaying the logo page and some information of our app functioning.
 */

@Composable
fun InitPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, profileViewModel: ProfileViewModel, mapViewModel: MapViewModel){

    val navItems = listOf(
        NavItem("Map", Icons.Default.Map),
        NavItem("Centers", Icons.Default.MedicalServices),
        NavItem("Dose", Icons.Default.Medication),
        NavItem("Profile", Icons.Default.Person)
    )

    var selectedIndex by remember{
        mutableStateOf(0)
    }

    Scaffold (
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.label)
                        },
                        label={
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ){
        ContentScreen(modifier = modifier.padding(it), selectedIndex, authViewModel, navController, profileViewModel, mapViewModel)
    }
}

@Composable
fun ContentScreen(modifier: Modifier= Modifier, selectedIndex:Int, authViewModel: AuthViewModel, navController: NavController, profileViewModel: ProfileViewModel, mapViewModel: MapViewModel){
    when(selectedIndex) {
        0 -> MapPage(modifier)
        1 -> CentersListPage(modifier){ center ->
            mapViewModel.selectCenter(center)
            navController.navigate("centerPage")
        }
        2 -> DosePage(modifier, authViewModel = authViewModel, profileViewModel = profileViewModel)
        3 -> ProfilePage(modifier, authViewModel = authViewModel, navController = navController, profileViewModel = profileViewModel)
    }
}
