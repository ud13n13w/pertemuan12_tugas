package com.example.pertemuan12_tugas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import task.Task
import task.TaskRepository

//ViewModel digunakan untuk mengelola data yang dibutuhkan untuk ditampilkan kedalam Layout maupun Fragment
class TaskViewModel(application: Application) : AndroidViewModel(application)
{
    //Repository
    private var repository: TaskRepository = TaskRepository(application)

    //LiveData digunakan untuk kondisi jika terdapat data yang dapat berubah-ubah pada layout (dinamis)
    //Sehingga jika terjadi perubahan data pada database, maka LiveData akan mengubah tampilan pada layout
        // sesuai dengan data yang telah diubah
    private var allTask: LiveData<List<Task>> = repository.getAllTask()

    fun insert(task: Task) {
        repository.insert(task)
    }

    fun update(task: Task) {
        repository.update(task)
    }

    fun delete(task: Task) {
        repository.delete(task)
    }

    fun deleteAllTask() {
        repository.deleteAllTask()
    }

    fun getAllTask(): LiveData<List<Task>> {
        return allTask
    }
}