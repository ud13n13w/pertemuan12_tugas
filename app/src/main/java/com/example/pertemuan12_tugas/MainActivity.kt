package com.example.pertemuan12_tugas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import task.Task

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ketika Action button di klick
        buttonAddTask.setOnClickListener {
            startActivityForResult(
                Intent(this, TaskAddEdit::class.java), ADD_NOTE_REQUEST
            )
        }

        //Settting RecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        val adapter = TaskAdapter()
        recycler_view.adapter = adapter

        //Setting ViewModel
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        taskViewModel.getAllTask().observe(this, Observer<List<Task>> {
            adapter.submitList(it)
        })

        //Item touch helper untuk membantu melakukan trigger action ketika event terjadi pada item
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {

            //Ketika menambahkan data baru, maka memperbaharui instance recyclerview, viewholder, dan target
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //Ketika item di swiped, maka akan menjalankan function delete() dan data akan terhapus
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Task dihapus!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recycler_view)

        //Ketika item dipilih, maka akan mengirimkan data dari item yg dipilih dan intent berupa Edit Request
        adapter.setOnItemClickListener(object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(task: Task) {
                val intent = Intent(baseContext, TaskAddEdit::class.java)
                intent.putExtra(TaskAddEdit.EXTRA_ID, task.id)
                intent.putExtra(TaskAddEdit.EXTRA_JUDUL, task.judul)
                intent.putExtra(TaskAddEdit.EXTRA_DESKRIPSI, task.deskripsi)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //Ketika option menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {

            //Ketika menu option delete all menu dipilih, maka akan menjalankan function deleteAllNotes()
              // untuk menghapus semua data note yang tersimpan
            R.id.delete_all_task -> {
                taskViewModel.deleteAllTask()
                Toast.makeText(this, "Semua sudah dihapus!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    //Action ketika activity menerima result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Jika result intent berupa ADD_NOTE_REQUEST dan RESULT_OK, maka jalankan insert()
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newTask = Task(
                data!!.getStringExtra(TaskAddEdit.EXTRA_JUDUL).toString(),
                data.getStringExtra(TaskAddEdit.EXTRA_DEADLINE).toString(),
                data.getStringExtra(TaskAddEdit.EXTRA_DESKRIPSI).toString()
            )
            taskViewModel.insert(newTask)
            Toast.makeText(this, "Task disimpan!", Toast.LENGTH_SHORT).show()

        //Jika result intent berupa EDIT_NOTE_REQUEST dan RESULT_OK, maka jalankan update()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(TaskAddEdit.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Pembaharuan gagal!", Toast.LENGTH_SHORT).show()
            }
            val updateTask = Task(
                data!!.getStringExtra(TaskAddEdit.EXTRA_JUDUL).toString(),
                data.getStringExtra(TaskAddEdit.EXTRA_DEADLINE).toString(),
                data.getStringExtra(TaskAddEdit.EXTRA_DESKRIPSI).toString()
            )
            updateTask.id = data.getIntExtra(TaskAddEdit.EXTRA_ID, -1)
            taskViewModel.update(updateTask)
        } else {
            Toast.makeText(this, "Task tidak disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}