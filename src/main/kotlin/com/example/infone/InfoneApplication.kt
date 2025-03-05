package com.example.infone

import com.example.infone.service.DataPointService
import com.example.infone.utils.FetcherRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InfoneApplication

fun main(args: Array<String>) {

    runApplication<InfoneApplication>(*args).apply {
        getBean(FetcherRunner::class.java).run()
        getBean(DataPointService::class.java).getDataPoints().forEach { println("DataPoint: $it") }
    }
}
