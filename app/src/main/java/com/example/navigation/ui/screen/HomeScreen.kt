package com.example.navigation.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoToDetails: () -> Unit,
    onGoToSettings: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToDetails) {
            Text("Go to Details")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToSettings) {
            Text("Go to Settings")
        }
    }
}

