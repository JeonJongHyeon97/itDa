package com.example.itda

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.signup_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUp: AppCompatActivity() {
    //firebase 선언
    var firestore : FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        firestore = FirebaseFirestore.getInstance()

        check_id.setOnClickListener {
                var email = email_edittext.text.toString()
                println("input email ->" + email)
                var duplicateCheck = false
                firestore!!.collection("accounts").whereEqualTo("Email", email).get()
                    .addOnCompleteListener { task ->
                        Log.d("firebase", "success")
                        duplicateCheck = task.result.documents.isNullOrEmpty()
                        Log.d("firebase", "$duplicateCheck")
                        println("duplicate -> " + duplicateCheck)
                        if (duplicateCheck) {
                            Toast.makeText(this, "You can use this email", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "You can not use this email", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("firebase", "failed")
                        Toast.makeText(this, "Please try later", Toast.LENGTH_SHORT).show()
                    }
            }
        }

//    fun checkId(email:String): Boolean{
//        var result = false
//        firestore?.collection("accounts")?.whereEqualTo("Email",email)?.get()?.
//        addOnCompleteListener{
//            task ->
//            if(task.isSuccessful){
//                println("task is sucessful")
//                result= task.result?.documents.isNullOrEmpty()
//            }
//        }
//        return result
//    }
//    CoroutineScope(IO).launch{
//
//    }
//     fun checkId(email:String): Boolean{
//        var bool = false
//            firestore!!.collection("accounts").whereEqualTo("Email", email).get()
//                .addOnCompleteListener { task ->
//                    Log.d("firebase", "success")
//                    bool = task.result.documents.isNullOrEmpty()
//                    Log.d("firebase", "$bool")
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("firebase", "failed")
//                    Toast.makeText(this, "Please try later", Toast.LENGTH_SHORT).show()
//                }
//    return bool
//    }

//    fun createAndLoginEmail() {
//
//        auth?.createUserWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
//            ?.addOnCompleteListener { task ->
//                progress_bar.visibility = View.GONE
//                if (task.isSuccessful) {
//                    //아이디 생성이 성공했을 경우
//                    Toast.makeText(this,
//                        "회원가입 성공", Toast.LENGTH_SHORT).show()
//
//                    //다음페이지 호출
//                    moveMainPage(auth?.currentUser)
//                } else if (task.exception?.message.isNullOrEmpty()) {
//                    //회원가입 에러가 발생했을 경우
//                    Toast.makeText(this,
//                        task.exception!!.message, Toast.LENGTH_SHORT).show()
//                } else {
//                    //아이디 생성도 안되고 에러도 발생되지 않았을 경우 로그인
//                    signinEmail()
//                }
//            }
//
//    }
}