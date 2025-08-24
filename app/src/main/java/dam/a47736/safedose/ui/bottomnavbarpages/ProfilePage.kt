package dam.a47736.safedose.ui.bottomnavbarpages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dam.a47736.safedose.AppUtil
import dam.a47736.safedose.R
import androidx.compose.foundation.layout.height
import dam.a47736.safedose.data.Medication
import dam.a47736.safedose.data.UserData
import dam.a47736.safedose.viewmodel.AuthState
import dam.a47736.safedose.viewmodel.AuthViewModel
import dam.a47736.safedose.viewmodel.ProfileViewModel

@Composable
fun ProfilePage(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    navController : NavController) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val profileUi by profileViewModel.uiState.collectAsState()

    LaunchedEffect(authState.value) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            profileViewModel.loadUserData(userId)
        }
    }

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
                AppUtil.showToast(context, R.string.logout_toast.toString())
            }
            is AuthState.Error -> AppUtil.showToast(context, (authState.value as AuthState.Error).message)
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserCard(profileUi.userData)
        AddDailyMedicationCard(profileViewModel)
        TextButton(
            onClick = { authViewModel.signout() },
            modifier = modifier
        ){
            Text(stringResource(R.string.logout_button))
        }
        CreatorInfoCard()
    }
}

@Composable
fun UserCard(
    userData : UserData?
){
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ){
        if(userData == null){
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Profile:",
                    fontWeight = Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator(color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Profile:",
                    fontWeight = Bold
                )
                Text(userData?.name ?: "Getting user data...")
                Text(userData?.email ?: "Email")
                Text(
                    text = "Daily medication:",
                    fontWeight = Bold
                )
                userData?.dailyMedications?.forEach { med ->
                    Text(text = "${med.name}, ${med.dosage} mg")
                }
            }
        }


    }
}

@Composable
fun AddDailyMedicationCard(profileViewModel: ProfileViewModel){

    var medicationName by remember { mutableStateOf("") }
    var medicationDose by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)){
        OutlinedTextField(
            value = medicationName,
            onValueChange = {medicationName = it},
            placeholder = { Text(text=stringResource(R.string.medication_name_label)) },
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_small))
        )
        OutlinedTextField(
            value = medicationDose,
            onValueChange = {medicationDose = it},
            placeholder = { Text(text=stringResource(R.string.medication_dose_label)) },
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_small)),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Button(
            modifier = Modifier.padding(8.dp).align(alignment = Alignment.CenterHorizontally),
            onClick = {
                profileViewModel.addDailyMedication(Medication(medicationName, medicationDose.toIntOrNull() ?: 100))
            },
            content = { Text(stringResource(R.string.add_daily_medication_button)) },
        )
    }
}

@Composable
fun CreatorInfoCard(){
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)){
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ){
            Text(stringResource(R.string.creator_name))
            Text(stringResource(R.string.app_version_details))
        }

    }
}