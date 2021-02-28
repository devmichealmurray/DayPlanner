package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "TodoViewModel"

class TodoViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _todoTaskList by lazy { MutableLiveData<List<TodoTaskEntity>>() }
    val todoTaskList: LiveData<List<TodoTaskEntity>> get() = _todoTaskList

    private val _todoErrorMessage by lazy { MutableLiveData<String>() }
    val todoErrorMessage: LiveData<String> get() = _todoErrorMessage


    fun getTasksFromDB() {
        viewModelScope.launch {
            try {
                dbRepo.getToDoTasks()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _todoTaskList.value = it
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
        Log.d(TAG, "* * * * *  REMOVE TASK CALLED from VM with $id * * * * *")
        viewModelScope.launch {
            dbRepo.deleteToDoTask(id)
        }
        refreshToDoFragment()
    }

    fun prepareTask(text: String) {
        if (text.isNotEmpty()) {
            val date: Long = System.currentTimeMillis()
            val taskEntity = TodoTaskEntity(
                title = text.capitalize(Locale.ROOT),
                dateAdded = date.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }
            )
            addTaskToDb(taskEntity)
        } else {
            _todoErrorMessage.value = "You Must Have A Task To Add"
        }
    }

    private fun addTaskToDb(task: TodoTaskEntity) {
        viewModelScope.launch {
            try {
                dbRepo.addTodoTask(task)
            } catch (e: Exception) {
                _todoErrorMessage.value = e.message.toString()
            }

        }
    }
}