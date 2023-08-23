package it.rattly.plugins.db.todo

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language
import java.sql.Connection
import java.sql.Statement

@Serializable
data class Todo(var id: Int? = null, val title: String, val content: String)
class TodoService(private val connection: Connection) {
    companion object {
        @Language("PostgreSQL")
        private const val CREATE_TABLE_TODOS =
            "CREATE TABLE TODOS (ID SERIAL PRIMARY KEY, TITLE VARCHAR(255), CONTENT VARCHAR(255));"

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
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_TODOS)
    }

    // Create new to-do item
    suspend fun create(todo: Todo): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, todo.title)
        statement.setString(2, todo.content)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            todo.id = generatedKeys.getInt(1)
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted todo")
        }
    }

    suspend fun read(): List<Todo> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_TODOS)
        val rs = statement.executeQuery()

        val list = mutableListOf<Todo>()

        while (rs.next()) {
            val title = rs.getString("title")
            val id = rs.getInt("id")
            val content = rs.getString("content")

            list += Todo(id, title, content)
        }

        return@withContext list
    }

    // Read a to-do
    suspend fun read(id: Int): Todo = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_TODO_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val title = resultSet.getString("title")
            val content = resultSet.getString("content")
            return@withContext Todo(id, title, content)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a to-do
    suspend fun update(id: Int, todo: Todo) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_TODO)
        statement.setString(1, todo.title)
        statement.setString(2, todo.content)
        statement.setInt(3, id)
        statement.executeUpdate()
    }

    // Delete a to-do
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TODO)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}
