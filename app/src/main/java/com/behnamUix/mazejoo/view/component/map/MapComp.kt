package com.behnamUix.mazejoo.view.component.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.behnamUix.mazejoo.R
import com.behnamUix.mazejoo.utils.RouteThrottleManager
import com.carto.graphics.Color
import com.carto.styles.LineJoinType
import com.carto.styles.LineStyle
import com.carto.styles.LineStyleBuilder
import com.carto.styles.MarkerStyleBuilder
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import org.neshan.common.model.LatLng
import org.neshan.common.utils.PolylineEncoding
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker
import org.neshan.mapsdk.model.Polyline
import org.neshan.mapsdk.style.NeshanMapStyle
import org.neshan.servicessdk.direction.NeshanDirection
import org.neshan.servicessdk.direction.model.NeshanDirectionResult
import org.neshan.servicessdk.direction.model.Route
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun NeshanMap(
    selectedMarker: Pair<Double, Double>? = null,
    ontargetLocationChnage: (Boolean) -> Unit,
    targetLocation: Boolean,
    lon: Double = 0.0,
    lat: Double = 0.0,
    showMarker: Boolean = false,
    onLocationChange: (Pair<Double, Double>) -> Unit = {},
    onLocationChangeCofee: (Pair<Double, Double>) -> Unit = {}

) {
    val ctx = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var marker: Marker? by remember { mutableStateOf(null) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var lastRouteUserLocation by remember { mutableStateOf<LatLng?>(null) }
    var lastRouteDestination by remember { mutableStateOf<LatLng?>(null) }

    // گرفتن پرمیژن
    RequestLocationPermission {
        permissionGranted = true
    }

    // تابع افزودن مارکر کاربر
    fun addUserMarker(map: MapView, loc: LatLng) {
        marker?.let { map.removeMarker(it) }


        val androidBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.ico_locate)
        val neshanBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidBitmap)
        val markerStyle = MarkerStyleBuilder().apply {
            size = 32f
            anchorPointX = 0.5f
            anchorPointY = 1f
            bitmap = neshanBitmap
        }.buildStyle()

        marker = Marker(loc, markerStyle)
        map.addMarker(marker)

    }
     fun distanceBetween(a: LatLng, b: LatLng): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
        return results[0]
    }
    // تابع شروع لوکیشن
    fun startLocationUpdates(mapView: MapView) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        val settingsClient = LocationServices.getSettingsClient(ctx)

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000L // هر ۲ ثانیه آپدیت
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                if (
                    ActivityCompat.checkSelfPermission(
                        ctx, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        ctx, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val callback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            val location = locationResult.lastLocation ?: return
                            userLocation = location

                            val latLng = LatLng(location.latitude, location.longitude)
                            //addUserMarker(mapView, latLng)
                            onLocationChange(Pair(location.latitude, location.longitude))
                            onLocationChangeCofee(Pair(location.latitude, location.longitude))

                            Log.d(
                                "NeshanMap",
                                "User location: ${location.latitude}, ${location.longitude}"
                            )
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        callback,
                        null
                    )
                }
            }
    }

    if (permissionGranted) {
        AndroidView(
            factory = { context ->
                val mapView = MapView(context)
                mapView.setZoom(8f, 0.5f)
                mapView.setTrafficEnabled(true)
                mapView.setMyLocationEnabled(true)
                mapView.setMapStyle(NeshanMapStyle.NESHAN)
                mapView.setPoiEnabled(true)
                // شروع گرفتن لوکیشن واقعی کاربر
                startLocationUpdates(mapView)
                mapView
            },
            update = { mapView ->

                selectedMarker?.let { (lat, lon) ->
                    addMarker(mapView, lat, lon, marker, ctx)

                    userLocation?.let { location ->
                        val userPoint = LatLng(location.latitude, location.longitude)
                        val destPoint = LatLng(lat, lon)

                        // چک کن آیا مسیر جدید باید کشیده بشه
                        if (RouteThrottleManager.shouldCallRoute(userPoint, destPoint)) {
                            neshanRoutingApi(mapView, userPoint, destPoint)
                            RouteThrottleManager.updateRoute(userPoint, destPoint)
                        }
                    }
                }

                if (targetLocation) {
                    userLocation?.let { location ->
                        if (targetLocation) {
                            mapView.moveCamera(LatLng(location.latitude, location.longitude), 36f)
                            mapView.setZoom(8f, 0.1f)
                            ontargetLocationChnage(false)
                        }
                    }
                }

            }
        )
    }
}

private val routePolylines = mutableListOf<Polyline>()
private fun neshanRoutingApi(mapView: MapView, start: LatLng, end: LatLng) {
    Log.e("NeshanRoute", "→ neshanRoutingApi CALLED")

    // حذف همه پلی‌لاین‌های قبلی
    routePolylines.forEach { mapView.removePolyline(it) }
    routePolylines.clear()
    NeshanDirection.Builder(
        "service.6b051814e7da409a9021c8cea0c436bd",
        start,
        end
    )
        .build().call(object : Callback<NeshanDirectionResult?> {
            override fun onResponse(
                call: Call<NeshanDirectionResult?>,
                response: Response<NeshanDirectionResult?>
            ) {
                if (response.body() != null && response.body()!!.routes.isNotEmpty()) {
                    val route: Route = response.body()!!.routes[0]
                    val routePoints =
                        PolylineEncoding.decode(route.overviewPolyline.encodedPolyline)
                    Log.d("NishanRoute", "Decoded points: ${routePoints.size}")
                    Log.d("NeshanRoute", "routes size: ${response.body()?.routes?.size}")

                    if (routePoints.isNotEmpty()) {
                        val polyline = Polyline(ArrayList(routePoints), getLineStyle())
                        mapView.addPolyline(polyline)

                        // 🔹 پلی‌لاین جدید رو ذخیره کن تا بعداً حذفش کنیم
                        routePolylines.add(polyline)
                    }
                }
            }

            override fun onFailure(call: Call<NeshanDirectionResult?>, t: Throwable) {
                Log.e("NeshanRouteError", "Failed: ${t.message}", t)
            }
        })
}

private fun getLineStyle(): LineStyle {
    val lineStCr = LineStyleBuilder()
    lineStCr.color = Color(0.toShort(), 0.toShort(), 0.toShort(), 255.toShort())
    lineStCr.width = 6f
    lineStCr.lineJoinType = LineJoinType.LINE_JOIN_TYPE_ROUND
    return lineStCr.buildStyle()
}

// افزودن مارکر معمولی (برای رستوران و غیره)
private val markers = mutableListOf<Marker>()

fun addMarker(
    mapView: MapView,
    lat: Double,
    lon: Double,
    marker: Marker?,
    ctx: Context
) {
    markers.forEach{mapView.removeMarker(it)}
    markers.clear()
    val androidBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.pin_place)
    val neshanBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidBitmap)
    val markerStyle = MarkerStyleBuilder().apply {
        size = 32f
        anchorPointX = 0.5f
        anchorPointY = 1f
        bitmap = neshanBitmap
    }.buildStyle()
    // افزودن مارکر جدید
    val newMarker = Marker(LatLng(lat, lon), markerStyle)
    mapView.addMarker(newMarker)
    markers.add(newMarker)


}

// پرمیژن مکان
@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit = {}) {
    var permissionRequested by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            onPermissionGranted()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionRequested) {
            permissionRequested = true
            kotlinx.coroutines.delay(300)
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}
