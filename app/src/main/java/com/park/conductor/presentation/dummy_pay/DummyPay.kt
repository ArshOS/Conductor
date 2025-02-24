package com.park.conductor.presentation.dummy_pay

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.park.conductor.ezetap.EzeCordovaSampleActivity
import com.park.conductor.ezetap.EzeNativeSampleActivity
import com.park.conductor.ezetap.Setting
import com.park.conductor.ui.theme.Green40

@Composable
fun DummyPayComposable(
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "₹10.0",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Green40
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        ButtonGrid(totalButtons = 8, buttonsPerRow = 2, buttonData = buttonData)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Powered by ", color = Color.LightGray)
            Text(
                text = "INNOBLES",
                textDecoration = TextDecoration.Underline,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val context = LocalContext.current

            Button(
                onClick = {
                    val intent = Intent(context, EzeNativeSampleActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text("Native")
            }
            Spacer(modifier = Modifier.height(8.dp))
//            Button(onClick = { navigateTo(EzeCordovaSampleActivity::class.java) }, modifier = Modifier.fillMaxWidth()) {
//                Text("Cordova")
//            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(context, Setting::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Settings")
            }
        }
    }
}


@Composable
@Preview
private fun DefaultPreview() {
    DummyPayComposable(
        PaddingValues(),
        rememberNavController()
    )
}
