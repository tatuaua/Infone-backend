package com.example.infone.repository

import com.example.infone.model.DataPoint
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Suppress("SqlNoDataSourceInspection")
@Repository
@Profile("local")
class DataPointH2Repository(private val jdbcTemplate: JdbcTemplate) : DataPointRepository {

    @PostConstruct
    override fun createTable() {
        println("Creating H2 table")
        val dropTable = "DROP TABLE IF EXISTS data_points"
        val createTable = """
            CREATE TABLE data_points (
                "id" INTEGER PRIMARY KEY,
                "name" VARCHAR(255),
                "value" VARCHAR(255),
                "description" VARCHAR(255)
            )
        """.trimIndent()
        jdbcTemplate.execute(dropTable)
        jdbcTemplate.execute(createTable)
    }

    override fun getDataPoints(): List<DataPoint> {
        val sql = "SELECT \"id\", \"name\", \"value\", \"description\" FROM data_points"
        return jdbcTemplate.query(sql) { rs, _ ->
            DataPoint(rs.getInt("id"), rs.getString("name"), rs.getString("value"), rs.getString("description"))
        }
    }

    override fun getDataPoints(ids: List<Int>): List<DataPoint> {
        val sql = "SELECT \"id\", \"name\", \"value\", \"description\" FROM data_points WHERE \"id\" IN (${ids.joinToString(",") { "?" }})"
        return jdbcTemplate.query(sql, ids.toTypedArray()) { rs, _ ->
            DataPoint(rs.getInt("id"), rs.getString("name"), rs.getString("value"), rs.getString("description"))
        }
    }

    override fun upsertDatapoint(id: Int, name: String, value: String, description: String) {
        val sql = """
            MERGE INTO data_points ("id", "name", "value", "description")
            VALUES (?, ?, ?, ?)
        """.trimIndent()
        jdbcTemplate.update(sql, id, name, value, description)
    }
}