package it.rattly.plugins.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.rattly.plugins.db.todo.TodoService
import it.rattly.plugins.db.todo.todoRoutes
import kotlinx.coroutines.*
import java.sql.*


fun Application.configureDatabases() {
    todoRoutes(TodoService(connectToPostgres(environment.developmentMode)))
}

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if true defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun connectToPostgres(embedded: Boolean): HikariDataSource {
    Class.forName("org.postgresql.Driver")

    val config = HikariConfig().apply {
        setPoolName("DB Pool")
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        setMaximumPoolSize(20)
    }

    if (embedded) {
        val dataSource = HikariDataSource(config.apply {
            this.username = "root"
            this.password = ""

            setDriverClassName("org.h2.Driver")
            setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        })

        return dataSource
    } else {
        val dataSource = HikariDataSource(config.apply {
            this.username = System.getenv("postgres.user")
            this.password = System.getenv("postgres.password")

            setDriverClassName("org.postgresql.Driver")
            setJdbcUrl(System.getenv("postgres.url"))
        })

        return dataSource
    }
}
