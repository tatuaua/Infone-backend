package com.example.infone.model

import java.io.Serializable

data class DataPoint(
    val id: Int,
    val name: String,
    val value: String,
    val description: String
) : Serializable{
    init {
        require(name.length <= 25) { "Name must be at most 25 characters long" }
    }
}