package dam.a47736.safedose.viewmodel

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dam.a47736.safedose.data.CommunityCenter
import dam.a47736.safedose.data.ServiceType
import dam.a47736.safedose.data.LocationsRepo
import dam.a47736.safedose.ui.bottomnavbarpages.MapUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val _allCenters = MutableLiveData<List<CommunityCenter>>() //Atualiza dados dentro do view model
    val allCenters: LiveData<List<CommunityCenter>> = _allCenters // Consultar os dados fora do viewmodel



    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getDeviceLocation(fusedLocationClient: FusedLocationProviderClient){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let { loc->
                    _uiState.update { it.copy(userLocation = LatLng(loc.latitude, loc.longitude)) }
                    sortCommunityCentersByLocation()
                }
            }
    }

    fun getCommunityCenters(){
        viewModelScope.launch {
            val centros = withContext(Dispatchers.IO){
                LocationsRepo.obtainLocations()
            }
            _allCenters.value = centros

            val userLoc = uiState.value.userLocation
            if(userLoc != null) {
                sortCommunityCentersByLocation()
            }
            applyFilter(_uiState.value.selectedServiceType)
        }
    }

    fun sortCommunityCentersByLocation(){
        val centrosList = _allCenters.value
        val location = uiState.value.userLocation
        if(centrosList != null){
            val sortedList = centrosList.sortedBy{ center ->
                calculateDistance(location, center.latitude, center.longitude)
            }
            _allCenters.value = sortedList
        }

    }

    fun setServiceType(type: ServiceType){
        _uiState.update { it.copy(selectedServiceType = type) }
        applyFilter(type)
    }

    fun selectCenter(center: CommunityCenter){
        _uiState.update {
            it.copy(selectedCenter = center)
        }
    }

    private fun applyFilter(type: ServiceType){
        val currentList = _allCenters.value
        val filtered = if(type == ServiceType.NOT_SELECTED){
            currentList
        }else{
            currentList.filter{type in it.services}
        }
        _uiState.update{it.copy(filteredCenters = filtered)}
    }

    fun calculateDistance(location1: LatLng?, location2Lat: Double, location2Lng: Double): Float{
        val startPoint = Location("startPoint")
        startPoint.latitude = location1?.latitude!!
        startPoint.longitude = location1.longitude

        val endPoint = Location("endPoint")
        endPoint.latitude = location2Lat
        endPoint.longitude = location2Lng

        return startPoint.distanceTo(endPoint)
    }

    fun toogleFavorite(center: CommunityCenter){
        // Deve ser favoritado no cliente e não no centro
    }


}