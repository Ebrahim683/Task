package com.example.task.ui.allusers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.data.model.TaskModel
import com.example.task.data.model.TaskModelAll

class AllUsersTaskAdapter : ListAdapter<TaskModelAll, AllUsersTaskAdapter.TaskHolder>(diffUtil) {

    class TaskHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val id_sample_task_number_h: TextView = itemView.findViewById(R.id.id_sample_task_number_h)
        fun bind(taskModel: TaskModelAll) {
            id_sample_task_number_h.text = taskModel.task
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_rec_row_horizontal, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskHolder = getItem(position)
        holder.bind(taskHolder)
    }

    companion object {
        val diffUtil = object : ItemCallback<TaskModelAll>() {
            override fun areItemsTheSame(oldItem: TaskModelAll, newItem: TaskModelAll): Boolean {
                return oldItem.task == newItem.task
            }

            override fun areContentsTheSame(oldItem: TaskModelAll, newItem: TaskModelAll): Boolean {
                return oldItem.task == newItem.task
            }

        }
    }

}