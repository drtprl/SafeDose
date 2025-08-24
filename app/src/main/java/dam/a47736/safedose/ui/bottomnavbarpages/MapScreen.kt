package dam.a47736.safedose.ui.bottomnavbarpages

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dam.a47736.safedose.data.CommunityCenter
import dam.a47736.safedose.viewmodel.MapViewModel
import dam.a47736.safedose.R

/*
Map screen to search for a community center, every pin has a color depending on the service offer
by the center.
After selecting a center on the map, the user is redirected to the info page of that center.
This info page will have a return button to go back to the map page saving the state of map page.
Every time we enter in this page we will see the favorite centers of the user marked differently,
also we will see the color category of every center icon on the map.
 */

@Composable
fun MapPage(
    modifier: Modifier = Modifier
){
    val mapViewModel: MapViewModel = viewModel()
    val uiState by mapViewModel.uiState.collectAsState()

    val selectedType = uiState.selectedServiceType
    var menuExpanded by remember {mutableStateOf(false)}

    val centros = uiState.filteredCenters
    val userLocation = uiState.userLocation

    var selectedCenter by remember { mutableStateOf<CommunityCenter?>(null)}

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted: Boolean ->
        if (isGranted) {
            mapViewModel.getDeviceLocation(fusedLocationClient)
        }
    }

    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapViewModel.getDeviceLocation(fusedLocationClient)
        }else{
            permissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
        mapViewModel.getCommunityCenters()
    }



    //Se houver disponível localização do user, esta será a posição inicial da camera
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
    }

    var mapUiSettings by remember { mutableStateOf(MapUiSettings(
        zoomControlsEnabled = true
    )) }
    var mapProperties by remember { mutableStateOf(MapProperties(
        mapType = MapType.NORMAL,
        isMyLocationEnabled = true
    )) }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            centros?.forEach{center ->
                val centerLatLng = LatLng(center.latitude, center.longitude)
                val centerState = MarkerState(position = centerLatLng)
                Marker(
                    state = centerState,
                    title = center.name,
                    snippet = center.address,
                    onClick = {
                        selectedCenter = center
                        false
                    }
                )
            }

        }

        selectedCenter?.let{center ->
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = center.name,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = center.address,
                        //style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = if(center.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Favorite",
                        tint = if(center.isFavorite) Color.Yellow else Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable{mapViewModel.toogleFavorite(center)}
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dimensionResource(R.dimen.padding_medium))
        ){
            ServiceTypeDropdown(
                selectedType = selectedType,
                menuExpanded = menuExpanded,
                onSelectType = {type ->
                    mapViewModel.setServiceType(type)
                    menuExpanded = false
                },
                onMenuDismiss= {menuExpanded=false},
                onMenuToogle = {menuExpanded = !menuExpanded}
            )
        }

    }

}