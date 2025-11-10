package com.behnamUix.mazejoo.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.behnamUix.mazejoo.R
import com.behnamUix.mazejoo.utils.gpsChecker
import com.behnamUix.mazejoo.view.navigation.home.HomeSc
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SplashSc1 : Screen {
    @SuppressLint("WrongConstant")
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val view = LocalView.current

        var scaleAnim = remember { Animatable(0f) }
        var scaleTarget = 0.6f

        var translateX = remember { Animatable(-400f) }
        var translateXTarget = 0f

        var nav = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            if (context is ComponentActivity) {
                val controller = WindowInsetsControllerCompat(context.window, view)
                controller.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            launch {
                scaleAnim.animateTo(
                    targetValue = scaleTarget,
                    animationSpec = spring(
                        dampingRatio = 0.3f,
                        stiffness = Spring.StiffnessVeryLow,
                        visibilityThreshold = 0.01f
                    )
                )
            }
            delay(2000)
            launch {
                translateX.animateTo(
                    targetValue = translateXTarget,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessVeryLow,
                        visibilityThreshold = 0.01f
                    )
                )
            }
            delay(2000)

            nav.push(SplashSc2)


        }

        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.background),
                    contentDescription = "",
                    contentScale = ContentScale.Crop

                )
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Card(
                        elevation = CardDefaults.elevatedCardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 80.dp,
                            bottomEnd = 80.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(scaleAnim.value)
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Mazejoo",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                Text(
                                    text = "مزه جو",
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }

                    }

                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier.offset(x = translateX.value.dp, y = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "طراحی و توسعه:BehnamUix",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = "V 1.0.0",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),

                            style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

        }

    }

}

object SplashSc2 : Screen {
    @SuppressLint("WrongConstant")
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val view = LocalView.current
        var gpsStatus by remember { mutableStateOf(false) }

        var rotateAnim = remember { Animatable(0f) }
        var rotateTarget = 360f


        var nav = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            if (context is ComponentActivity) {
                val controller = WindowInsetsControllerCompat(context.window, view)
                controller.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            launch {
                rotateAnim.animateTo(
                    targetValue = rotateTarget,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 4000,
                            easing = LinearEasing
                        ), // سرعت و روانی
                        repeatMode = RepeatMode.Restart // یا Reverse برای چرخش رفت و برگشتی
                    )

                )
            }

        }

        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.background2),
                    contentDescription = "",
                    contentScale = ContentScale.Crop

                )

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(

                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = "https://static.vecteezy.com/system/resources/thumbnails/031/415/218/small/top-view-delicious-food-plate-on-a-black-background-ai-generated-photo.jpg",
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .rotate(rotateAnim.value)
                                .clip(CircleShape)
                                .size(300.dp)

                        )
                        Text("مزه جو", style = MaterialTheme.typography.titleLarge)
                        Text(

                            textAlign = TextAlign.Center,
                            text = " \n همراه شما در کشف طعم های تازه؛" +
                                    " رستورانها و کافه های اطرافت، تنها با یک نگاه!",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .alpha(0.7f)
                                .fillMaxWidth(0.8f)
                        )
                        Spacer(Modifier.height(48.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.alpha(0.5f)) {
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                        }
                        OutlinedButton(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(0.8f),
                            onClick = {
                                gpsStatus=gpsChecker(ctx = context)
                                if (!gpsStatus) {
                                    Toast.makeText(
                                        context,
                                        " لطفا جی پی اس دستگاه خود را روشن کنید",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Hawk.put("firstRun",false)
                                    nav.push(HomeSc)
                                }
                            }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(


                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    text = " شروع کنیم ",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Icon(
                                    painter = painterResource(R.drawable.icon_gps),
                                    contentDescription = ""
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.alpha(0.5f)) {
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_cup),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)

                            )
                            Icon(
                                painter = painterResource(R.drawable.icon_spoon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)

                            )
                        }

                    }
                }
            }
        }

    }

}
