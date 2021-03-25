package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.data.model.local.TodoTask
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "TodoViewModel"

class TodoViewModel(app: Application) : SplashActivityViewModel(app) {

    private val _todoTaskList by lazy { MutableLiveData<List<TodoTask>>() }
    val todoTaskList: LiveData<List<TodoTask>> get() = _todoTaskList

    private val _todoErrorMessage by lazy { MutableLiveData<String>() }
    val todoErrorMessage: LiveData<String> get() = _todoErrorMessage


    fun getTasksFromDB() {
        viewModelScope.launch {
            try {
                todoTasksUseCases.getToDoTasks.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect { list ->
                        val tasks =
                            list.toMutableList()
                                .map { it.toTodoTaskObject() }
                        _todoTaskList.value = tasks.reversed()
                    }
            } catch (e: Exception) {
                _todoErrorMessage.value = e.message.toString()
            }
        }
    }

    private fun refreshToDoFragment() {
        getTasksFromDB()
    }

    fun removeTask(id: Long) {
        viewModelScope.launch {
            try {
                todoTasksUseCases.deleteToDoTask.invoke(id)
                refreshToDoFragment()
            } catch (e: Exception) {
                _todoErrorMessage.value = e.message.toString()
            }
        }
    }

    fun prepareTask(text: String) {
        if (text.isNotEmpty()) {
            val date: Long = System.currentTimeMillis()
            val taskEntity =
                TodoTaskEntity(
                    title = text.capitalize(Locale.ROOT),
                    dateAdded = date.let {
                        TimeStampProcessing.transformSystemTime(
                            it,
                            TimeFlags.FULL
                        )
                    }
                )
            addTaskToDb(taskEntity)
        } else {
            _todoErrorMessage.value = "You Must Have A Task To Add"
        }
    }

    private fun addTaskToDb(task: TodoTaskEntity) {
        viewModelScope.launch {
            try {
                todoTasksUseCases.addTodoTask.invoke(task)
                refreshToDoFragment()
            } catch (e: Exception) {
                _todoErrorMessage.value = e.message.toString()
            }
        }
    }

}