package com.behnamUix.mazejoo.view.navigation.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import cafe.adriel.voyager.core.screen.Screen
import com.behnamUix.mazejoo.R
import com.behnamUix.mazejoo.remote.ktor.model.Element
import com.behnamUix.mazejoo.utils.gpsChecker
import com.behnamUix.mazejoo.view.component.items.ItemCardComp
import com.behnamUix.mazejoo.view.component.map.NeshanMap
import com.behnamUix.mazejoo.view.viewmodel.NearbyCoffeeViewModel
import com.behnamUix.mazejoo.view.viewmodel.OverpassViewModel
import com.behnamUix.mazejoo.view.viewmodel.SearchPlaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object HomeSc : Screen {

    enum class PlaceType {
        RESTAURANT,
        COFFEE
    }

    @SuppressLint("WrongConstant")

    @Composable
    override fun Content() {

        // State variables
        var selectedMarker by remember { mutableStateOf<Pair<Double, Double>?>(null) }

        var location by remember { mutableStateOf(Pair(0.0, 0.0)) }
        var locationLoaded by remember { mutableStateOf(false) }
        var targetLocation by remember { mutableStateOf(false) }
        var showMarker by remember { mutableStateOf(false) }
        var gpsStatus by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val view = LocalView.current

        val searchVM: SearchPlaceViewModel = koinViewModel()
        val restaurantVM: OverpassViewModel = koinViewModel()
        val coffeeVM: NearbyCoffeeViewModel = koinViewModel()

        val restaurant by restaurantVM.overpass.collectAsState()
        val coffee by coffeeVM.coffee.collectAsState()

        var translateX = remember { androidx.compose.animation.core.Animatable(200f) }
        var translateXTarget = 0f

        // انتخاب فعلی
        var selectedType by remember { mutableStateOf(PlaceType.COFFEE) }

        // تنظیم نمایش نوار وضعیت
        LaunchedEffect(Unit) {
            if (context is ComponentActivity) {
                val controller = WindowInsetsControllerCompat(context.window, view)
                controller.show(
                    android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
                )
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
            gpsStatus = gpsChecker(ctx = context)
            if (!gpsStatus) {
                Toast.makeText(
                    context,
                    " لطفا جی پی اس دستگاه خود را روشن کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
            delay(6000)
            launch {
                translateX.animateTo(
                    translateXTarget, animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessVeryLow,
                        visibilityThreshold = 0.01f
                    )
                )
            }

        }
        LaunchedEffect(location) {
            if (location.first != 0.0 && location.second != 0.0) {
                restaurantVM.loadOverpassPlaceByPose(location.first, location.second)
                coffeeVM.loadNearbyCoffeePlaceByPose(location.first, location.second)
            }
        }

        // UI
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // نقشه
            NeshanMap(
                selectedMarker = selectedMarker, // 👈 اضافه شد
                ontargetLocationChnage = { targetLocation = it },
                targetLocation = targetLocation,
                lat = location.first,
                lon = location.second,
                onLocationChange = {
                    location = it
                },
                onLocationChangeCofee = {

                }
            )

            // دکمه مکان فعلی
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                TargetLocationFab(translateX) { targetLocation = true }
            }
            // فیلتر بالا
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RestaurantFilterChip(
                        selected = selectedType == PlaceType.RESTAURANT,
                        onClick = { selectedType = PlaceType.RESTAURANT }
                    )
                    CoffeeFilterChip(
                        selected = selectedType == PlaceType.COFFEE,
                        onClick = { selectedType = PlaceType.COFFEE }
                    )
                }
            }

            // لیست پایین
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                when (selectedType) {
                    PlaceType.RESTAURANT -> RestaurantListCard(
                        restaurant = restaurant,
                        location = location,
                        onItemClick = { selectedMarker = it }
                    )

                    PlaceType.COFFEE -> CoffeeListCard(
                        coffee = coffee,
                        location = location,
                        onItemClick = { selectedMarker = it }
                    )
                }
            }
        }
    }


    // ---------- Composables ----------
    @Composable
    fun RestaurantListCard(
        restaurant: List<Element>,
        location: Pair<Double, Double>,
        onItemClick: (Pair<Double, Double>) -> Unit
    ) {
        if (restaurant.isEmpty()) {
            LoadingIndicator()
        } else {
            LazyRow(

                horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                items(restaurant) { item ->
                    ItemCardComp(
                        item = item, location = location, isCaffee = false,
                        onClick = {
                            item.lat?.let { lat ->
                                item.lon?.let { lon ->
                                    onItemClick(Pair(lat, lon))
                                }
                            }
                        })
                }
            }
        }
    }

    @Composable
    fun CoffeeListCard(
        coffee: List<Element>,
        location: Pair<Double, Double>,
        onItemClick: (Pair<Double, Double>) -> Unit
    ) {
        if (coffee.isEmpty()) {
            LoadingIndicator()
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                items(coffee) { item ->
                    ItemCardComp(item = item, location = location, isCaffee = true, onClick = {
                        item.lat?.let { lat ->
                            item.lon?.let { lon ->
                                onItemClick(Pair(lat, lon))
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun LoadingIndicator() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(trackColor = MaterialTheme.colorScheme.background)
                Text(
                    "در حال بروزرسانی اطلاعات...",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    fun RestaurantFilterChip(selected: Boolean, onClick: () -> Unit) {
        FilterChip(
            shape = RoundedCornerShape(8.dp),
            selected = selected,
            onClick = onClick,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.onSurface
            ),
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "رستوران‌های نزدیک",
                        color = if (selected) Color.Black else Color.LightGray,
                        modifier = Modifier.padding(6.dp)
                    )
                    Icon(
                        modifier = Modifier.size(24.dp),

                        painter = painterResource(R.drawable.icon_spoon),
                        contentDescription = "",
                        tint = if (selected) Color.Black else Color.LightGray
                    )
                }
            }
        )
    }

    @Composable
    fun CoffeeFilterChip(selected: Boolean, onClick: () -> Unit) {
        FilterChip(
            shape = RoundedCornerShape(8.dp),
            selected = selected,
            onClick = onClick,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.onSurface
            ),
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,

                        text = "کافه های نزدیک",
                        color = if (selected) Color.Black else Color.LightGray,
                        modifier = Modifier.padding(6.dp)
                    )
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.icon_cup),
                        contentDescription = "",
                        tint = if (selected) Color.Black else Color.LightGray
                    )
                }
            }
        )
    }
    @Composable
    fun TargetLocationFab(
        translateX: androidx.compose.animation.core.Animatable<Float, *>,
        onClick: () -> Unit
    ) {
        SmallFloatingActionButton(
            modifier = Modifier
                .offset(x = translateX.value.dp, y = 0.dp)
                .padding(16.dp)
                .size(48.dp),
            containerColor = MaterialTheme.colorScheme.onSurface,
            onClick = onClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_gps),
                contentDescription = "مکان فعلی"
            )
        }
    }

}











