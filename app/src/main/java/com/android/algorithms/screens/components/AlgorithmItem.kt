package com.android.algorithms.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.screens.Screen

@Composable
fun AlgorithmItem(
    navController: NavHostController,
    algorithm: Algorithm
) {
    Card(
        shape = RoundedCornerShape(38.dp),
        border = if (algorithm.isDownloaded) BorderStroke(
            2.dp, Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.inversePrimary
                )
            )
        )
        else null,
    ) {
        Box(
            modifier = Modifier
                .clickable { navController.navigate(Screen.Details.route + "/${algorithm.name}") }
                .padding(8.dp)
                .height(120.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = algorithm.title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 30.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}