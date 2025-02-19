package com.park.conductor.common.components

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedScreen() {
    var isClicked by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            scale.animateTo(1.1f, animationSpec = tween(200, easing = FastOutSlowInEasing))
            scale.animateTo(1f, animationSpec = tween(200, easing = FastOutSlowInEasing))
            isClicked = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value) // Apply scale animation
    ) {
        Icon(imageVector = Icons.Rounded.Person, contentDescription = null, Modifier.size(100.dp))
        Text(
            text = "Conductor",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )
        Button(
            onClick = { isClicked = true }, // Trigger animation on click
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.Black),
        ) {
            Row(
                modifier = Modifier.padding(50.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Pay",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    }
}

@Composable
fun LiquidEffectScreen() {
    var isClicked by remember { mutableStateOf(false) }
    val rippleRadius = remember { Animatable(0f) }
    val rippleAlpha = remember { Animatable(1f) }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            rippleRadius.snapTo(0f)
            rippleAlpha.snapTo(1f)
            rippleRadius.animateTo(500f, animationSpec = tween(600, easing = FastOutSlowInEasing)) // Expands outward
            rippleAlpha.animateTo(0f, animationSpec = tween(600, easing = FastOutSlowInEasing)) // Fades out
            isClicked = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Blue.copy(alpha = rippleAlpha.value),
                radius = rippleRadius.value,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(imageVector = Icons.Rounded.Person, contentDescription = null, Modifier.size(100.dp))
            Text(
                text = "Conductor",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )
            Button(
                onClick = { isClicked = true }, // Trigger ripple effect
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
            ) {
                Row(
                    modifier = Modifier.padding(50.dp, 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Pay",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive
                    )
                }
            }
        }
    }
}



@Composable
fun RippleAnimationScreen() {
    val context = LocalContext.current
    val jsonString = remember { loadJsonFromAssets(context, "ripple.json") } // Load JSON from assets

    var isPlaying by remember { mutableStateOf(false) } // Track animation state
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(jsonString))
    val coroutineScope = rememberCoroutineScope()

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = isPlaying, // Play only when clicked
        restartOnPlay = true,   // Restart animation on each click
        iterations = 1          // Play only once
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) { // Show ripple only when active
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    isPlaying = true // Trigger animation
                    coroutineScope.launch {
                        delay(1000) // Wait for animation to complete
                        isPlaying = false // Hide animation after fading out
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("Pay")
            }
        }
    }
}

// Function to Load Lottie JSON from Assets
fun loadJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

@Composable
fun RippleAnywhereScreen() {
    val context = LocalContext.current
    val jsonString = remember { loadJsonFromAssets(context, "ripple.json") }

    var isPlaying by remember { mutableStateOf(false) } // Track animation state
    var touchPosition by remember { mutableStateOf(Offset.Zero) } // Store touch position
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(jsonString))
    val coroutineScope = rememberCoroutineScope()

    val rippleSize = 1000.dp
    val density = LocalDensity.current

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = isPlaying,
        restartOnPlay = true,
        iterations = 1
    )

    // Detect tap gestures and store the touch position
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    touchPosition = offset
                    isPlaying = true // Start animation at touch location
                    coroutineScope.launch {
                        delay(1000) // Wait for animation to complete
                        isPlaying = false // Hide animation
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Log touch position and ripple center
        Log.d("RippleEffect", "Touch Position: ${touchPosition.x}, ${touchPosition.y}")

        // Calculate the ripple center
        val rippleCenterX = with(density) { touchPosition.x }
        val rippleCenterY = with(density) { touchPosition.y }

        // Log the ripple center
        Log.d("RippleEffect", "Ripple Center: $rippleCenterX, $rippleCenterY")

        // Show the ripple animation at the touch position
        if (isPlaying) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(rippleSize)
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Rounded.Person, contentDescription = null, Modifier.size(100.dp), tint = Color.White)
        Text(
            text = "Conductor",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Button(
            onClick = {  }, // Trigger animation on click
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.White),
        ) {
            Row(
                modifier = Modifier.padding(50.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Pay",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    }
}

