package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.utils.RequestUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SpotifySuomiHitit : DataPointFetcher {

    private val mapper = ObjectMapper()
    private val url = "https://api.spotify.com/v1/playlists/7f4RDnWP4Bht6EeuIaC8wh?fields=tracks(items(track(name,popularity,artists)))"
    private val id = DataPointFetcher.getNextId()
    private val description = "Top song of Filtr Finland top 50"
    private val name = "Spotify Finland Top Hit"

    private lateinit var token: String
    private var tokenLastUpdate = LocalDateTime.now().minusHours(2) // make checkAuth run immediately

    @Value("\${spotify.client.id}")
    private lateinit var clientId: String
    @Value("\${spotify.client.secret}")
    private lateinit var clientSecret: String

    override fun fetch(): DataPoint {
        checkAuth()
        val response = RequestUtils.makeRequest(
            url,
            HttpMethod.GET,
            HttpHeaders().apply { set("Authorization", "Bearer $token") },
            null
        )
        val topTrack = mapper.readTree(response.body ?: throw RuntimeException("Response body is empty"))
            .get("tracks")
            .get("items")
            .mapNotNull { it.get("track") }
            .maxByOrNull { it.get("popularity").asInt() } ?: throw RuntimeException("No tracks found")
        return DataPoint(
            id,
            name,
            "${topTrack.get("name").asText()} by ${topTrack.get("artists")[0].get("name").asText()}",
            description
        )
    }

    private fun checkAuth() {
        if (!::token.isInitialized || tokenLastUpdate.plusMinutes(50).isBefore(LocalDateTime.now())) {
            val response = RequestUtils.spotifyAuthRequest(clientId, clientSecret)
            val body = response.body ?: throw RuntimeException("Response body is empty")
            val jsonNode: JsonNode = mapper.readTree(body)
            token = jsonNode.get("access_token").asText()
            tokenLastUpdate = LocalDateTime.now()
        }
    }
}