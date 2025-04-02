import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.LoginResponse
import com.park.conductor.navigation.Dashboard
import com.park.conductor.navigation.Login
import com.park.conductor.presentation.login.LoginViewModel
import com.park.conductor.ui.theme.Green40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (String, String) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isButtonEnabled = username.isNotEmpty() && password.length >= 8
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 20.dp, 10.dp, 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Filled.NaturePeople,
                        contentDescription = "Park Ticket Booking Icon",
                        tint = Green40,
                        modifier = Modifier.size(70.dp)
                    )

                    Text(
                        text = "ParkEasy",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Green40
                    )
                }
                Text(
                    text = "An initiative by Lucknow Development Authority",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Login",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = username,
                        onValueChange = {
                            if (it.matches(Regex("[a-zA-Z0-9._]*"))) {
                                username = it.trim()
                            }
                        },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White // Sets the background color to white
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it.trim() },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.RemoveRedEye else Icons.Filled.Lock,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White // Sets the background color to white
                        )

                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { onLoginSuccess(username, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = isButtonEnabled,
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = Green40,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Text("Login")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Trouble logging in? ")
                        Text(
                            text = "Contact admin.",
                            fontWeight = FontWeight.Medium,
                            color = Color.Blue,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "Contacting admin...", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    }
                }
            }

            Text(text = "Powered by", color = Color.LightGray)
            Text(
                text = "INNOBLES",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )


            val state by loginViewModel.login.collectAsState()
            SetUpObserver(state, navController)
        }
    }
}

@Composable
fun SetUpObserver(state: ApiState<LoginResponse>, navController: NavController) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            is ApiState.Loading -> {
                Log.d("TAG", "Loading login Observer")
                // Show loading indicator here, if required
            }

            is ApiState.Success -> {
                Prefs.saveLogin(state.data)
                val userInfo = state.data.userInfo
                Log.d("TAG", "In login Success ::::$userInfo.toString()")
                navController.navigate(Dashboard) {
                    popUpTo(Login) { inclusive = true }
                }
            }

            is ApiState.Error -> {
                Log.d("TAG", "In login Errorrrrrrrrrrrrrrrrr")
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                Log.d("TAG", "login  error message + ${state.message}")

                val errorMessage = state.message ?: "Unknown error occurred"
                snackBarHostState.showSnackbar(errorMessage)

            }
        }
    }
}


@Composable
fun LoginHandler() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    LoginScreen(rememberNavController(), { username, password -> })
}