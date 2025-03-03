package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.model.RequestHelper
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

@Component
class Euribor3m (private val requestHelper: RequestHelper) : DataPointFetcher {

    @Value("\${x-rapidapi-key}")
    lateinit var apiKey: String

    private val mapper = ObjectMapper()

    override fun fetch(): DataPoint {

        val headers = HttpHeaders().apply {
            set("X-RapidAPI-Key", apiKey)
        }

        val data = requestHelper.makeRequest(
            "https://euribor.p.rapidapi.com/",
            HttpMethod.GET,
            headers,
            null
        )

        val value = mapper.readTree(data).get("3m").asText()

        return DataPoint("Euribor 3m", value)
    }
}