package com.example.itda.homefolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itda.MainActivity
import com.example.itda.MyApplication
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.signup_page.*
import kotlinx.android.synthetic.main.signup_page.email_edittext
import kotlinx.android.synthetic.main.signup_page.password_edittext

class SignUp: AppCompatActivity() {
    //firebase 선언
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var userDTO = UserDTO()
    var emailCheck = false
    var emailAddress="email"
    var password="password"
    var passwordCheck="password check"
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        val age_array = Array(100,{i->(2020-i)})
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, age_array)
        year_spinner.adapter = myAdapter
        var sex = "Male"
        var marital = true
        var age = 1
        //confirm repetition of ID
        check_id.setOnClickListener {
                var email = email_edittext.text.toString()
                println("input email ->" + email)
                var duplicateCheck = false
                // check whether the ID is already existed in firebase
                firestore!!.collection("accounts").whereEqualTo("Email", email).get()
                    .addOnCompleteListener { task ->
                        Log.d("firebase", "email check success")
                        duplicateCheck = task.result!!.documents.isNullOrEmpty() // Null or Empty => new ID
                        Log.d("firebase", "$duplicateCheck")
                        if (duplicateCheck) {
                            // not overlapping
                            Toast.makeText(this, "You can use this email", Toast.LENGTH_SHORT).show()
                            // convert emailCheck:Boolean to True
                            emailCheck=true
                        } else {
                            //overlapping
                            Toast.makeText(this, "You can not use this email", Toast.LENGTH_SHORT).show()
                        }
                    } // read firebase failed
                    .addOnFailureListener { exception ->
                        Log.d("firebase", "email check failed")
                        Toast.makeText(this, "Please try later", Toast.LENGTH_SHORT).show()
                    }
            }

        //Complete 버튼 누를시 회원가입
        complete_button.setOnClickListener{
            emailAddress=email_edittext.text.toString()
            password=password_edittext.text.toString()
            passwordCheck=password_check_edittext.text.toString()

            if (emailCheck&&(password==passwordCheck)) {
                if (radio_female.isChecked) {sex = "Female"}
                if (radio_single.isChecked) {marital = false}
                age = 2020 - year_spinner.getSelectedItem().toString().toInt() + 1
                //Assign random value for family key
                var familyKey = getRandomString(10)
                //Use data class to save data in firebase
                userDTO = UserDTO(emailAddress, password, name_edittext.text.toString(), sex, age, marital, familyKey)
                //Save data about user's account
                firestore!!.collection("accounts").document(emailAddress).set(userDTO)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Toast.makeText(this, "Sign complete!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                createEmail()
            }else if (!emailCheck){     //check whether the user check the id or not
                Toast.makeText(this, "Please check ID", Toast.LENGTH_SHORT).show()
            }else if(!password.isNullOrBlank()&&(password!=passwordCheck)){     //password check
                Toast.makeText(this, "Please check the password", Toast.LENGTH_SHORT).show()
            }else{  //check the empty text box
                Toast.makeText(this, "Please enter the value for all boxes", Toast.LENGTH_SHORT).show()
            }

        }
        //Cancel 버튼 누를시 전페이지로 돌아감
        cancel_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }



    fun createEmail() { // create authentication
        progressBar!!.visibility = View.VISIBLE
        progressBar!!.bringToFront()
        Log.d("firebase", "try create account")
        auth.createUserWithEmailAndPassword(emailAddress, password)     // create accounts with email
            .addOnCompleteListener { task ->
                progressBar!!.visibility = View.GONE
                if (task.isSuccessful) { // create success
                    Toast.makeText(this,
                        "Sign-up complete!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    MyApplication.prefs.setString("email", emailAddress)
                    startActivity(intent)
                    finish()    // finish the activity and back to log in page
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Log.d("firebase", "create account failed")
                    //error occur
                    Toast.makeText(this,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}
