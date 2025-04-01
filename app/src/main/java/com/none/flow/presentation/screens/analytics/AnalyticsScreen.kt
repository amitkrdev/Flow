package com.none.flow.presentation.screens.analytics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.none.flow.R

@Preview(showBackground = true)
@Composable
fun PreviewAnalyticsScreen() {
    AnalyticsScreen(
        onCancel = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onCancel: () -> Unit
) {
    val contentState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            AnalyticsAppBar(
                onCancel = onCancel,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        AnalyticsContent(
            contentState = contentState,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsAppBar(
    onCancel: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.feature_analytics)) },
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.action_cancel)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun AnalyticsContent(

    contentState: LazyListState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            state = contentState,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp)
        ) {
            item {
                OutlinedCard(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(all = 20.dp)) {

                    }
                }
            }
        }
    }
}
