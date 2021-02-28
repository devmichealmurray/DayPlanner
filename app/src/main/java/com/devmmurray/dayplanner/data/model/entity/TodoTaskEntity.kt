package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.TodoTask

@Entity(tableName = "todo_tasks")
class TodoTaskEntity(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0L,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "date_added")
    val dateAdded: String?

) {
    fun toTodoTaskObject() = TodoTask(uid, title, dateAdded)
}