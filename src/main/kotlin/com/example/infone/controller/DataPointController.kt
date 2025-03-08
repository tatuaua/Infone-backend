package com.example.infone.controller

import com.example.infone.model.DataPoint
import com.example.infone.repository.DataPointRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse

@RestController
@RequestMapping("/api")
class DataPointController(private val dataPointRepository: DataPointRepository) {

    @Value("\${api.key}")
    private lateinit var apiKey: String
    private val logger: Logger = LoggerFactory.getLogger(DataPointController::class.java)

    @GetMapping("/datapoints/{ids}")
    fun getDataPoints(@RequestHeader("X-API-KEY") apiKey: String, @PathVariable ids: List<Int>): ResponseEntity<List<DataPoint>> {
        if (apiKey != this.apiKey) {
            logger.warn("Invalid API key: {}", apiKey)
            return ResponseEntity.status(401).build()
        }
        logger.info("Fetching data points by ids: {}", ids)
        val dataPoints = dataPointRepository.getDataPoints(ids)
        logger.info("Retrieved {} data points", dataPoints.size)
        return ResponseEntity.ok(dataPoints)
    }

    @GetMapping("/datapoints")
    fun getDataPoints(@RequestHeader("X-API-KEY") apiKey: String): ResponseEntity<List<DataPoint>> {
        if (apiKey != this.apiKey) {
            logger.warn("Invalid API key: {}", apiKey)
            return ResponseEntity.status(401).build()
        }
        logger.info("Fetching all data points")
        val dataPoints = dataPointRepository.getDataPoints()
        logger.info("Retrieved {} data points", dataPoints.size)
        return ResponseEntity.ok(dataPoints)
    }

}