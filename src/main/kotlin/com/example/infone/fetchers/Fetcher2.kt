package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import org.springframework.stereotype.Component

@Component
class Fetcher2 : DataPointFetcher {

    var i = 0
    override fun fetch(): DataPoint {
        if (i == 1) {
            return DataPoint("balls3", "new value")
        }
        i = 1
        return DataPoint("balls3", "value3")
    }
}