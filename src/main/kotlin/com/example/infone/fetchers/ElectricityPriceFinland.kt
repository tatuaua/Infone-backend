package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.utils.RequestUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Component
class ElectricityPriceFinland : DataPointFetcher {

    private val url = "https://api.porssisahko.net/v1/latest-prices.json"
    private val id = DataPointFetcher.getNextId()
    private val description = "Maximum electricity price in Finland in the next 24 hours"

    override fun fetch(): DataPoint {
        val response = RequestUtils.makeRequest(url, HttpMethod.GET, HttpHeaders(), null)

        val body = response.body ?: throw RuntimeException("Response body is empty")
        val highestPriceData = getHighestPriceInNext24hrs(body)

        val highestPrice = highestPriceData?.second ?: 0.0
        val timeOfDay = highestPriceData?.first ?: ""

        return DataPoint(id, "Max elec. price (FI24)", "$highestPrice at $timeOfDay", description)
    }

    fun getHighestPriceInNext24hrs(jsonData: String): Pair<String, Double>? {
        val mapper = ObjectMapper()
        val rootNode: JsonNode = mapper.readTree(jsonData)
        val pricesArray = rootNode.get("prices")
        val currentTime = ZonedDateTime.now()
        val endTime = currentTime.plus(24, ChronoUnit.HOURS)

        val highestPrice = pricesArray
            .mapNotNull { priceNode ->
                val startDate = ZonedDateTime.parse(priceNode.get("startDate").asText(), DateTimeFormatter.ISO_DATE_TIME)
                val price = priceNode.get("price").asDouble()
                if (startDate.isAfter(currentTime) && startDate.isBefore(endTime)) {
                    startDate to price
                } else {
                    null
                }
            }
            .maxByOrNull { it.second }

        return highestPrice?.let {
            it.first.toLocalTime().toString() to it.second
        }
    }
}
