package dam.a47736.safedose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a47736.safedose.data.Dose
import dam.a47736.safedose.data.FirestoreRepo
import dam.a47736.safedose.data.Medication
import dam.a47736.safedose.ui.bottomnavbarpages.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val authViewModel = AuthViewModel()
    private val firestoreOperations = FirestoreRepo.firestoreOperations
    private val gemini = FirestoreRepo.geminiOperations

    fun loadUserData(userId: String){
        firestoreOperations.getUserData (
            userId,
            onSuccess = { user ->
                _uiState.update{it.copy(userData = user)}
                        },
            onFailure = {}
        )
        firestoreOperations.getDoses(
            userId = userId,
            onSuccess = { doseList ->
                _uiState.update{it.copy(doses = doseList)}

            },
            onFailure = {
                _uiState.update{it.copy(errorMessage = "Error donwloading doses from firestore")}
            }
        )
    }

    fun addDailyMedication(medication: Medication){
        var userId = authViewModel.getUserId()
        if (userId != null) {
            firestoreOperations.addDailyMedication(userId, medication)
            loadUserData(userId)
        }
    }

    fun addDose(dose: Dose){
        viewModelScope.launch{
            _uiState.update{it.copy(isLoading=true, success = false, errorMessage = null)}
            try {
                val dailyMedication = uiState.value.userData?.dailyMedications ?: emptyList()
                val interactions = gemini.makeQuery(dose, dailyMedication)
                val updatedDose = dose.copy(interactions = interactions)
                val userId = authViewModel.getUserId() ?: throw Exception("User ID null")
                firestoreOperations.addDose(userId,updatedDose)
                loadUserData(userId)
                _uiState.update{it.copy(isLoading = false, success = true)}
            }catch (e: Exception){
                _uiState.update{
                    it.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}