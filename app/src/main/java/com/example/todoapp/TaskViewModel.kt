package com.example.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskDatabase.getDatabase(application)
    private val taskDao = database.taskDao()

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun addTask(title: String, desc: String, diff: String) {
        val newTask = Task(title = title, desc = desc, status = "Активно", difficulty = diff)
        Thread { taskDao.insert(newTask) }.start()
    }

    fun updateTaskFull(task: Task) {
        Thread { taskDao.update(task) }.start()
    }

    fun updateTaskCompletion(task: Task, isCompleted: Boolean) {
        val updatedTask = task.copy(isCompleted = isCompleted)
        Thread { taskDao.update(updatedTask) }.start()
    }

    fun deleteTask(task: Task) {
        Thread { taskDao.delete(task) }.start()
    }
}