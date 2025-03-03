package com.example.infone

import com.example.infone.service.DataPointService
import com.example.infone.service.ExternalDataFetcher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InfoneApplication

fun main(args: Array<String>) {

    val context = runApplication<InfoneApplication>(*args)
    val externalDataFetcher = context.getBean(ExternalDataFetcher::class.java)
    externalDataFetcher.run()

    val dataPointService = context.getBean(DataPointService::class.java)
    println(dataPointService.getDataPoints())
}
