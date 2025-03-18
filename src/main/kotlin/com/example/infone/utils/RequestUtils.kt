package com.example.infone.utils

import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

object RequestUtils {
    fun makeRequest(url: String, requestType: HttpMethod, headers: HttpHeaders, body: Any?): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val entity = HttpEntity(body, headers)
        val response = restTemplate.exchange(url, requestType, entity, String::class.java)
        if(response.statusCode.is2xxSuccessful) {
            return response
        } else {
            throw RuntimeException("Failed to fetch data: ${response.statusCode}")
        }
    }

    fun makeFileRequest(url: String, requestType: HttpMethod, headers: HttpHeaders, body: Any?): ResponseEntity<Resource> {
        val restTemplate = RestTemplate()
        val entity = HttpEntity(body, headers)
        val response = restTemplate.exchange(url, requestType, entity, Resource::class.java)
        if(response.statusCode.is2xxSuccessful) {
            return response
        } else {
            throw RuntimeException("Failed to fetch file: ${response.statusCode}")
        }
    }

    fun yahooFinanceRequest(ticker: String, period: String, interval: String): ResponseEntity<String> {
        val url = "https://query1.finance.yahoo.com/v8/finance/chart/$ticker?range=$period&interval=$interval"
        val headers = HttpHeaders().apply {
            set("User-Agent", "PostmanRuntime/7.43.2")
            set("Accept", "*/*")
        }
        return makeRequest(url, HttpMethod.GET, headers, null)
    }

    fun spotifyAuthRequest(clientId: String, clientSecret: String): ResponseEntity<String> {
        val url = "https://accounts.spotify.com/api/token"
        val body = "grant_type=client_credentials&client_id=$clientId&client_secret=$clientSecret"

        val headers = HttpHeaders().apply {
            set("Content-Type", "application/x-www-form-urlencoded")
        }

        return makeRequest(url, HttpMethod.POST, headers, body)
    }
}