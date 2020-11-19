package com.example.itda


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    // Firebase Authentication 관리 클래스
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Firebase 로그인 통합 관리하는 Object 만들기
        auth = FirebaseAuth.getInstance()

        //이메일 로그인 세팅
        email_login_button.setOnClickListener { emailLogin() }
    }

    fun moveMainPage(user: FirebaseUser?) {

        // User is signed in
        if (user != null) {
            Toast.makeText(this, "sign-in complete", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    //이메일 회원가입 및 로그인 메소드
    fun createAndLoginEmail() {

        auth?.createUserWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE
                if (task.isSuccessful) {
                    //아이디 생성이 성공했을 경우
                    Toast.makeText(this,
                        getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()

                    //다음페이지 호출
                    moveMainPage(auth?.currentUser)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                } else {
                    //아이디 생성도 안되고 에러도 발생되지 않았을 경우 로그인
                    signinEmail()
                }
            }

    }

    fun emailLogin() {

        if (email_edittext.text.toString().isNullOrEmpty() || password_edittext.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.signout_fail_null), Toast.LENGTH_SHORT).show()

        } else {

            progress_bar.visibility = View.VISIBLE
            createAndLoginEmail()

        }
    }

    //로그인 메소드
    fun signinEmail() {

        auth?.signInWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE

                if (task.isSuccessful) {
                    //로그인 성공 및 다음페이지 호출
                    moveMainPage(auth?.currentUser)
                } else {
                    //로그인 실패
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

    }


    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE
                if (task.isSuccessful) {


                    //다음페이지 호출
                    moveMainPage(auth?.currentUser)
                }
            }
    }
    override fun onStart() {
        super.onStart()

        //자동 로그인 설정
        moveMainPage(auth?.currentUser)

    }
}
