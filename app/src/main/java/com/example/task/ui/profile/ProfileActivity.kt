package com.example.task.ui.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.data.model.TaskModel
import com.example.task.ui.main.TaskAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

private const val TAG = "profileActivity"

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var email: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var pAdapter: TaskAdapter
    private lateinit var pList: ArrayList<TaskModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()
        firebaseFirestore = Firebase.firestore
        email = mAuth.currentUser?.email.toString()
        pAdapter = TaskAdapter()
        pList = ArrayList()

        firebaseFirestore.collection(email).get()
            .addOnSuccessListener {
                val list: MutableList<DocumentSnapshot> = it.documents
                list.forEach { data ->
                    val taskModel = data.toObject(TaskModel::class.java)
                    pList.add(taskModel!!)
                }
                pAdapter.submitList(pList)
                pAdapter.notifyDataSetChanged()
                Log.d(TAG, "onCreate: $pList")
            }.addOnFailureListener { e ->
                Log.d(TAG, "onCreate: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        id_my_task_rec.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = pAdapter
        }

    }
}