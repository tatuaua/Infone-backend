package com.example.infone

import com.example.infone.service.DataPointService
import com.example.infone.utils.FetcherRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InfoneApplication

fun main(args: Array<String>) {
    val context = runApplication<InfoneApplication>(*args)
    val fetcherRunner = context.getBean(FetcherRunner::class.java)
    while(true) {
        fetcherRunner.run()
        Thread.sleep(600000)
    }
}
