package com.example.infone.model

import java.io.Serializable

data class DataPoint(
    var id: Int,
    var name: String,
    var value: String,
    var description: String
) : Serializable{
    init {
        require(name.length <= 25) { "Name must be at most 25 characters long" }
    }
}