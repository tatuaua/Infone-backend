package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import org.springframework.stereotype.Component

@Component
class Fetcher1 : DataPointFetcher {
    override fun fetch(): DataPoint {
        return DataPoint("balls2", "value2")
    }
}