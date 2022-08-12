package com.example.task.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task.R
import com.example.task.data.model.TaskModel
import com.example.task.data.model.TaskModelAll
import com.example.task.ui.allusers.AllUsersActivity
import com.example.task.ui.allusers.AllUsersTaskAdapter
import com.example.task.ui.auth.LoginActivity
import com.example.task.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jgabrielfreitas.core.BlurImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var email: String
    private lateinit var id: String
    private lateinit var imageView: BlurImageView

    private lateinit var sabbirAdapter: AllUsersTaskAdapter
    private lateinit var tarequeAdapter: AllUsersTaskAdapter
    private lateinit var jubayerAdapter: AllUsersTaskAdapter
    private lateinit var ashikAdapter: AllUsersTaskAdapter
    private lateinit var ebrahimAdapter: AllUsersTaskAdapter
    private lateinit var sabbirArrayList: ArrayList<TaskModelAll>
    private lateinit var tarequeArrayList: ArrayList<TaskModelAll>
    private lateinit var jubayerArrayList: ArrayList<TaskModelAll>
    private lateinit var ashikArrayList: ArrayList<TaskModelAll>
    private lateinit var ebrahimArrayList: ArrayList<TaskModelAll>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)

        imageView = findViewById(R.id.id_banner_image)
        mAuth = FirebaseAuth.getInstance()
        email = mAuth.currentUser?.email.toString()
        id = mAuth.currentUser?.uid.toString()
        firebaseFirestore = Firebase.firestore
        firebaseDatabase = Firebase.database
        swipeRefreshLayout = findViewById(R.id.main_root)

        sabbirAdapter = AllUsersTaskAdapter()
        tarequeAdapter = AllUsersTaskAdapter()
        jubayerAdapter = AllUsersTaskAdapter()
        ashikAdapter = AllUsersTaskAdapter()
        ebrahimAdapter = AllUsersTaskAdapter()

        sabbirArrayList = ArrayList()
        tarequeArrayList = ArrayList()
        jubayerArrayList = ArrayList()
        ashikArrayList = ArrayList()
        ebrahimArrayList = ArrayList()

        setUpView()

        getAllList("sabbir@gmail.com", id_sabbir_rec, sabbirArrayList)
        getAllList("tareque@gmail.com", id_tareque_rec, tarequeArrayList)
        getAllList("jubayer@gmail.com", id_jubayer_rec, jubayerArrayList)
        getAllList("ashik@gmail.com", id_ashik_rec, ashikArrayList)
        getAllList("ebrahim@gmail.com", id_ebrahim_rec, ebrahimArrayList)

        swipeRefreshLayout.setOnRefreshListener {
            setUpView()
            sabbirArrayList.clear()
            tarequeArrayList.clear()
            jubayerArrayList.clear()
            ashikArrayList.clear()
            ebrahimArrayList.clear()
            getAllList("sabbir@gmail.com", id_sabbir_rec, sabbirArrayList)
            getAllList("tareque@gmail.com", id_tareque_rec, tarequeArrayList)
            getAllList("jubayer@gmail.com", id_jubayer_rec, jubayerArrayList)
            getAllList("ashik@gmail.com", id_ashik_rec, ashikArrayList)
            getAllList("ebrahim@gmail.com", id_ebrahim_rec, ebrahimArrayList)

        }

//        on task done
        id_btn_mark.setOnClickListener {
            taskDone()
            allUsers()
        }
    }

    private fun setUpView() {
        //        banner image
        firebaseFirestore.collection("banner")
            .document("bannerImage")
            .get()
            .addOnSuccessListener { result ->
                if (result != null && result.exists()) {
                    val document = result.getString("bannerImageLink")
                    Log.d(TAG, "imageLink: $document")
                    Picasso
                        .get()
                        .load(document)
                        .placeholder(R.drawable.loading)
                        .into(imageView)
                    swipeRefreshLayout.isRefreshing = false
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        //task number set
        firebaseFirestore.collection("TaskNumber").document("task")
            .get()
            .addOnSuccessListener { data ->
                if (data.data != null && data.exists()) {
                    val taskNumber = data.getString("task")
                    Log.d(TAG, "TaskNumber: $taskNumber")
                    id_task_number_text.text = taskNumber.toString()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        //task problem set
        firebaseFirestore.collection("TaskNumber").document("task")
            .get()
            .addOnSuccessListener { data ->
                if (data.data != null && data.exists()) {
                    val taskNumber = data.getString("problem")
                    Log.d(TAG, "problem: $taskNumber")
                    id_problem_text.text = taskNumber.toString()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        //task last date set
        firebaseFirestore.collection("TaskNumber").document("task")
            .get()
            .addOnSuccessListener { data ->
                if (data.data != null && data.exists()) {
                    val taskLastDate = data.getString("lastDate")
                    Log.d(TAG, "lastDate: $taskLastDate")
                    id_task_last_date_text.text = taskLastDate.toString()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun taskDone() {
        val task = id_task_number_text.text.toString()
        val problem = id_problem_text.text.toString()
        if (task.isEmpty()) {
            Toast.makeText(this, "Empty task", Toast.LENGTH_SHORT).show()
        } else if (problem.isEmpty()) {
            Toast.makeText(this, "Empty problem", Toast.LENGTH_SHORT).show()
        } else {
            val taskModel = TaskModel(task = task, problem = problem)
            firebaseFirestore.collection(email)
                .document(taskModel.task.toString())
                .set(taskModel)
                .addOnSuccessListener { task ->
                    Log.d(TAG, "taskDone: $task")
                    Toast.makeText(this, "Task completed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Log.d(TAG, "taskDone: ${e.message}")
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun allUsers() {
        val task = id_task_number_text.text.toString()
        val problem = id_problem_text.text.toString()
        val taskModel = TaskModel(task = task, problem = problem)
        firebaseDatabase.getReference("AllUsers")
            .child(id)
            .child(taskModel.task.toString())
            .setValue(taskModel).addOnSuccessListener { task ->
                Log.d(TAG, "allUsers: $task")
            }.addOnFailureListener { e ->
                Log.d(TAG, "allUsers: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun getAllList(email: String, rec: RecyclerView, arrayList: ArrayList<TaskModelAll>) {
        firebaseFirestore.collection(email)
            .get()
            .addOnSuccessListener {
                val list: MutableList<DocumentSnapshot> = it.documents
                list.forEach { data ->
                    val taskModelAll = data.toObject(TaskModelAll::class.java)
                    arrayList.add(taskModelAll!!)
                }
                Log.d(TAG, "getAllList: $email: $arrayList")
                when (email) {
                    "ebrahim@gmail.com" -> {
                        ebrahimAdapter.submitList(arrayList)
                        ebrahimAdapter.notifyDataSetChanged()
                        setUpRec(ebrahimAdapter, rec)
                    }
                    "sabbir@gmail.com" -> {
                        sabbirAdapter.submitList(arrayList)
                        sabbirAdapter.notifyDataSetChanged()
                        setUpRec(sabbirAdapter, rec)
                    }
                    "tareque@gmail.com" -> {
                        tarequeAdapter.submitList(arrayList)
                        tarequeAdapter.notifyDataSetChanged()
                        setUpRec(tarequeAdapter, rec)
                    }
                    "jubayer@gmail.com" -> {
                        jubayerAdapter.submitList(arrayList)
                        jubayerAdapter.notifyDataSetChanged()
                        setUpRec(jubayerAdapter, rec)
                    }
                    "ashik@gmail.com" -> {
                        ashikAdapter.submitList(arrayList)
                        ashikAdapter.notifyDataSetChanged()
                        setUpRec(ashikAdapter, rec)
                    }
                }
            }.addOnFailureListener { e ->
                Log.d(TAG, "getAllList: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setUpRec(userAdapter: AllUsersTaskAdapter, rec: RecyclerView) {
        rec.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = userAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_menu_logout -> {
                if (mAuth.currentUser != null) {
                    mAuth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            R.id.id_menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.id_menu_all_task -> {
                startActivity(Intent(this, AllUsersActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
