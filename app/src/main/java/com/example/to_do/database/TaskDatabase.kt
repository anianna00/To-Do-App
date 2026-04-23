package com.example.to_do.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// zawiera tabelę reprezentowaną przez klasę Task
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {   // RoomDatabase() - klasa z metodami

    abstract fun taskDao(): TaskDao

    companion object {      // definiuje statyczne czlony klasy
        private var INSTANCE: TaskDatabase? = null      // private więc tylko do odczytu będąc w TaskDatabase

        fun getDatabase(context: Context): TaskDatabase {
            val tempInstance = INSTANCE
            // jesli baza już istnieje to ją zwracamy
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){     // tylko jeden wątek może wykonywać ten blok kodu w danym momencie
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                return instance     // zwracamy pojedyncza instancje klasy
            }
        }
    }
}