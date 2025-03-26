package com.park.conductor.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.park.conductor.R

@Composable
fun NationalityTag(labels: List<String>, painterResources: List<Painter>? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp)
    ) {
        if (!painterResources.isNullOrEmpty() && painterResources.size == 1 ) {
            Icon(painter = painterResources[0], contentDescription = null)
        }

        Spacer(modifier = Modifier.padding(2.dp))

        var label: String = ""

        if (labels.size > 1) {
            labels.forEachIndexed { index, s ->
                label = if (index == 0) {
                    s
                } else {
                    "$label & $s"
                }
            }
        } else {
            label = labels[0]
        }
        label = "$label Visitors"

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    NationalityTag(
        listOf("Indian", "Foreigner"),
        listOf(painterResource(id = R.drawable.ic_india), painterResource(id = R.drawable.ic_globe))
    )
}