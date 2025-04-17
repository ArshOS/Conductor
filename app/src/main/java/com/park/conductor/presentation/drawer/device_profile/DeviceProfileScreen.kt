package com.park.conductor.presentation.drawer.device_profile

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.park.conductor.R
import com.park.conductor.common.utilities.Prefs

@Composable
fun DeviceProfileScreen(
    paddingValues1: PaddingValues,
    navController: NavHostController,
) {


    Scaffold(
        topBar = {
            TopBarComposable(
                Icons.Filled.Menu,
                Prefs.getLogin()?.userInfo?.parkName,
                R.drawable.logo_lda_white
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                DeviceProfileComposable()
            }

        }
    )

}

@Composable
fun TopBarComposable(
    menu: ImageVector,
    parkName: String?,
    logoLda: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(all = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable { },
                imageVector = menu, contentDescription = null, tint = Color.White
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = parkName.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Image(painter = painterResource(logoLda), contentDescription = null)

    }
}

@Composable
fun InfoBar(label: String, info: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .background(
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp)
    ) {
        Icon(modifier = Modifier.size(35.dp), imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = info,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DeviceProfileComposable() {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(20.dp),
            text = "Operator Profile",
            textAlign = TextAlign.Center,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.padding(10.dp))

//        Text(
//            modifier = Modifier.clickable {
//                Prefs.clear()
////                    navController.navigate(Login)
//
//
//                val activity = context as? Activity
//                activity?.apply {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//            },
//            text = "Logout"
//        )

        val operatorInfo = Prefs.getLogin()?.userInfo

        val operatorName: String = operatorInfo?.userName ?: "--"
        val username: String = operatorInfo?.userName ?: "--"
        val operatorEmail: String = operatorInfo?.email ?: "--"
        val operatorMobile: String = operatorInfo?.mobile ?: "--"

        InfoBar(label = "Name", info = operatorName, icon = Icons.Outlined.Person)
        InfoBar(label = "Username", info = username, icon = Icons.Outlined.VerifiedUser)
        InfoBar(label = "Email", info = operatorEmail, icon = Icons.Outlined.Email)
        InfoBar(label = "Mobile", info = operatorMobile, icon = Icons.Outlined.Call)
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
//    DeviceProfileScreen(PaddingValues(), rememberNavController())
    DeviceProfileComposable()
}
