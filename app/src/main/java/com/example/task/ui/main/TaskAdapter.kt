package com.example.task.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.data.model.TaskModel

class TaskAdapter : ListAdapter<TaskModel, TaskAdapter.TaskHolder>(diffUtil) {

    class TaskHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val id_sample_task_number: TextView = itemView.findViewById(R.id.id_sample_task_number)
        val id_sample_problem_number: TextView =
            itemView.findViewById(R.id.id_sample_problem_number)

        fun bind(taskModel: TaskModel) {
            id_sample_task_number.text = taskModel.task
            id_sample_problem_number.text = taskModel.problem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rec_row, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskHolder = getItem(position)
        holder.bind(taskHolder)
    }

    companion object {
        val diffUtil = object : ItemCallback<TaskModel>() {
            override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
                return oldItem.task == newItem.task &&
                        oldItem.problem == newItem.problem
            }

            override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
                return oldItem.task == newItem.task &&
                        oldItem.problem == newItem.problem
            }

        }
    }

}