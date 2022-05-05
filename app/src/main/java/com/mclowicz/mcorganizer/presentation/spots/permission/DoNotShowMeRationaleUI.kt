package com.mclowicz.mcorganizer.presentation.spots.permission

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mclowicz.mcorganizer.R
import com.mclowicz.mcorganizer.util.openSettings

@Composable
fun DoNotShowMeRationaleUI(
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_denied),
            contentDescription = "Denied image",
            modifier = Modifier.size(71.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = "Location permission has been denied. " +
                    "You can accept in settings for app to work properly."
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { context.openSettings() }) {
            Text(text = "Open Settings")
        }
    }
}