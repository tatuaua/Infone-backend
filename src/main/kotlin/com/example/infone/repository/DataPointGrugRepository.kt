package com.example.infone.repository

import com.example.infone.utils.GrugDBClient
import com.example.infone.model.DataPoint
import org.springframework.stereotype.Repository

@Repository
class DataPointGrugRepository : DataPointRepository {

    val db: GrugDBClient = GrugDBClient.getInstance()

    init {
        GrugDBClient.clearDatabaseDirectory()
    }

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
        db.updateOrSave(DataPoint(id, name, value, description), {it.id == id}, { it ->
            it.name = name
            it.value = value
            it.description = description
        } )
    }
}