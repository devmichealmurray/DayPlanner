package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "TodoViewModel"

class TodoViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _todoTaskList by lazy { MutableLiveData<List<TodoTaskEntity>>() }
    val todoTaskList: LiveData<List<TodoTaskEntity>> get() = _todoTaskList

    private val _refreshFragment by lazy { MutableLiveData<Boolean>() }
    val refreshFragment: LiveData<Boolean> get() = _refreshFragment

    private val _progress by lazy { MutableLiveData<Boolean>() }
    val progress: LiveData<Boolean> get() = _progress

    private val _todoError by lazy { MutableLiveData<String>() }
    val todoError: LiveData<String> get() = _todoError

    fun addTaskToDb(task: TodoTaskEntity) {
        viewModelScope.launch {
            dbRepo.addTodoTask(task)
        }
    }

    fun getTasksFromDB() {
        _progress.value = true
        viewModelScope.launch {
            try {
                dbRepo.getToDoTasks()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _todoTaskList.value = it
                        _progress.value = false
                    }
            } catch (e: Exception) {
                _progress.value = false
                _todoError.value = e.message.toString()
            }
        }
    }

    private fun refreshToDoFragment() {
        getTasksFromDB()
        _refreshFragment.value = true
    }

    fun removeTask(id: Long) {
        Log.d(TAG, "* * * * *  REMOVE TASK CALLED from VM with $id * * * * *")
        viewModelScope.launch {
            dbRepo.deleteToDoTask(id)
        }
        refreshToDoFragment()
    }
}