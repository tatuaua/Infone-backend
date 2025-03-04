package com.example.infone.model

interface DataPointFetcher {

    companion object {
        private var idCounter = 0
        fun getNextId(): Int {
            return idCounter++
        }
    }

    fun fetch(): DataPoint
}