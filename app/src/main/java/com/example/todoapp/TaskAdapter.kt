package com.example.todoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskLongClick: (Task) -> Unit,
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvTaskDesc)
        val cbCompleted: CheckBox = view.findViewById(R.id.cbCompleted)
        val tvDiff: TextView = view.findViewById(R.id.tvTaskDifficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTitle.text = task.title
        holder.tvDesc.text = task.desc
        holder.tvDiff.text = task.difficulty

        // --- ЛОГИКА СМЕНЫ ЦВЕТА СЛОЖНОСТИ ---
        when (task.difficulty) {
            "Легкая" -> {
                holder.tvDiff.setBackgroundColor(Color.parseColor("#C8E6C9")) // Светло-зеленый
            }
            "Средняя" -> {
                holder.tvDiff.setBackgroundColor(Color.parseColor("#FFE082")) // Светло-желтый
            }
            "Сложная" -> {
                holder.tvDiff.setBackgroundColor(Color.parseColor("#FFCDD2")) // Светло-красный
            }
        }

        holder.cbCompleted.setOnCheckedChangeListener(null)
        holder.cbCompleted.isChecked = task.isCompleted

        // Прозрачность для выполненных задач
        holder.itemView.alpha = if (task.isCompleted) 0.5f else 1.0f

        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(task, isChecked)
        }

        holder.itemView.setOnLongClickListener {
            onTaskLongClick(task)
            true
        }

        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }
    }

    override fun getItemCount() = tasks.size
}