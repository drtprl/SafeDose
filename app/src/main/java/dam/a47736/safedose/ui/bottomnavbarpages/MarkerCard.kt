package dam.a47736.safedose.ui.bottomnavbarpages

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import dam.a47736.safedose.data.CommunityCenter

@Composable
fun MarkerCard(
    communityCenter: CommunityCenter,
    onFavoriteToogle: (CommunityCenter) -> Unit
){
    Marker(
        state = MarkerState(position = LatLng(communityCenter.latitude, communityCenter.longitude)),
        title = communityCenter.name,
        snippet = communityCenter.address,
        onClick = {
            it.showInfoWindow()
            false
        }
    ){

    }
}