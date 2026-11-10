
package com.example.android_mvvm_tictactoe.model


data class BoardModel(
    val cells: List<CellModel> = emptyList(),
    val currentPlayer: String = "X",
    val winner: String? = null,
    val isDraw: Boolean = false,
    val isGameOver: Boolean = false
) {

    companion object {


        fun createInitialBoard(): BoardModel {
            val initialCells = (0 until 9).map { position ->
                CellModel(
                    id = position,
                    position = position
                )
            }

            return BoardModel(cells = initialCells)
        }
    }


    fun makeMove(position: Int): BoardModel {


        if (isGameOver) return this

        if (position !in cells.indices) return this

        if (cells[position].symbol.isNotEmpty()) return this

        val updatedCells = cells.map { cell ->
            if (cell.position == position) {
                cell.copy(symbol = currentPlayer)
            } else {
                cell
            }
        }

        val updatedBoard = copy(cells = updatedCells)

        val checkedBoard = updatedBoard.checkWinner()

        if (checkedBoard.isGameOver) return checkedBoard

        val nextPlayer = if (currentPlayer == "X") "O" else "X"

        return checkedBoard.copy(currentPlayer = nextPlayer)
    }

    fun checkWinner(): BoardModel {

        val winningCombinations = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        for (combination in winningCombinations) {

            val symbol = cells[combination[0]].symbol

            if (
                symbol.isNotEmpty() &&
                combination.all { cells[it].symbol == symbol }
            ) {
                return copy(
                    winner = symbol,
                    isGameOver = true
                )
            }
        }

        if (cells.all { it.symbol.isNotEmpty() }) {
            return copy(
                isDraw = true,
                isGameOver = true
            )
        }

        return this
    }
}
