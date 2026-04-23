package com.example.to_do.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// collection
@Entity(tableName = "tasks")
class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    val isCompleted: Boolean = false,
    val userId: String
)