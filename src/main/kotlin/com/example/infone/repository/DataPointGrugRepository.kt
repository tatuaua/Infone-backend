package com.example.infone.repository

import com.example.infone.utils.GrugDBClient
import com.example.infone.model.DataPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("local")
class DataPointGrugRepository : DataPointRepository {

    val db: GrugDBClient = GrugDBClient.getInstance()

    override fun getDataPoints(): List<DataPoint> {
        return db.find(DataPoint::class.java)
    }

    override fun getDataPoints(ids: List<Int>): List<DataPoint> {
        return db.find(DataPoint::class.java) { it.id in ids }
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