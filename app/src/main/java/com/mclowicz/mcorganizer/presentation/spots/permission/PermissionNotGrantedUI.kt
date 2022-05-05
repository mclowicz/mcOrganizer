package com.mclowicz.mcorganizer.presentation.spots.permission

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mclowicz.mcorganizer.R

@Composable
fun PermissionNotGrantedUI(
    onYesClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_privacy),
            contentDescription = "Permission image",
            modifier = Modifier.size(71.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Location permission",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This app require you location permission. " +
                    "Please grant your permission and enjoy your adventure.",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = onYesClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "OK")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "No")
            }
        }
    }
}