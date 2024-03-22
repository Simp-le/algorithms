package com.android.algorithms.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.algorithms.data.model.details.data.DataElement

@Composable
fun DetailsInput(parameters: List<DataElement>) {

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (parameter in parameters) {
            var valueState by remember { mutableStateOf(parameter.value) }
            val updateValue: (Any) -> Unit = { newValue ->
                parameter.value = newValue
                valueState = newValue
            }

            if (parameter.dataShape == "scalar" && parameter.dataType == "bool") {
                parameter.value = parameter.value ?: false
                DetailsCheckBox(parameter, valueState, updateValue)
            } else {
                DetailsTextField(parameter, valueState, updateValue)
            }
        }
    }
}

@Composable
fun DetailsCheckBox(parameter: DataElement, valueState: Any?, updateValue: (Any) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = (valueState as? Boolean) ?: false,
            onCheckedChange = updateValue
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = parameter.title)
    }
}

@Composable
fun DetailsTextField(parameter: DataElement, valueState: Any?, updateValue: (Any) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = valueState?.toString() ?: "",
        onValueChange = updateValue,
        placeholder = {
            Text(
                text = parameter.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingText = {
            Text(
                text = parameter.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
        ),
    )

}
