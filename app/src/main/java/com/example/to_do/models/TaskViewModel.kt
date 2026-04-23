package com.example.to_do.models

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.to_do.database.Task
import com.example.to_do.database.TaskDatabase
import com.example.to_do.database.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// definicja funkcji, ale samo wykonanie we fragmentach

open class TaskViewModel(application: Application) : AndroidViewModel(application) {
    val repository: TaskRepository
    lateinit var allCompletedTasks: LiveData<List<Task>>
    lateinit var allUncompletedTasks: LiveData<List<Task>>

    init {
        val database = TaskDatabase.getDatabase(application)
        val taskDao = database.taskDao()
        repository = TaskRepository(taskDao)
//        allCompletedTasks = repository.allCompletedTasks
//        allUncompletedTasks = repository.allUncompletedTasks
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun setTasksOfUser(userId: String){
        allUncompletedTasks = repository.getAllUncompletedTasks(userId)
        allCompletedTasks = repository.getAllCompletedTasks(userId)
        Log.i("lista", "tasks")
    }

    fun makeToast(context: Context, message: String){
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

}