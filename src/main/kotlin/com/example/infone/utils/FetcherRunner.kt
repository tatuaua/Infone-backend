package com.example.infone.utils

import com.example.infone.model.DataPointFetcher
import com.example.infone.service.DataPointService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

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
}