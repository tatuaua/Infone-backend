package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.utils.RequestUtils
import com.example.infone.utils.Util
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import java.nio.file.Files

@Component
class Euribor3m: DataPointFetcher {

    private val mapper = ObjectMapper()
    private val url = "https://data-api.ecb.europa.eu/service/data/FM/M.U2.EUR.RT.MM.EURIBOR3MD_.HSTA?lastNObservations=24&detail=dataonly&format=jsondata"
    private val id = DataPointFetcher.getNextId()
    private val description = "Euribor rate for 3 months"
    private val name = "Euribor 3M"

    override fun fetch(): DataPoint {
        val response = RequestUtils.makeFileRequest(
            url,
            HttpMethod.GET,
            HttpHeaders(),
            null
        )

        val fileResource = response.body ?: throw RuntimeException("File resource is empty")
        val tempFile = Files.createTempFile("euribor", ".json")
        fileResource.inputStream.use { input -> Files.copy(input, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING) }
        val jsonNode: JsonNode = mapper.readTree(Files.newBufferedReader(tempFile))
        val value = jsonNode.get("dataSets").first().get("series").first().get("observations").last().get(0).asDouble()
        val formatted = Util.formatDouble(value)
        return DataPoint(id, name, formatted, description)
    }
}