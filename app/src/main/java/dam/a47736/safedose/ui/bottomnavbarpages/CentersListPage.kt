package dam.a47736.safedose.ui.bottomnavbarpages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a47736.safedose.R
import dam.a47736.safedose.data.CommunityCenter
import dam.a47736.safedose.data.ServiceType
import dam.a47736.safedose.viewmodel.MapViewModel

@Composable
fun CentersListPage(
    modifier: Modifier = Modifier,
    onCardClick: (CommunityCenter) -> Unit)
{
    val mapViewModel: MapViewModel = viewModel()
    val uiState by mapViewModel.uiState.collectAsState()
    val selectedType = uiState.selectedServiceType
    val selectedCenter = uiState.selectedCenter

    var menuExpanded by remember {mutableStateOf(false)}

    LaunchedEffect(Unit){
        mapViewModel.getCommunityCenters()
    }
    Column(modifier = modifier.fillMaxSize()) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ){
            Text(
                text = "Service Type: ",
                modifier = Modifier
                    .padding(end=dimensionResource(R.dimen.padding_small))
                    .align(Alignment.CenterVertically)
            )
            ServiceTypeDropdown(
                selectedType,
                menuExpanded,
                onSelectType = {type ->
                    mapViewModel.setServiceType(type)
                    menuExpanded = false
                },
                onMenuDismiss= {menuExpanded=false},
                onMenuToogle = {menuExpanded = !menuExpanded}
            )
        }



        // A ui é atualiza automáticamente porque a lista é observada.
        LazyColumn {
            items(uiState.filteredCenters) {
                CenterCard(center = it, onClick = {onCardClick(it)})
            }
        }
    }
}

@Composable
fun CenterCard(center: CommunityCenter, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(text = center.name, fontSize = 20.sp)
            Text(text = center.address, fontSize = 16.sp)
        }
    }
}

@Composable
fun ServiceTypeDropdown(
    selectedType: ServiceType,
    menuExpanded: Boolean,
    onSelectType:(ServiceType) -> Unit,
    onMenuDismiss:()->Unit,
    onMenuToogle:()->Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ){
        Button(
            onClick = onMenuToogle,
            modifier = Modifier.width(200.dp)
        ){
            Text(text= stringResource(selectedType.id))
            Icon(imageVector= Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.servicetype_selection))
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = onMenuDismiss
        ) {
            ServiceType.entries.forEach{ type ->
                DropdownMenuItem(
                    text = {Text(text = stringResource(id = type.id))},
                    onClick = { onSelectType(type) }
                )
            }
        }
    }
}