package com.example.infone.model

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RequestHelper {
    fun makeRequest(url: String, requestType: HttpMethod, headers: HttpHeaders, body: Any?): String {
        val restTemplate = RestTemplate()
        val entity = HttpEntity(body, headers)
        val response = restTemplate.exchange(url, requestType, entity, String::class.java)
        return response.body ?: "Error: No response body"
    }
}