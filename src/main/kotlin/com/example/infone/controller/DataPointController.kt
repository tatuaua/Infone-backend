package com.example.infone.controller

import com.example.infone.model.DataPoint
import com.example.infone.repository.DataPointRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DataPointController(private val dataPointRepository: DataPointRepository) {

    private val logger: Logger = LoggerFactory.getLogger(DataPointController::class.java)

    @GetMapping("/datapoints")
    fun getDataPoints(): List<DataPoint> {
        logger.info("Fetching all data points")
        val dataPoints = dataPointRepository.getDataPoints()
        logger.info("Retrieved {} data points", dataPoints.size)
        return dataPoints
    }

    @GetMapping("/datapoints/{ids}")
    fun getDataPoints(@PathVariable ids: List<Int>): List<DataPoint> {
        logger.info("Fetching data points with ids: {}", ids)
        val dataPoints = dataPointRepository.getDataPoints(ids)
        logger.info("Retrieved {} data points", dataPoints.size)
        return dataPoints
    }
}