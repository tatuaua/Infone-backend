package com.example.infone.utils

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.service.DataPointService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlinx.coroutines.*

@Component
class FetcherRunner(
    @Autowired private val dataPointService: DataPointService,
    @Autowired private val dataPointFetchers: List<DataPointFetcher>
) {
    fun run() {
        dataPointFetchers.forEach { fetcher ->
            val dataPoint = fetcher.fetch()
            dataPointService.updateDataPoint(dataPoint.id, dataPoint.name, dataPoint.value, dataPoint.description)
        }
    }

    suspend fun runAsync() = coroutineScope {
        val deferredResults: List<Deferred<DataPoint>> = dataPointFetchers.map { item ->
            async(Dispatchers.IO) {
                item.fetch()
            }
        }

        val results = deferredResults.awaitAll()

        results.map { dataPoint ->
            dataPointService.updateDataPoint(dataPoint.id, dataPoint.name, dataPoint.value, dataPoint.description)}
    }
}