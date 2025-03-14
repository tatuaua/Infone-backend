package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.utils.RequestUtils
import com.example.infone.utils.Util
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.text.DecimalFormat

@Component
class SP500 : DataPointFetcher {

    private val ticker = "^GSPC"
    private val period = "2d"
    private val interval = "1d"
    val id = DataPointFetcher.getNextId()
    val mapper = ObjectMapper()

    override fun fetch(): DataPoint {
        val response = RequestUtils.yahooFinanceRequest(ticker, period, interval)
        val body = response.body ?: throw RuntimeException("Response body is empty")
        val jsonResponse = mapper.readTree(body)
        val closePrices = jsonResponse.get("chart")
            .get("result")
            .get(0)
            .get("indicators")
            .get("quote")
            .get(0)
            .get("close")

        if (closePrices.size() >= 2) {
            val todayClose = closePrices.get(closePrices.size() - 1).asDouble()
            val yesterdayClose = closePrices.get(closePrices.size() - 2).asDouble()
            val oneDayChange = todayClose - yesterdayClose
            val formattedChange = Util.formatDouble(oneDayChange)
            val formattedPercentage = Util.formatDouble(oneDayChange / yesterdayClose * 100)
            return DataPoint(id, "S&P 500", "$formattedChange ($formattedPercentage%)", "S&P 500 index at close")
        } else {
            throw RuntimeException("Not enough data available to calculate one-day change")
        }
    }
}