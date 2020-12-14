package task

import androidx.room.Entity
import androidx.room.PrimaryKey

//Merepresentasikan tabel SQLite bernama task_table
@Entity(tableName = "task_table")

//Menunjukkan bahwa tabel note_table berisi kolom id dengan tipe data int dengan auto increment
data class Task(
    var judul: String,
    var deadline: String,
    var deskripsi: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}