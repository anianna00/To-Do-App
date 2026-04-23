package com.example.to_do.adapters

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedPrefViewModel(application: Application):AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences("UserPreferences", MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val startingCompletedList: MutableList<String> = fetchCompletedTasksFromPref()

    private val _completedTasks = MutableLiveData<MutableList<String>>(startingCompletedList)
    val completedTasks: LiveData<MutableList<String>> get() = _completedTasks

    private fun fetchCompletedTasksFromPref(): MutableList<String> {
        val completedTasksList = sharedPreferences.getString("COMPLETED_TASKS_LIST", "")
        val result = completedTasksList?.split(";")
            .orEmpty()
            .toList()
            .filterNot{it.isEmpty()}
            .toMutableList()
        return result
    }

    private fun saveCompletedTasksToPref(completedTasks: List<String>){
        val completedTasksString = completedTasks.joinToString(";")
        editor.putString("COMPLETED_TASKS_LIST", completedTasksString).apply()
    }

    fun addCompletedTask(task:String){
        if(task.isNotEmpty()){
            val updatedCompletedTasksList = _completedTasks.value.orEmpty().toMutableList()
            updatedCompletedTasksList.add(task)
            _completedTasks.value = updatedCompletedTasksList
            saveCompletedTasksToPref(updatedCompletedTasksList)
        }
    }

    fun deleteTask(task: String) {
        _completedTasks.value?.remove(task)
        _completedTasks.value = _completedTasks.value
        saveCompletedTasksToPref(_completedTasks.value.orEmpty())
    }

}