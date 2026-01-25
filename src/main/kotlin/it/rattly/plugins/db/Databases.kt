package it.rattly.plugins.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import it.rattly.plugins.db.todo.TodoService
import it.rattly.plugins.db.todo.todoRoutes
import java.sql.Connection


fun Application.configureDatabases() {
    todoRoutes(TodoService(connectToPostgres(System.getenv("USE_EMBEDDED_DB") == "true")))
}

fun connectToPostgres(embedded: Boolean = true): HikariDataSource {
    Class.forName("org.postgresql.Driver")

    val config = HikariConfig().apply {
        poolName = "DB Pool"
        maximumPoolSize = 20

        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    }

    if (embedded) {
        val dataSource = HikariDataSource(config.apply {
            this.username = "root"
            this.password = ""

            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        })

        return dataSource
    } else {
        val dataSource = HikariDataSource(config.apply {
            this.username = System.getenv("postgres.user")
            this.password = System.getenv("postgres.password")

            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("postgres.url")
        })

        return dataSource
    }
}
