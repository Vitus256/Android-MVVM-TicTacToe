
package com.example.android_mvvm_tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.example.android_mvvm_tictactoe.model.BoardModel


class GameViewModel : ViewModel() {


    private val _board = MutableStateFlow(
        BoardModel.createInitialBoard()
    )


    val board: StateFlow<BoardModel> = _board.asStateFlow()


    fun onCellClick(position: Int) {
        _board.value = _board.value.makeMove(position)
    }


    fun restartGame() {
        _board.value = BoardModel.createInitialBoard()
    }
}
