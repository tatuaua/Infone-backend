package com.example.infone.service

import com.example.infone.repository.DataPointRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataPointService(
    @Autowired private val dataPointRepository: DataPointRepository
) {
    fun getDataPoints() = dataPointRepository.getDataPoints()
    fun getDataPoints(ids: List<Int>) = dataPointRepository.getDataPoints(ids)
    fun getDataPoint(id: Int) = dataPointRepository.getDataPoint(id)
    fun updateDataPoint(id: Int, name: String, value: String, description: String) = dataPointRepository.upsertDatapoint(id, name, value, description)
}