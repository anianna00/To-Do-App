package com.example.to_do.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao{
    @Insert(onConflict = OnConflictStrategy.NONE)
    fun addTask(task: Task)

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND userId=:userId")
    fun getUncompletedTasks(userId: String): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 AND userId=:userId")
    fun getCompletedTasks(userId: String): LiveData<List<Task>>

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id=:taskId")
    fun getTaskById(taskId: Int): Task
}