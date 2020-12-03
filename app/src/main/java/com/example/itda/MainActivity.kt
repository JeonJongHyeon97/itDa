package com.example.itda


import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.itda.databinding.FragmentGameBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.login_page.email_edittext
import kotlinx.android.synthetic.main.login_page.password_edittext
import kotlinx.android.synthetic.main.signup_page.*
import java.util.Random
import androidx.drawerlayout.widget.DrawerLayout
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    // Firebase Authentication 관리 클래스
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<FragmentGameBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)

        setContentView(R.layout.login_page)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        signup_button.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        // Firebase 로그인 통합 관리하는 Object 만들기
        auth = FirebaseAuth.getInstance()

        //이메일 로그인 세팅
        login_button.setOnClickListener { emailLogin() }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


    fun moveMainPage(user: FirebaseUser?) {

        // User is signed in
        if (user != null) {
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
    }

    fun emailLogin() {

        if (email_edittext.text.toString().isNullOrEmpty() || password_edittext.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()

        } else {
            progress_bar!!.visibility = View.VISIBLE
            progress_bar!!.bringToFront()
            signinEmail()
        }
    }
    //로그인 메소드
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //로그인 성공 및 다음페이지 호출
                    progress_bar!!.visibility = View.GONE
                    MyApplication.prefs.setString("email", email_edittext.text.toString())
                    moveMainPage(auth?.currentUser)
                } else {
                    //로그인 실패
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    progress_bar!!.visibility = View.GONE
                }
            }
    }

    override fun onStart() {
        super.onStart()
        //자동 로그인 설정
        moveMainPage(auth?.currentUser)

    }

}