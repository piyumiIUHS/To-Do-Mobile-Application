package com.example.myapplication1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {

    private lateinit var firstTask: EditText
    private lateinit var secondTask: EditText
    private lateinit var thirdTask: EditText
    private lateinit var fourthTask: EditText
    private lateinit var fifthTask: EditText
    private lateinit var deleteFirstTask: Button
    private lateinit var editFirstTask: Button
    private lateinit var saveFirstTask: Button
    private lateinit var deleteSecondTask: Button
    private lateinit var saveSecondTask: Button
    private lateinit var deleteThirdTask: Button
    private lateinit var saveThirdTask: Button
    private lateinit var deleteFourthTask: Button
    private lateinit var saveFourthTask: Button
    private lateinit var deleteFifthTask: Button
    private lateinit var saveFifthTask: Button
    private lateinit var addButton: ImageButton
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        firstTask = findViewById(R.id.fisttask)
        secondTask = findViewById(R.id.secondtask)
        thirdTask = findViewById(R.id.thirdtask)
        fourthTask = findViewById(R.id.fourthtask)
        fifthTask = findViewById(R.id.fifthtask)
        deleteFirstTask = findViewById(R.id.delete_firsttask)
        editFirstTask = findViewById(R.id.edit_firsttask)
        saveFirstTask = findViewById(R.id.saveBtn2)
        deleteSecondTask = findViewById(R.id.delete_secondtask)
        saveSecondTask = findViewById(R.id.saveBtn2)
        deleteThirdTask = findViewById(R.id.delete_thirdtask)
        saveThirdTask = findViewById(R.id.saveBtn3)
        deleteFourthTask = findViewById(R.id.delete_fourthtask)
        saveFourthTask = findViewById(R.id.saveBtn4)
        deleteFifthTask = findViewById(R.id.delete_fifthtask)
        saveFifthTask = findViewById(R.id.saveBtn5)
        addButton = findViewById(R.id.addbutton)

        mDatabase = FirebaseDatabase.getInstance().reference.child("tasks")

        // Load tasks from database
        loadTasks()

        saveFirstTask.setOnClickListener {
            saveTask("task1", firstTask.text.toString())
        }

        saveSecondTask.setOnClickListener {
            saveTask("task2", secondTask.text.toString())
        }

        saveThirdTask.setOnClickListener {
            saveTask("task3", thirdTask.text.toString())
        }

        saveFourthTask.setOnClickListener {
            saveTask("task4", fourthTask.text.toString())
        }

        saveFifthTask.setOnClickListener {
            saveTask("task5", fifthTask.text.toString())
        }

        editFirstTask.setOnClickListener {
            updateTask("task1", firstTask.text.toString())
        }

        deleteFirstTask.setOnClickListener {
            deleteTask("task1")
        }

        deleteSecondTask.setOnClickListener {
            deleteTask("task2")
        }

        deleteThirdTask.setOnClickListener {
            deleteTask("task3")
        }

        deleteFourthTask.setOnClickListener {
            deleteTask("task4")
        }

        deleteFifthTask.setOnClickListener {
            deleteTask("task5")
        }

        addButton.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun loadTasks() {
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val task1 = dataSnapshot.child("task1").getValue(String::class.java)
                    val task2 = dataSnapshot.child("task2").getValue(String::class.java)
                    val task3 = dataSnapshot.child("task3").getValue(String::class.java)
                    val task4 = dataSnapshot.child("task4").getValue(String::class.java)
                    val task5 = dataSnapshot.child("task5").getValue(String::class.java)

                    firstTask.setText(task1)
                    secondTask.setText(task2)
                    thirdTask.setText(task3)
                    fourthTask.setText(task4)
                    fifthTask.setText(task5)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MainActivity2", "Failed to load tasks: ${databaseError.message}")
                Toast.makeText(this@MainActivity2, "Failed to load tasks", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveTask(taskId: String, taskValue: String) {
        mDatabase.child(taskId).setValue(taskValue).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("MainActivity2", "Failed to save task: ${task.exception?.message}")
                Toast.makeText(this, "Failed to save task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTask(taskId: String, taskValue: String) {
        mDatabase.child(taskId).setValue(taskValue).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("MainActivity2", "Failed to update task: ${task.exception?.message}")
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTask(taskId: String) {
        mDatabase.child(taskId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                when (taskId) {
                    "task1" -> firstTask.setText("")
                    "task2" -> secondTask.setText("")
                    "task3" -> thirdTask.setText("")
                    "task4" -> fourthTask.setText("")
                    "task5" -> fifthTask.setText("")
                }
            } else {
                Log.e("MainActivity2", "Failed to delete task: ${task.exception?.message}")
                Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddTaskDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        dialogBuilder.setView(dialogView)

        val newTaskEditText = dialogView.findViewById<EditText>(R.id.newTaskEditText)

        dialogBuilder.setTitle("Add New Task")
        dialogBuilder.setPositiveButton("Add") { dialog, _ ->
            val newTask = newTaskEditText.text.toString()
            if (newTask.isNotEmpty()) {
                addNewTask(newTask)
            } else {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun addNewTask(task: String) {
        val newTaskId = mDatabase.push().key ?: return
        mDatabase.child(newTaskId).setValue(task).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "New task added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("MainActivity2", "Failed to add new task: ${task.exception?.message}")
                Toast.makeText(this, "Failed to add new task", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
