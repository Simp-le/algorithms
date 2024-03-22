package com.android.algorithms.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.algorithms.data.model.details.data.DataElement

@Composable
fun DetailsOutputs(outputs: List<DataElement>) {
    outputs.forEach { dataElement ->
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(modifier = Modifier.padding(start = 16.dp), text = dataElement.title)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
            ) {
                val chunks = (dataElement.value?.toString() ?: " ").chunked(100)
                LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(chunks) { chunk ->
                        Text(
                            modifier = Modifier
                                .padding(vertical = 10.dp), text = chunk
                        )
                    }
                }
            }
        }
    }

}

