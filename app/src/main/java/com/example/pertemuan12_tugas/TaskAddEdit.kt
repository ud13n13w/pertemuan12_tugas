package com.example.pertemuan12_tugas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task_add_edit.*

class TaskAddEdit : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.example.pertemuan12_tugas.EXTRA_ID"
        const val EXTRA_JUDUL = "com.example.pertemuan12_tugas.EXTRA_JUDUL"
        const val EXTRA_DEADLINE = "com.example.pertemuan12_tugas.EXTRA_DEADLINE"
        const val EXTRA_DESKRIPSI = "com.example.pertemuan12_tugas.EXTRA_DESKRIPSI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_add_edit)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)

        //Jika intent memiliki ID (Ketika Item dipilih pada MainActivity)
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Task"
            edit_judul.setText(intent.getStringExtra(EXTRA_JUDUL))
            edit_deadline.setText(intent.getStringExtra(EXTRA_DEADLINE))
            edit_deskripsi.setText(intent.getStringExtra(EXTRA_DESKRIPSI))
        }else {
            title = "Tambah Task"
        }
    }

    //Override menu pada activity agar sesuai dengan custom menu kita
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_task_menu, menu)
        return true
    }

    //Trigger event, ketika layout save di tekan, maka akan menjalankan function saveNote()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.save_task -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveTask() {

        //Jika data yang dimasukkan masih kosong
        if (edit_judul.text.toString().trim().isBlank() || edit_deskripsi.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Task kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        //Jika data tidak kosong, maka kirim semua data ke intent dan kirim sebagai RESULT OK
        val data = Intent().apply {
            putExtra(EXTRA_JUDUL, edit_judul.text.toString())
            putExtra(EXTRA_DEADLINE, edit_deadline.text.toString())
            putExtra(EXTRA_DESKRIPSI, edit_deskripsi.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }



}