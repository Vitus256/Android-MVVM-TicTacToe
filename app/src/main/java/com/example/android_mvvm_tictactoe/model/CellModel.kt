package com.example.android_mvvm_tictactoe.model

data class CellModel(
    val id: Int,
    val position: Int,
    val symbol: String = ""
)