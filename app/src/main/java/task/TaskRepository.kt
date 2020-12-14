package task

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

//Repository merupakan langkah untuk memisahkan antara operasi data dengan arsitektur
//Repository memisahkan hal tersebut dengan menggunakan API
//Repository ditujukan untuk mengelola query dan mengelola banyak data backend
//Repository adalah algoritma untuk memutuskan mengambil data dari jaringan atau local database(DAO)

class TaskRepository(application: Application) {

    private var taskDao: TaskDao

    private var allTask: LiveData<List<Task>>

    init {
        val database: TaskDatabase = TaskDatabase.getInstance(
            application.applicationContext
        )!!
        taskDao = database.taskDao()
        allTask = taskDao.getAllTask()
    }

    //Penerapan Asyntask pada method wrapper sesuai dengan method yg ada pada DAO

    fun insert(task: Task) {
        val insertTaskAsyncTask = InsertTaskAsyncTask(taskDao).execute(task)
    }

    fun update(task: Task) {
        val updateTaskAsyncTask = UpdateTaskAsyncTask(taskDao).execute(task)
    }

    fun delete(task: Task) {
        val deleteTaskAsyncTask = DeleteTaskAsyncTask(taskDao).execute(task)
    }

    fun deleteAllTask() {
        val deleteAllTaskAsyncTask = DeleteAllTaskAsyncTask(
            taskDao
        ).execute()
    }

    fun getAllTask(): LiveData<List<Task>> {
        return allTask
    }

    //Asyntask ditujuan agar aplikasi dapat dijalankan pada masing-masing thread
    //Asyntask dibuat dengan menerapkan tugas apa yang ingin dijalankan pada thread yg berbeda
       //pada studi kasus ini digunakan untuk pengoperasian data pada database melalui DAO
    companion object {
        private class InsertTaskAsyncTask(taskDao: TaskDao) : AsyncTask<Task, Unit, Unit>() {
            val taskDao = taskDao
            override fun doInBackground(vararg p0: Task?) {
                taskDao.insert(p0[0]!!)
            }
        }
        private class UpdateTaskAsyncTask(taskDao: TaskDao) : AsyncTask<Task, Unit, Unit>() {
            val taskDao = taskDao
            override fun doInBackground(vararg p0: Task?) {
                taskDao.update(p0[0]!!)
            }
        }
        private class DeleteTaskAsyncTask(taskDao: TaskDao) : AsyncTask<Task, Unit, Unit>() {
            val taskDao = taskDao
            override fun doInBackground(vararg p0: Task?) {
                taskDao.delete(p0[0]!!)
            }
        }
        private class DeleteAllTaskAsyncTask(taskDao: TaskDao) : AsyncTask<Unit, Unit, Unit>() {
            val taskDao = taskDao
            override fun doInBackground(vararg p0: Unit?) {
                taskDao.deleteAllTask()
            }
        }
    }
}