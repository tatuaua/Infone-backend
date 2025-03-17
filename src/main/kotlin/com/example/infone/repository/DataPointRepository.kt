package com.example.infone.repository

import com.example.infone.model.DataPoint

interface DataPointRepository {
    fun dropAndCreateTable()
    fun getDataPoints(): List<DataPoint>
    fun getDataPoints(ids: List<Int>): List<DataPoint>
    fun upsertDatapoint(id: Int, name: String, value: String, description: String)
}