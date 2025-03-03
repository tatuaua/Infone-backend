package com.example.infone.repository

import com.example.infone.model.DataPoint

interface DataPointRepository {
    fun createTable()
    fun getDataPoints(): List<DataPoint>
    fun getDataPoint(name: String): DataPoint?
    fun upsertDatapoint(name: String, value: String)
}