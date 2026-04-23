package com.example.to_do.adapters

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object TaskObject {

    fun fetchTasksFromPref(application: Application, keyTasks: String): MutableList<String>{
        val sharedPreferences: SharedPreferences = application.getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val tasksList = sharedPreferences.getString(keyTasks, "")
        val result = tasksList?.split(";")
            .orEmpty()
            .toList()
            .filter { it.isNotEmpty() }
            .toMutableList()
        return result
    }

    fun saveTasksToPref(application: Application, keyTasks: String, tasks:List<String>){
        val sharedPreferences: SharedPreferences = application.getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val tasksString = tasks.joinToString(";")
        sharedPreferences.edit()
            .putString(keyTasks, tasksString)
            .apply()
    }
}