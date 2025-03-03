package com.example.infone.repository

import com.example.infone.model.DataPoint
import jakarta.annotation.PostConstruct
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Suppress("SqlNoDataSourceInspection")
@Repository
class DataPointJdbcRepository(private val jdbcTemplate: JdbcTemplate) : DataPointRepository {

    @PostConstruct
    override fun createTable() {
        val dropTable = "DROP TABLE IF EXISTS data_points"
        val createTable = """
            CREATE TABLE data_points (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255),
                value VARCHAR(255)
            )
        """
        jdbcTemplate.execute(dropTable)
        jdbcTemplate.execute(createTable)
    }

    override fun getDataPoints(): List<DataPoint> {
        val sql = "SELECT id, name, value FROM data_points"
        return jdbcTemplate.query(sql) { rs, _ ->
            DataPoint(rs.getString("id"), rs.getString("name"), rs.getString("value"))
        }
    }

    override fun getDataPoint(id: String): DataPoint? {
        val sql = "SELECT id, name, value FROM data_points WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, DataPoint::class.java, id)
    }

    override fun upsertDatapoint(id: String, name: String, value: String) {
        val sql = "INSERT INTO data_points (id, name, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, value = ?"
        jdbcTemplate.update(sql, id, name, value, name, value)
    }
}
