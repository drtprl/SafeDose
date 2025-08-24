package dam.a47736.safedose.ui.bottomnavbarpages

import com.google.android.gms.maps.model.LatLng
import dam.a47736.safedose.data.CommunityCenter
import dam.a47736.safedose.data.ServiceType

data class MapUiState (
    val selectedServiceType: ServiceType = ServiceType.NOT_SELECTED,
    val filteredCenters: List<CommunityCenter> = emptyList(),
    val userLocation: LatLng? = null,
    val selectedCenter: CommunityCenter? = null
)