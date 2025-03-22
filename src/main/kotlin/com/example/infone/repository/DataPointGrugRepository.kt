package com.example.infone.repository

import com.example.infone.model.DataPoint
import org.example.grugDB.GrugDBClient
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("local")
class DataPointGrugRepository : DataPointRepository {

    val db = GrugDBClient.getInstance(true) ?: throw Exception("Failed to connect to GrugDB")

    override fun dropAndCreateTable() {
    }

    override fun getDataPoints(): List<DataPoint> {
        return db.find(DataPoint::class.java)
    }

    override fun getDataPoints(ids: List<Int>): List<DataPoint> {
        return db.find(DataPoint::class.java).stream().filter { it.id in ids }.toList()
    }

    override fun upsertDatapoint(
        id: Int,
        name: String,
        value: String,
        description: String
    ) {
        db.delete(DataPoint::class.java) { it.id == id }
        db.save(DataPoint(id, name, value, description))
    }

}