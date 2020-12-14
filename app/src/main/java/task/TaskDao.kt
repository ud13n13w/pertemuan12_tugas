package task

import androidx.lifecycle.LiveData
import androidx.room.*

//Dao digunakan untuk pengoperasian query database dengan cara pemanggilan method
@Dao
interface TaskDao {

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAllTask()

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTask(): LiveData<List<Task>>

}