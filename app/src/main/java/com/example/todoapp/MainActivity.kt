package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val ADD_TASK_REQUEST_CODE = 1
    private val EDIT_TASK_REQUEST_CODE = 2

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        taskAdapter = TaskAdapter(
            onTaskChecked = { task, isChecked -> taskViewModel.updateTaskCompletion(task, isChecked) },
            onTaskLongClick = { task ->
                AlertDialog.Builder(this)
                    .setTitle("Удаление")
                    .setMessage("Удалить задачу \"${task.title}\"?")
                    .setPositiveButton("Да") { _, _ -> taskViewModel.deleteTask(task) }
                    .setNegativeButton("Нет", null).show()
            },
            onTaskClick = { task ->
                val intent = Intent(this, AddTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                intent.putExtra("task_title", task.title)
                intent.putExtra("task_desc", task.desc)
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
            }
        )

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }

        // Обновление списка и СТАТИСТИКИ
        lifecycleScope.launch {
            taskViewModel.tasks.collect { tasks ->
                taskAdapter.updateTasks(tasks)

                // Считаем статистику
                val active = tasks.count { !it.isCompleted }
                val completed = tasks.count { it.isCompleted }
                val total = tasks.size
                val percent = if (total > 0) (completed.toFloat() / total * 100).toInt() else 0

                // Выводим в UI (проверь ID в activity_main.xml!)
                findViewById<TextView>(R.id.tvActiveCount).text = active.toString()
                findViewById<TextView>(R.id.tvCompletedCount).text = completed.toString()
                findViewById<TextView>(R.id.tvProgress).text = "$percent %"
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), ADD_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val title = data.getStringExtra("task_title") ?: ""
            val desc = data.getStringExtra("task_desc") ?: ""
            val diff = data.getStringExtra("task_diff") ?: "Средняя"

            if (requestCode == ADD_TASK_REQUEST_CODE) {
                taskViewModel.addTask(title, desc, diff)
            } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
                val id = data.getIntExtra("task_id", 0)
                taskViewModel.updateTaskFull(Task(id, title, desc, "Активно", false, diff))
            }
        }
    }
}