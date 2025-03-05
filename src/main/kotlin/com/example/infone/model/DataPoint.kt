package com.example.infone.model

data class DataPoint(
    val id: Int,
    val name: String,
    val value: String
) {
    init {
        require(name.length <= 25) { "Name must be at most 25 characters long" }
    }
}