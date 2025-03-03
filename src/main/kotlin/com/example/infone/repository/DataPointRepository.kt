package com.example.infone.repository

import com.example.infone.model.DataPoint

interface DataPointRepository {
    fun createTable()
    fun getDataPoints(): List<DataPoint>
    fun getDataPoint(id: String): DataPoint?
    fun upsertDatapoint(id: String, name: String, value: String)
}