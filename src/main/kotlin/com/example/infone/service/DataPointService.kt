package com.example.infone.service

import com.example.infone.repository.DataPointRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataPointService(
    @Autowired private val dataPointRepository: DataPointRepository
) {
    fun getDataPoints() = dataPointRepository.getDataPoints()
    fun getDataPoint(id: String) = dataPointRepository.getDataPoint(id)
    fun updateDataPoint(id: String, name: String, value: String) = dataPointRepository.upsertDatapoint(id, name, value)
}