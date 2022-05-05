package com.mclowicz.mcorganizer.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mclowicz.mcorganizer.domain.model.Spot

@Composable
fun SpotsUI(
    spots: MutableList<Spot>,
    onSpotClicked: (Spot) -> Unit,
    title: String = ""
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .clickable(
                onClick = {

                }
            ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        elevation = 8.dp
    ) {
        Column {
            Row() {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    color = MaterialTheme.colors.primary
                )
            }
            if (spots.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Spots does not exists.",
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Normal
                    )
                }
            } else {
                LazyRow() {
                    items(items = spots) { spot ->
                        Card(
                            modifier = Modifier
                                .width(230.dp)
                                .height(120.dp)
                                .padding(16.dp)
                                .clickable(
                                    onClick = {
                                        onSpotClicked.invoke(spot)
                                    }
                                ),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                            elevation = 8.dp,
                            backgroundColor = MaterialTheme.colors.primary
                        ) {
                            Column(
                                modifier = Modifier.height(160.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = spot.name,
                                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = convertLongToTime(spot.timeStamp),
                                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}