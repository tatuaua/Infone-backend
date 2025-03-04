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
                id INTEGER PRIMARY KEY,
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
            DataPoint(rs.getInt("id"), rs.getString("name"), rs.getString("value"))
        }
    }

    override fun getDataPoints(ids: List<Int>): List<DataPoint> {
        val sql = "SELECT id, name, value FROM data_points WHERE id IN (${ids.joinToString(",") { "?" }})"

        return jdbcTemplate.query(sql, ids.toTypedArray()) { rs, _ ->
            DataPoint(rs.getInt("id"), rs.getString("name"), rs.getString("value"))
        }
    }

    override fun getDataPoint(id: Int): DataPoint? {
        val sql = "SELECT id, name, value FROM data_points WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, DataPoint::class.java, id)
    }

    override fun upsertDatapoint(id: Int, name: String, value: String) {
        val sql = """
        INSERT INTO data_points (id, name, value) 
        VALUES (?, ?, ?) 
        ON CONFLICT(id) 
        DO UPDATE SET name = EXCLUDED.name, value = EXCLUDED.value
    """
        jdbcTemplate.update(sql, id, name, value)
    }
}
