package dam.a47736.safedose.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dam.a47736.safedose.AppUtil
import dam.a47736.safedose.R
import dam.a47736.safedose.viewmodel.AuthState
import dam.a47736.safedose.viewmodel.AuthViewModel

/**
 * In this page we are going to create an account to our user
 *
 * @param authState it is directly observate from de viewModel to check if where are already
 * authenticated.
 */
@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Image(
                painter = painterResource(R.drawable.safedose_logo),
                contentDescription = "SafeDose app logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentScale = ContentScale.Fit

            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = { Text(stringResource(R.string.name_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_small)
                )
        )
        EmailField(
            value = email,
            onValueChange = {email = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_small)
                )
        )
        PasswordField(
            value = password,
            onValueChange = {password  = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_small)
                )
        )
        Button(
            onClick = {
                authViewModel.signup(name, email, password){success, errorMessage ->
                    if(success){
                        AppUtil.showToast(context, "Signup Successful")
                        navController.navigate("login")
                    }else{
                        AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                    }
                }
                      },
            enabled = authState.value != AuthState.Loading,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ){
            Text(stringResource(R.string.signup_button))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {navController.navigate("login")}){
            Text(stringResource(R.string.login_textbutton))
        }
    }

}