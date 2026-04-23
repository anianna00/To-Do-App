package com.example.to_do.database

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

//    val allUncompletedTasks: LiveData<List<Task>> = taskDao.getUncompletedTasks()
//    val allCompletedTasks: LiveData<List<Task>> = taskDao.getCompletedTasks()

    fun getAllUncompletedTasks(token: String): LiveData<List<Task>> {
        return taskDao.getUncompletedTasks(token)
    }

    fun getAllCompletedTasks(userId: String): LiveData<List<Task>> {
        return taskDao.getCompletedTasks(userId)
    }

    fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    fun changeTaskContent(
        task:Task,
        name: String = task.name,
        isCompleted: Boolean = task.isCompleted,
        userId: String = task.userId
    ): Task {
        return Task(task.id, name, isCompleted, userId)
    }

    fun getTaskById(taskId: Int): Task {
        return taskDao.getTaskById(taskId)
    }

}