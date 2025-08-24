package dam.a47736.safedose.ui.bottomnavbarpages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import dam.a47736.safedose.R
import dam.a47736.safedose.data.Dose
import dam.a47736.safedose.viewmodel.AuthViewModel
import dam.a47736.safedose.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
In this screen, our user can register a dose intake and our AI can screen an alert in case of
risky interaction between any of the drugs.
 */

@Composable
fun DosePage(modifier: Modifier, authViewModel: AuthViewModel, profileViewModel: ProfileViewModel) {

    val authState = authViewModel.authState.observeAsState()
    val uiState by profileViewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedDose by remember {mutableStateOf<Dose?>(null)}

    var drugName by remember {mutableStateOf("")}

    LaunchedEffect(authState.value) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            profileViewModel.loadUserData(userId)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ){
        Row(modifier = modifier
            .height(40.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Dose Register Page",
                fontSize = 30.sp,
                fontWeight = Bold
            )
        }

        Button(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            onClick = { showDialog = true }
        ) {
            if(uiState.isLoading){
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text(stringResource(R.string.register_dose_button))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ){
            items(uiState.doses){ dose ->
                DoseCard(dose, onClick = {selectedDose = dose})
            }
        }


    }

    // Dialogo para registo de consumo
    if(showDialog){
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = {Text(stringResource(R.string.register_dose_title))},
            text = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = drugName,
                    onValueChange = {drugName = it},
                    placeholder = {Text(stringResource(R.string.dose_placeholder))}
                )
            },
            confirmButton = {
                Button (
                    onClick = {
                        // Dose(userId,timestamp,drugs,interactions)
                        val userId = authViewModel.getUserId()
                        if(userId == null)
                            return@Button
                        val newDose = Dose(
                            userId = userId,
                            drugs = drugName.split(",").map{it.trim()}
                        )
                        profileViewModel.addDose(newDose)
                        showDialog = false
                    }
                ){
                    Text(stringResource(R.string.dose_register))
                }
            },
            dismissButton = {
                TextButton(onClick = {showDialog = false}){
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    // Dialogo para os detalhes das interações
    selectedDose?.let{ dose ->
        AlertDialog(
            onDismissRequest = {selectedDose = null},
            title = {Text(stringResource(R.string.interaction_details))},
            text = {
                Column {
                    dose.interactions.forEach {
                        Text(stringResource(R.string.drugs)+ it.drugs.toString())
                        Text(stringResource(R.string.interaction)+ it.risk)
                        Text(stringResource(R.string.interaction_explanation)+ it.explanation)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {selectedDose = null}){
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )

    }

}

@Composable
fun DoseCard(dose: Dose, onClick: () -> Unit){
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
            val drugsText = dose.drugs.joinToString(", ")
            Text(text = drugsText , fontSize = 20.sp)
            val simpleDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDate = simpleDate.format(Date(dose.timestamp))
            Text(text = formattedDate, fontSize = 16.sp)
        }
    }
}