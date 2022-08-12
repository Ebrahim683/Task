package com.example.task.ui.admin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task.R
import com.example.task.data.model.TaskModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_admin.*

private const val TAG = "adminActivity"

class AdminActivity : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        firebaseFirestore = Firebase.firestore

        id_btn_submit.setOnClickListener {
            val task = id_admin_task_number.editText?.text.toString()
            val problem = id_admin_problem_number.editText?.text.toString()
            val date = id_admin_last_date.editText?.text.toString()

            if (task.isEmpty()) {
                Toast.makeText(this, "Enter task number", Toast.LENGTH_SHORT).show()
            } else if (problem.isEmpty()) {
                Toast.makeText(this, "Enter problem", Toast.LENGTH_SHORT).show()
            } else if (date.isEmpty()) {
                Toast.makeText(this, "Enter date", Toast.LENGTH_SHORT).show()
            } else {
                submitData(task, problem, date)
                submitDataAll(task, problem)
            }

        }

    }

    private fun submitData(task: String, problem: String, date: String) {
        val taskModel = TaskModel(task = task, problem = problem, lastDate = date)
        firebaseFirestore.collection("TaskNumber")
            .document("task")
            .set(taskModel)
            .addOnSuccessListener { task ->
                Log.d(TAG, "submitData: $task")
                Log.d(TAG, "submitData: data saved")
                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Log.d(TAG, "submitData: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitDataAll(task: String, problem: String) {
        val taskModel = TaskModel(task = task, problem = problem)
        firebaseFirestore.collection("Task")
            .add(taskModel)
            .addOnSuccessListener { task ->
                Log.d(TAG, "submitData: $task")
                Log.d(TAG, "submitData: data saved")
                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Log.d(TAG, "submitData: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}