package com.example.task.ui.allusers

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.data.model.TaskModel
import com.example.task.ui.main.TaskAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_users.*

private const val TAG = "allUsersActivity"

class AllUsersActivity : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var email: String
    private lateinit var mAuth: FirebaseAuth


    private lateinit var arrayList: ArrayList<TaskModel>
    private lateinit var mAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)

        mAuth = FirebaseAuth.getInstance()
        firebaseFirestore = Firebase.firestore
        firebaseDatabase = Firebase.database
        email = mAuth.currentUser?.email.toString()

        arrayList = ArrayList()
        mAdapter = TaskAdapter()

        allTask()

    }

    private fun allTask() {
        firebaseFirestore.collection("Task")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val taskModel = it.toObject(TaskModel::class.java)
                    arrayList.add(taskModel!!)
                }
                mAdapter.submitList(arrayList)
                mAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "onCreate: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        id_task_rec.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AllUsersActivity)
            adapter = mAdapter
        }
    }

}

