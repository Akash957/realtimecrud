package com.example.realtimecrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), StudentAdapter.OnClickListener {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var studentList: ArrayList<StudentModel>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.floatingActivityButton)
        recyclerView = findViewById(R.id.recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        studentList = ArrayList<StudentModel>()
        getStudentList()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, activity_insert_data::class.java)
            startActivity(intent)
        }
    }

    private fun getStudentList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                if (snapshot.exists()) {
                    for (studentSnap in snapshot.children) {
                        val studentData = studentSnap.getValue(StudentModel::class.java)
                        studentList.add(studentData!!)
                    }
                    val studentAdapter = StudentAdapter(studentList, this@MainActivity, this@MainActivity)
                    recyclerView.adapter = studentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onUpdateStudentData(position: Int) {
        val intent = Intent(this, activity_update::class.java)
        intent.putExtra("id_key", studentList[position].id)
        intent.putExtra("StudentName", studentList[position].name)
        intent.putExtra("StudentEmail", studentList[position].email)
        startActivity(intent)
    }
    override fun onDeleteStudentData(position: Int) {
        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students to delete", Toast.LENGTH_SHORT).show()
            return
        }
        val studentId = studentList[position].id
        databaseReference.child(studentId!!).removeValue().addOnFailureListener {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this, "Data Delete Success", Toast.LENGTH_SHORT).show()
        }
    }
}
