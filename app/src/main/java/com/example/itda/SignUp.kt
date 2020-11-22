package com.example.itda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.signup_page.*
import kotlinx.android.synthetic.main.signup_page.email_edittext
import kotlinx.android.synthetic.main.signup_page.password_edittext


class SignUp: AppCompatActivity() {
    //firebase 선언
    var firestore : FirebaseFirestore?=null
    var auth: FirebaseAuth? = null
    var userDTO = UserDTO()
    var emailCheck = false
    var emailAddress="email"
    var password="password"
    var passwordCheck="password check"
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        firestore = FirebaseFirestore.getInstance()
        val age_array = Array(100,{i->(2020-i)})
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, age_array)
        year_spinner.adapter = myAdapter
        var sex = "Male"
        var marital = true
        var age = 1
        //아이디 중복체크
        check_id.setOnClickListener {
                var email = email_edittext.text.toString()
                println("input email ->" + email)
                var duplicateCheck = false
                firestore!!.collection("accounts").whereEqualTo("Email", email).get()
                    .addOnCompleteListener { task ->
                        Log.d("firebase", "email check success")
                        duplicateCheck = task.result!!.documents.isNullOrEmpty()
                        Log.d("firebase", "$duplicateCheck")
                        println("duplicate -> " + duplicateCheck)
                        if (duplicateCheck) {
                            Toast.makeText(this, "You can use this email", Toast.LENGTH_SHORT).show()
                            emailCheck=true
                        } else {
                            Toast.makeText(this, "You can not use this email", Toast.LENGTH_SHORT).show()
                        }
                    }
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
                if (radio_female.isChecked) {
                    sex = "Female"
                }
                if (radio_single.isChecked) {
                    marital = false
                }
                age = 2020 - year_spinner.getSelectedItem().toString().toInt() + 1
                userDTO = UserDTO(
                    emailAddress, password,
                    name_edittext.text.toString(), sex, age, marital
                )
                firestore!!.collection("accounts").document().set(userDTO)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Toast.makeText(this, "Sign complete!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                createEmail()
            }else if (!emailCheck){
                Toast.makeText(this, "Please check ID", Toast.LENGTH_SHORT).show()
            }else if(!password.isNullOrBlank()&&(password!=passwordCheck)){
                Toast.makeText(this, "Please check the password", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Please enter 어쩌구저쩌구", Toast.LENGTH_SHORT).show()
            }

        }
        //Cancel 버튼 누를시 전페이지로 돌아감
        cancel_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        }



    fun createEmail() {
        progressBar.visibility = View.VISIBLE
        progressBar.bringToFront()
        Log.d("firebase", "try create account")
        auth?.createUserWithEmailAndPassword(emailAddress, password)
            ?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d("firebase", "create account complete")
                    //아이디 생성이 성공했을 경우
                    Toast.makeText(this,
                        "Sign-up complete!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Log.d("firebase", "create account failed")
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
data class UserDTO(var email:String?=null, var pw:String?=null, var name:String?=null, var sex:String?=null, var age:Int?=null, var marital:Boolean?=null)