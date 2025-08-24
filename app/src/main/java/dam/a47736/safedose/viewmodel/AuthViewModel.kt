package dam.a47736.safedose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dam.a47736.safedose.R
import dam.a47736.safedose.data.FirestoreRepo
import dam.a47736.safedose.data.UserData

class AuthViewModel : ViewModel(){

    private val auth = Firebase.auth
    private val firestoreOperations = FirestoreRepo.firestoreOperations

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init{
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser != null){
            _authState.value = AuthState.Authenticated
        }else{
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error(R.string.empty_fields.toString())
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(name: String, email: String, password: String, onResult : (Boolean, String?)-> Unit){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error(R.string.empty_fields.toString())
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    var userId = task.result?.user?.uid
                    // !! -> not null assertion operator
                    val userData = UserData(name, email, userId!!)
                    // Creating a new collection
                    firestoreOperations.addUserData(userId, userData){ success, errorMessage ->
                        if(success){
                            onResult(true, null)
                        }else{
                            onResult(false, errorMessage)
                        }
                        _authState.value = AuthState.Unauthenticated
                    }
                }else{
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun getUserId(): String?{
        return Firebase.auth.currentUser?.uid
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()

}