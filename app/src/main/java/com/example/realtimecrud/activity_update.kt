package com.example.realtimecrud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class activity_update : AppCompatActivity() {
    private lateinit var editTextName_update: AppCompatEditText
    private lateinit var editTextEmail_update: AppCompatEditText
    private lateinit var buttonUpdate: Button
    private lateinit var databaseReference: DatabaseReference
    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        editTextName_update = findViewById(R.id.editText_name_Update)
        editTextEmail_update = findViewById(R.id.editText_email_Update)
        buttonUpdate = findViewById(R.id.button_Update)

        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        studentId = intent.getStringExtra("id_key")
        val updateName = intent.getStringExtra("StudentName")
        val updateEmail = intent.getStringExtra("StudentEmail")

        editTextEmail_update.setText(updateEmail)
        editTextName_update.setText(updateName)

        buttonUpdate.setOnClickListener {
            updateDate()
        }
    }
    private fun updateDate(){
        val updateName = editTextName_update.text.toString()
        val updateEmail= editTextEmail_update.text.toString()

        if (studentId != null){
            val updateStudent = StudentModel(studentId!!,updateName,updateEmail)
            databaseReference.child(studentId!!).setValue(updateStudent)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Update", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "data failed", Toast.LENGTH_SHORT).show()
                }
        }


    }
}