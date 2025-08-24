package dam.a47736.safedose.ui.bottomnavbarpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a47736.safedose.R
import dam.a47736.safedose.data.CommunityCenter

@Composable
fun CenterPage(item: CommunityCenter, onClose: () -> Unit){

    val drawableId = when(item.drawableName){
        "checkpoint.jpg" -> R.drawable.checkpoint
        "gatalmada.jpg" -> R.drawable.gatalmada
        "gatmouraria.jpg" -> R.drawable.gatmouraria
        "gatsetubal.wepp" -> R.drawable.gatsetubal
        "kosmicare.png" -> R.drawable.kosmicare
        else -> R.drawable.gatmouraria
    }
    val servicesText = item.services.map { serviceType -> stringResource(serviceType.id) }.joinToString(", ")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ){
            Image(
                painter = painterResource(id = drawableId),
                contentDescription  = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .size(36.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .clickable{onClose()},
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }
        Column (
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ){
            Text(
                text = item.name,
                fontSize = 22.sp,
                fontWeight = Bold
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text = item.address,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text = stringResource(R.string.schedule)  + item.schedule,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text= stringResource(R.string.services) + servicesText
            )


        }
    }
}