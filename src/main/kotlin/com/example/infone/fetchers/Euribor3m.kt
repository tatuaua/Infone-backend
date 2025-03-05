package com.example.infone.fetchers

import com.example.infone.model.DataPoint
import com.example.infone.model.DataPointFetcher
import com.example.infone.model.RequestUtils
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.coyote.Request
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import java.nio.file.Files
import java.text.DecimalFormat

@Component
class Euribor3m: DataPointFetcher {

    private val mapper = ObjectMapper()
    private val url = "https://data-api.ecb.europa.eu/service/data/FM/M.U2.EUR.RT.MM.EURIBOR3MD_.HSTA?lastNObservations=24&detail=dataonly&format=jsondata"
    private val id = DataPointFetcher.getNextId()

    override fun fetch(): DataPoint {
        val response: ResponseEntity<Resource> = RequestUtils.makeFileRequest(
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
        val formatted = DecimalFormat("#.###").format(value).replace(",", ".")
        return DataPoint(id, "Euribor 3M", formatted)
    }
}