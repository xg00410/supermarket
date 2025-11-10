package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.supermarket.R

@Composable
fun AppIcon(width: Int = 280, height: Int = 60) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "BAROGAKI Icon",
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
    )
}
