package com.example.pertemuan12_tugas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*
import task.Task

//Adapter digunakan untuk melakukan perulangan Item yang bersifat dinamis pada Item Layout (RecyclerView_item)
class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.judul == newItem.judul && oldItem.deskripsi == newItem.deskripsi
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val currentTask: Task = getItem(position)
        holder.tv_judul.text = currentTask.judul
        holder.tv_deadline.text = currentTask.deadline
        holder.tv_deskripsi.text = currentTask.deskripsi
    }

    fun getTaskAt(position: Int): Task {
        return getItem(position)
    }

    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var tv_judul: TextView = itemView.tv_judul
        var tv_deadline: TextView = itemView.tv_deadline
        var tv_deskripsi: TextView = itemView.tv_deskripsi
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}