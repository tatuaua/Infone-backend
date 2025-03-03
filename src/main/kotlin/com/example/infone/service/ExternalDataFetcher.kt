package com.example.infone.service

import com.example.infone.model.DataPointFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExternalDataFetcher(
    @Autowired private val dataPointService: DataPointService,
    @Autowired private val dataPointFetchers: List<DataPointFetcher>
) {
    fun run() {
        dataPointFetchers.forEach { fetcher ->
            val dataPoint = fetcher.fetch()
            dataPointService.updateDataPoint(dataPoint.id, dataPoint.name, dataPoint.value)
        }
    }
}