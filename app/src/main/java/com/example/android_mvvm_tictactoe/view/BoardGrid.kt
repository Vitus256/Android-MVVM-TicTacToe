
package com.example.android_mvvm_tictactoe.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.android_mvvm_tictactoe.model.CellModel

@Composable
fun BoardGrid(
    cells: List<CellModel>,
    isGameOver: Boolean,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = cells,
            key = { cell -> cell.id }
        ) { cell ->

            CellItemView(
                cell = cell,
                isGameOver = isGameOver,
                onCellClick = {
                    onCellClick(cell.position)
                }
            )
        }
    }
}
