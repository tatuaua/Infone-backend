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
                name VARCHAR(255) PRIMARY KEY,
                value VARCHAR(255)
            )
        """
        jdbcTemplate.execute(dropTable)
        jdbcTemplate.execute(createTable)
    }

    override fun getDataPoints(): List<DataPoint> {
        val sql = "SELECT name, value FROM data_points"
        return jdbcTemplate.query(sql) { rs, _ ->
            DataPoint(rs.getString("name"), rs.getString("value"))
        }
    }

    override fun getDataPoint(name: String): DataPoint? {
        val sql = "SELECT name, value FROM data_points WHERE name = ?"
        return jdbcTemplate.queryForObject(sql, DataPoint::class.java, name)
    }

    override fun upsertDatapoint(name: String, value: String) {
        val sql = """
            INSERT INTO data_points (name, value) VALUES (?, ?)
            ON CONFLICT (name) DO UPDATE SET value = ?
        """
        jdbcTemplate.update(sql, name, value, value)
    }
}
