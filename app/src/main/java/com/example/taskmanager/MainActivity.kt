package com.example.taskmanager

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Adapter.ToDoAdapter
import com.example.taskmanager.Model.ToDoModel
import com.example.taskmanager.Utils.DataBaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), OnDialogCloseListner {

    private var mRecyclerview: RecyclerView? = null
    private var fab: FloatingActionButton? = null
    private var myDB: DataBaseHelper? = null
    private var mList: List<ToDoModel?> = ArrayList()
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views and database helper
        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this)
        adapter = ToDoAdapter(myDB, this@MainActivity)

        // Set up RecyclerView
        mRecyclerview?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        // Get all tasks from the database
        mList = myDB?.getAllTasks() ?: ArrayList()
        mList = mList.reversed()
        adapter.setTasks(mList)

        // FAB click listener to open AddNewTask dialog
        fab?.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
            // Handle FAB click event here
        }

        // Set up ItemTouchHelper for swipe-to-delete functionality
        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)
    }


    // This method is called when a dialog is closed
    override fun onDialogClose(dialogInterface: DialogInterface) {
        mList = myDB?.getAllTasks() ?: ArrayList()
        mList = mList.reversed()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
    }
}