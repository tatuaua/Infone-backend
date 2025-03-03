package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.model.RequestHelper
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Component
class ElectricityPriceFinland(private val requestHelper: RequestHelper) : DataPointFetcher {

    private val mapper = ObjectMapper()

    private val url = "https://api.porssisahko.net/v1/latest-prices.json"

    override fun fetch(): DataPoint {
        val data = requestHelper.makeRequest(url, HttpMethod.GET, HttpHeaders(), null)

        val json = mapper.readTree(data)
        val pricesArray = json.get("prices")

        val prices = pricesArray
            .map { priceNode ->
                priceNode.get("price").asDouble() to priceNode.get("startDate").asText()
            }

        val highestPriceData = prices.maxByOrNull { it.first } ?: (0.0 to "")
        val highestPrice = highestPriceData.first
        val timeOfDay = highestPriceData.second
            .let { ZonedDateTime.parse(it).hour.toString() }

        return DataPoint("Highest electricity price (Finland, 48h) at $timeOfDay:00", highestPrice.toString())
    }
}
