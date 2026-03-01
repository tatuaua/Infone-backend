package com.example.infone

import com.example.infone.utils.FetcherRunner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class InfoneApplication

fun main(args: Array<String>) {
    val context = runApplication<InfoneApplication>(*args)
    val fetcherRunner = context.getBean<FetcherRunner>()

    runBlocking {
        while (true) {
            launch {
                fetcherRunner.runAsync()
            }
            delay(100000)
        }
    }
}
