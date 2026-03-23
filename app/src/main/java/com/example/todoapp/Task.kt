package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val title: String,
    val desc: String,
    val status: String,
    val isCompleted: Boolean = false,
    val difficulty: String = "Средняя" // Новое поле
)