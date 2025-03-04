package com.example.infone.repository

import com.example.infone.model.DataPoint

interface DataPointRepository {
    fun createTable()
    fun getDataPoints(): List<DataPoint>
    fun getDataPoints(ids: List<Int>): List<DataPoint>
    fun getDataPoint(id: Int): DataPoint?
    fun upsertDatapoint(id: Int, name: String, value: String)
}