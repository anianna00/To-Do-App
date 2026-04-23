package com.example.to_do.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.to_do.database.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// definicja funkcji, ale samo wykonanie we fragmentach
// i have to pass a userId to homeViewModel as well
class HomeViewModel(application: Application): TaskViewModel(application) {

    fun addTask(task: String, userId: String) {
        if (task.isNotEmpty()) {
            val newTask = Task(name = task, userId = userId)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.addTask(newTask)
                    Log.d("Udalo się", newTask.toString())
                } catch (e: Exception) {
                    Log.e("Nie udalo się", e.message.toString())
                }

            }
        }
    }

    fun moveToCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedTask = repository.changeTaskContent(task, isCompleted = true)
            updateTask(updatedTask)
        }
    }
}