package com.android.algorithms.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.algorithms.screens.components.AlgorithmItem
import com.android.algorithms.viewmodel.AlgorithmListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmListScreen(
    navController: NavHostController,
    shouldUpdate: Boolean = false
) {
    val algorithmListViewModel = hiltViewModel<AlgorithmListViewModel>()
    val algorithmListState = algorithmListViewModel.algorithmListState.collectAsState().value

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(shouldUpdate || pullToRefreshState.isRefreshing) {
        if (shouldUpdate) algorithmListViewModel.update()
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(shouldUpdate || pullToRefreshState.isRefreshing) {
            algorithmListViewModel.update()
            pullToRefreshState.endRefresh()
        }
    }

    if (algorithmListState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (algorithmListState.errorMessage.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize().nestedScroll(pullToRefreshState.nestedScrollConnection)
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
                    text = "Error: ${algorithmListState.errorMessage}.",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            PullToRefreshContainer(state = pullToRefreshState, modifier = Modifier.align(Alignment.TopCenter))
        }
    } else {
        Box(Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (!pullToRefreshState.isRefreshing) {
                    items(algorithmListState.algorithmList.size) { index ->
                        AlgorithmItem(
                            algorithm = algorithmListState.algorithmList[index],
                            navController = navController
                        )
                    }
                }
            }
            PullToRefreshContainer(state = pullToRefreshState, modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}