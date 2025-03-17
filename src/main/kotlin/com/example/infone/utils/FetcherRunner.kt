package com.example.infone.utils

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.service.DataPointService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class FetcherRunner(
    @Autowired private val dataPointService: DataPointService,
    @Autowired private val dataPointFetchers: List<DataPointFetcher>
) {
    private val logger: Logger = LoggerFactory.getLogger(FetcherRunner::class.java)
    fun run() {
        dataPointFetchers.forEach { fetcher ->
            val dataPoint = fetcher.fetch()
            dataPointService.updateDataPoint(dataPoint.id, dataPoint.name, dataPoint.value, dataPoint.description)
        }
    }

    suspend fun runAsync() = coroutineScope {
        logger.info("Running ${dataPointFetchers.size} fetchers")

        // Use supervisorScope to isolate failures in child coroutines
        val deferredResults: List<Deferred<DataPoint?>> = supervisorScope {
            dataPointFetchers.map { item ->
                async(Dispatchers.IO) {
                    try {
                        item.fetch()
                    } catch (e: Exception) {
                        logger.error("Failed to fetch data point from ${item}: ${e.message}")
                        null
                    }
                }
            }
        }

        val results = deferredResults.awaitAll()
        logger.info("Fetched ${results.size} data points (including failures)")

        val successfulResults = results.filterNotNull()
        successfulResults.map { dataPoint ->
            dataPointService.updateDataPoint(
                dataPoint.id,
                dataPoint.name,
                dataPoint.value,
                dataPoint.description
            )
        }

        logger.info("Updated ${successfulResults.size} data points")
    }
}