package dam.a47736.safedose.ui.bottomnavbarpages

import dam.a47736.safedose.data.Dose
import dam.a47736.safedose.data.UserData

data class ProfileUiState(
    val userData: UserData? = null,
    val doses: List<Dose> = emptyList(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)
