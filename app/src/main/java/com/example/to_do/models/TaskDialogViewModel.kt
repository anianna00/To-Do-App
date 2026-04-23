package com.example.to_do.models

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDialogViewModel(application: Application): TaskViewModel(application) {
    fun updateTaskName(taskId: Int, taskName: String){
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.getTaskById(taskId)
            val updatedTask = repository.changeTaskContent(task, name = taskName)
            updateTask(updatedTask)
        }
    }

    fun deleteTaskById(taskId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.getTaskById(taskId)
            deleteTask(task)
        }
    }
}