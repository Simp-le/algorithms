package com.android.algorithms.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.algorithms.R
import com.android.algorithms.screens.components.DetailsInput
import com.android.algorithms.screens.components.DetailsOutputs
import com.android.algorithms.viewmodel.AlgorithmDetailsViewModel

@Composable
fun AlgorithmDetailsScreen(
    navController: NavHostController) {
    val context = LocalContext.current
    val detailsViewModel = hiltViewModel<AlgorithmDetailsViewModel>()
    val detailsState = detailsViewModel.algorithmDetailsState.collectAsState().value

    BackHandler {
        navController.currentBackStackEntry?.savedStateHandle?.set("isChanged", detailsState.isChanged)
        if(detailsState.isChanged) {
            navController.navigate(Screen.AlgorithmList.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
        else { navController.popBackStack() }
    }

    if (detailsState.isLoading) { // or detailsState.algorithmDetails == null as in ListScreen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (detailsState.errorMessage.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Error Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 30.dp)
                )
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Couldn't load page.",
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Error: ${detailsState.errorMessage}.",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .weight(3.0F),
                    textAlign = TextAlign.Center,
                    text = detailsState.algorithmDetailsResult.title,
                    style = MaterialTheme.typography.titleLarge
                )
                if (!detailsState.isDownloaded) {
                    IconButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(1.0F),
                        onClick = { detailsViewModel.actionButtonClick(context) }) {
                        Icon(
                            painterResource(R.drawable.rounded_download_for_offline),
                            "Icon button.",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(1.0F),
                        onClick = { detailsViewModel.actionButton2Click(context) }) {
                        Icon(
                            painterResource(R.drawable.rounded_download_done),
                            "Icon button.",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Text(
                text = detailsState.algorithmDetailsResult.description,
                textAlign = TextAlign.Justify
            )

            Spacer(Modifier.height(16.dp))

            DetailsInput(detailsState.algorithmDetailsResult.parameters)
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { detailsViewModel.buttonClick(context) }
            ) {
                // TODO: Resource text
                Text(text = "EXECUTE")
            }
            DetailsOutputs(detailsState.algorithmDetailsResult.outputs)
        }
    }
}