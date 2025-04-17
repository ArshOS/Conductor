package com.park.conductor.presentation.attraction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.park.conductor.R
import com.park.conductor.common.utilities.Prefs

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
                RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
//            Text(
//                text = Prefs.getLogin()?.userInfo?.userName.toString(),
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Bold
//            )
            Spacer(modifier = Modifier.padding(30.dp))
            DrawerItem(label = "My Profile", icon = Icons.Outlined.Person)
            DrawerItemDivider()
            DrawerItem(label = "My Transactions", icon = Icons.AutoMirrored.Outlined.List)
            DrawerItemDivider()
            DrawerItem(label = "Settings", icon = Icons.Outlined.Settings)
            DrawerItemDivider()
            DrawerItem(label = "Privacy Policy", icon = Icons.Outlined.PrivacyTip)
            DrawerItemDivider()
            DrawerItem(label = "Logout", icon = Icons.Outlined.Logout)
            DrawerItemDivider()

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(R.drawable.logo_lda_black),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Lucknow Development Authority", style = MaterialTheme.typography.bodySmall)
        }

    }
}

@Composable
fun DrawerItemDivider() {
    HorizontalDivider(Modifier.padding(26.dp, 10.dp), color = Color.LightGray.copy(alpha = 0.5f))
}

@Composable
fun DrawerItem(label: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.padding(3.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun DrawerHeader(isCalledFromDrawer: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val employeeName: String? = Prefs.getLogin()?.userInfo?.userName

        Text(
            text = employeeName.toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    DrawerContent(rememberNavController(), rememberDrawerState(initialValue = DrawerValue.Open))
}
