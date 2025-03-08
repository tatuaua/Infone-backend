package com.example.infone.fetchers

import com.example.infone.model.DataPointFetcher
import com.example.infone.model.DataPoint
import com.example.infone.utils.RequestUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.text.DecimalFormat

@Component
class WeatherOulu : DataPointFetcher {

    private val mapper = ObjectMapper()
    private val url = "https://api.open-meteo.com/v1/forecast?latitude=65.0121&longitude=25.4651&hourly=temperature_2m,relative_humidity_2m&timezone=auto&forecast_days=1"
    private val id = DataPointFetcher.getNextId()

    override fun fetch(): DataPoint {
        val response = RequestUtils.makeRequest(url, HttpMethod.GET, HttpHeaders(), null)
        val body = response.body ?: throw RuntimeException("Response body is empty")

        val averageTemperature =
            DecimalFormat("#.###").
            format(getAverageTemperatureFrom8amTo8pm(body)).
            replace(",", ".")

        val averageHumidity =
            DecimalFormat("#.###").
            format(getAverageHumidityFrom8amTo8pm(body)).
            replace(",", ".")

        return DataPoint(id, "Avg temp./humid. in Oulu", "$averageTemperature°C / $averageHumidity%")
    }

    fun getAverageTemperatureFrom8amTo8pm(jsonData: String): Double {
        val rootNode = mapper.readTree(jsonData)
        val hourly = rootNode.get("hourly")
        val temperatures = hourly.get("temperature_2m")
        val times = hourly.get("time")

        val temperaturesAndTimes = temperatures.zip(times)
        val temperaturesFrom8amTo8pm = temperaturesAndTimes
            .map { it }
            .filter { it.second.asText().substring(11, 13).toInt() in 8..20 }
            .map { it.first.asDouble() }

        return temperaturesFrom8amTo8pm.average()
    }
    
    fun getAverageHumidityFrom8amTo8pm(jsonData: String): Double {
        val rootNode = mapper.readTree(jsonData)
        val hourly = rootNode.get("hourly")
        val humidities = hourly.get("relative_humidity_2m")
        val times = hourly.get("time")

        val humiditiesAndTimes = humidities.zip(times)
        val humiditiesFrom8amTo8pm = humiditiesAndTimes
            .map { it }
            .filter { it.second.asText().substring(11, 13).toInt() in 8..20 }
            .map { it.first.asDouble() }

        return humiditiesFrom8amTo8pm.average()
    }
}