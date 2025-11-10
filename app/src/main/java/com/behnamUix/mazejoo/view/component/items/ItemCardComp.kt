package com.behnamUix.mazejoo.view.component.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.behnamUix.mazejoo.R
import com.behnamUix.mazejoo.remote.ktor.model.Element
import com.behnamUix.mazejoo.utils.calculateDistance
import com.behnamUix.mazejoo.utils.calculateTime

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url, // ✅ مستقیم URL
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ItemCardComp(
    item: Element,
    location: Pair<Double, Double>,
    isCaffee: Boolean,
    onClick: () -> Unit
) {
    Card(

        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface)
    ) {
        val name = item.tags?.name ?: "بدون نام"
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isCaffee) {
                NetworkImage(
                    url = "https://previews.123rf.com/images/mingirov/mingirov2008/mingirov200801217/153019834-black-cup-of-coffee-shop-with-free-wifi-zone-icon-isolated-on-white-background-internet-connection.jpg",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            } else {
                NetworkImage(
                    url = "https://thumbs.dreamstime.com/b/black-crossed-fork-knife-plate-icon-isolated-white-background-restaurant-symbol-yellow-speech-bubble-symbol-vector-black-202140317.jpg",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                if (isCaffee) {
                    Text(
                        maxLines = 2,
                        text = if ("کافه" in name) name else "کافه $name",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                } else {
                    Text(
                        maxLines = 2,

                        text = item.tags?.name ?: "",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )


                }

                var distance = calculateDistance(
                    lat1 = item.lat ?: 0.0, lon1 = item.lon ?: 0.0,
                    lat2 = location.first, lon2 = location.second
                )

                Card(

                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onError.copy(
                            alpha = 0.2f
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        text = " فاصله از شما:${distance.toInt()}متر ",
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,

                        )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = calculateTime(distance),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Icon(
                        painter = painterResource(R.drawable.icon_time2),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),

                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { onClick() }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "کجاست؟",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(R.drawable.icon_gps),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }


        }

    }
}


