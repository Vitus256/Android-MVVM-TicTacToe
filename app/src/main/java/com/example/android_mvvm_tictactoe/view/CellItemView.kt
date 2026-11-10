
package com.example.android_mvvm_tictactoe.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

import com.example.android_mvvm_tictactoe.model.CellModel

@Composable
fun CellItemView(
    cell: CellModel,
    onCellClick: () -> Unit,
    isGameOver: Boolean,
    modifier: Modifier = Modifier
) {
    val canClick = cell.symbol.isEmpty() && !isGameOver

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(enabled = canClick) {
                onCellClick()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cell.symbol,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = if (cell.symbol == "X") {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.tertiary
                }
            )
        }
    }
}
