package com.example.infone.model

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
}