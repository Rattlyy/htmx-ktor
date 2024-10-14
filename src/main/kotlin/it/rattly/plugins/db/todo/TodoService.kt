package it.rattly.plugins.db.todo

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language
import java.sql.Statement

@Serializable
data class Todo(var id: Int? = null, val title: String, val content: String)
class TodoService(private val dataSource: HikariDataSource) {
    companion object {
        @Language("PostgreSQL")
        private const val CREATE_TABLE_TODOS =
            "CREATE TABLE IF NOT EXISTS TODOS (ID SERIAL PRIMARY KEY, TITLE VARCHAR(255), CONTENT VARCHAR(255));"

        @Language("PostgreSQL")
        private const val SELECT_TODO_BY_ID = "SELECT id, title, content FROM todos WHERE id = ?"

        @Language("PostgreSQL")
        private const val SELECT_ALL_TODOS = "SELECT id, title, content FROM todos"

        @Language("PostgreSQL")
        private const val INSERT_TODO = "INSERT INTO todos (title, content) VALUES (?, ?)"

        @Language("PostgreSQL")
        private const val UPDATE_TODO = "UPDATE todos SET title = ?, content = ? WHERE id = ?"

        @Language("PostgreSQL")
        private const val DELETE_TODO = "DELETE FROM todos WHERE id = ?"

    }

    init {
        dataSource.connection.use { it.createStatement().executeUpdate(CREATE_TABLE_TODOS) }
    }

    // Create new to-do item
    suspend fun create(todo: Todo): Int = withContext(Dispatchers.IO) {
        dataSource.connection.use {
            val statement = it.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS).apply {
                setString(1, todo.title)
                setString(2, todo.content)

                executeUpdate()
            }

            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                todo.id = generatedKeys.getInt(1)
                return@withContext generatedKeys.getInt(1)
            } else {
                throw Exception("Unable to retrieve the id of the newly inserted todo")
            }
        }
    }

    suspend fun readAll(): List<Todo> = withContext(Dispatchers.IO) {
        dataSource.connection.use {
            val rs = it.prepareStatement(SELECT_ALL_TODOS).executeQuery()
            val list = mutableListOf<Todo>()

            while (rs.next()) {
                val title = rs.getString("title")
                val id = rs.getInt("id")
                val content = rs.getString("content")

                list += Todo(id, title, content)
            }

            return@withContext list
        }
    }

    // Read a to-do
    suspend fun findById(id: Int): Todo = withContext(Dispatchers.IO) {
        dataSource.connection.use {
            val rs = it.prepareStatement(SELECT_TODO_BY_ID).apply {
                setInt(1, id)
            }.executeQuery()

            if (rs.next()) {
                val title = rs.getString("title")
                val content = rs.getString("content")
                return@withContext Todo(id, title, content)
            } else {
                throw Exception("Record not found")
            }
        }
    }

    // Update a to-do
    suspend fun update(id: Int, todo: Todo) = withContext(Dispatchers.IO) {
        dataSource.connection.use {
            with(it.prepareStatement(UPDATE_TODO)) {
                setString(1, todo.title)
                setString(2, todo.content)
                setInt(3, id)

                executeUpdate()
            }
        }
    }

    // Delete a to-do
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        dataSource.connection.use {
            with(it.prepareStatement(DELETE_TODO)) {
                setInt(1, id)
                executeUpdate()
            }
        }
    }
}
