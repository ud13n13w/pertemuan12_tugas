package task

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

//Room database digunakan untuk mengelola penugasan SQLite Database
//Room database menggunakan DAO untuk meng-query Database

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase? {
            if (instance == null) {
                synchronized(TaskDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java, "task_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }
        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    //Proses penerapan asyntask pada database
    class PopulateDbAsyncTask(db: TaskDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val taskDao = db?.taskDao()
        override fun doInBackground(vararg p0: Unit?) {
            taskDao?.insert(Task("Matematika", "27 September 2020","Tugas mengerjakan kalkulus bab 7 halaman 2"))
            taskDao?.insert(Task("Fisika", "23 September 2020","Tugas buku paket halaman 87 Poin A-C"))
        }
    }
}