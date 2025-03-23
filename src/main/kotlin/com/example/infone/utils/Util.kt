package com.example.infone.utils

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import java.text.DecimalFormat

object Util {

    fun formatDouble(value: Double): String {
        return DecimalFormat("#.###").format(value).replace(",", ".")
    }

    fun mockFetcherWithRandomDelay(n: Int) : DataPointFetcher {
        return object : DataPointFetcher {
            override fun fetch(): DataPoint {
                Thread.sleep((Math.random() * 1000).toLong())
                return DataPoint(n, "mock $n", "mock $n", "mock $n")
            }
        }
    }
}